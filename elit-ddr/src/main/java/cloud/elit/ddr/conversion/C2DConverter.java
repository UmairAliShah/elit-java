/*
 * Copyright 2018 Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cloud.elit.ddr.conversion;

import cloud.elit.ddr.constituency.CTArc;
import cloud.elit.ddr.constituency.CTNode;
import cloud.elit.ddr.constituency.CTTag;
import cloud.elit.ddr.constituency.CTTree;
import cloud.elit.ddr.conversion.headrule.HeadRule;
import cloud.elit.ddr.conversion.headrule.HeadRuleMap;
import cloud.elit.ddr.conversion.headrule.HeadTagSet;
import cloud.elit.ddr.util.DDGTag;
import cloud.elit.ddr.util.DSUtils;
import cloud.elit.ddr.util.PatternUtils;
import cloud.elit.sdk.structure.Sentence;
import cloud.elit.sdk.structure.node.NLPNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class C2DConverter {
    protected HeadRuleMap headrule_map;
    protected HeadRule default_rule;

    /**
     * The default rule takes the rightmost constituency as the head.
     */
    public C2DConverter(HeadRuleMap headrule_map) {
        this(headrule_map, new HeadRule(HeadRule.DIR_RIGHT_TO_LEFT));
    }

    /**
     * @param default_rule use this rule when no specified headrule matches.
     */
    public C2DConverter(HeadRuleMap headrule_map, HeadRule default_rule) {
        this.headrule_map = headrule_map;
        this.default_rule = default_rule;
    }

//  ============================= Abstract Methods =============================

    /**
     * @return the dependency graph converted from the constituency tree.
     * If the constituent tree contains only empty categories, returns {@code null}.
     */
    public abstract Sentence toDependencyGraph(CTTree tree);

    /**
     * Sets the head of the specific constituent node using the specific headrule.
     */
    protected abstract void findHead(CTNode node, HeadRule rule);

    /**
     * @return the head flag of the specific constituent node.
     */
    protected abstract int getHeadFlag(CTNode node);

    /**
     * Returns a dependency label given the specific phrase structure.
     * @param node the current node.
     * @param head the head of the current node.
     * @return a dependency label given the specific phrase structure.
     */
    protected abstract String getDependencyLabel(CTNode node, CTNode head);

//  ============================= Set Heads =============================

    /**
     * Sets the head of the specific node and all its sub-nodes.
     */
    protected void setHead(CTNode node) {
        if (node.isTerminal())
            return;

        // set the heads of the children first
        for (CTNode child : node.getChildren())
            setHead(child);

        // stop traversing if it is the top node
        if (node.isSyntacticTag(CTTag.TOP))
            return;

        // trivial case of one child
        if (node.numChildren() == 1) {
            node.setPhraseHead(node.getFirstChild());
            return;
        }

        // find the headrule of the current node
        HeadRule rule = headrule_map.get(node.getSyntacticTag());

        if (rule == null) {
            System.err.println("Error: headrules not found for \"" + node.getSyntacticTag() + "\"");
            rule = default_rule;
        }

        // abstract method
        findHead(node, rule);
    }

    /**
     * Every other node in the list becomes the dependent of the head node.
     * @param nodes the list of nodes.
     * @param rule  the headrule to be consulted.
     * @return the head of the input node-list according to the headrule.
     */
    protected CTNode findHeadDefault(List<CTNode> nodes, HeadRule rule) {
        CTNode head = getDefaultHead(nodes);

        if (head == null) {
            if (rule.isRightToLeft()) {
                nodes = new ArrayList<>(nodes);
                Collections.reverse(nodes);
            }

            int[] flags = nodes.stream().mapToInt(this::getHeadFlag).toArray();
            int flag_size = DSUtils.max(flags);

            outer:
            for (int flag = 0; flag <= flag_size; flag++) {
                for (HeadTagSet tagset : rule.getHeadTags()) {
                    for (int i = 0; i < nodes.size(); i++) {
                        CTNode node = nodes.get(i);

                        if (flags[i] == flag && tagset.matches(node)) {
                            head = node;
                            break outer;
                        }
                    }
                }
            }
        }

        if (head == null)
            throw new IllegalStateException("Head not found");

        for (CTNode node : nodes) {
            if (node != head && !node.hasPrimaryHead())
                setPrimaryHead(node, head);
        }

        return head;
    }

    protected void setPrimaryHead(CTNode node, CTNode head) {
        node.setPrimaryHead(head, getDependencyLabel(node, head));
    }

    /**
     * @return the default head if it is the only node in the list that is not an empty category.
     */
    private CTNode getDefaultHead(List<CTNode> nodes) {
        CTNode head = null;

        for (CTNode node : nodes) {
            if (!node.isEmptyCategoryPhrase()) {
                if (head != null) return null;
                head = node;
            }
        }

        return head;
    }

//  ============================= Get Dependency Graph =============================

    protected void finalizeDependencies(CTNode node) {
        if (node.hasPrimaryHead()) {
            CTArc arc = node.getPrimaryHead();
            CTNode head = arc.getNode().getTerminalHead();
            CTNode dep = node.getTerminalHead();
            dep.setPrimaryHead(head, arc.getLabel());
        }

        for (CTArc arc : node.getSecondaryHeads()) {
            if (arc.getNode() == null) continue;
            CTNode head = arc.getNode().getTerminalHead();
            CTNode dep = getTerminalHead(node);

            if (head != null && dep != null && dep != node)
                dep.addSecondaryHead(head, arc.getLabel());
            else
                arc.setNode(head);
        }

        for (CTNode child : node.getChildren())
            finalizeDependencies(child);
    }

    private CTNode getTerminalHead(CTNode node) {
        CTNode t = node.getTerminalHead();

        while (t.hasAntecedent())
            t = t.getAntecedent().getTerminalHead();

        return t;
    }

    /**
     * @return the dependency graph converted from the specific constituent tree without head information.
     */
    protected Sentence createDependencyGraph(CTTree tree) {
        List<CTNode> tokens = tree.getTokens();
        Sentence graph = new Sentence();
        String form, pos, lemma, nament;
        NLPNode node, head;
        int id;

        for (CTNode token : tokens) {
            id = token.getTokenID();
            form = token.getForm();
            lemma = token.getLemma();
            pos = token.getSyntacticTag();
            graph.add(new NLPNode(id, form, lemma, pos, token.getFeatMap()));
        }

        for (CTNode token : tokens) {
            node = graph.get(token.getTokenID());

            if (token.hasPrimaryHead()) {
                CTArc arc = token.getPrimaryHead();
                head = graph.get(arc.getNode().getTokenID());
                node.setParent(head, arc.getLabel());
            } else
                node.setParent(graph.getRoot(), DDGTag.ROOT);

            for (CTArc arc : token.getSecondaryHeads()) {
                head = graph.get(arc.getNode().getTokenID());
                if (!node.isChildOf(head)) node.addSecondaryParent(head, arc.getLabel());
            }
        }

        return graph;
    }
}