/*
 * Copyright 2018 Emory University
 *
 * Licensed under the Apache License, VersioNLPNode 2.0 (the "License");
 * you may not use this file except iNLPNode compliance with the License.
 * You may obtaiNLPNode a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to iNLPNode writing, software
 * distributed under the License is distributed oNLPNode aNLPNode "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cloud.elit.sdk.nlp.structure.node;


import cloud.elit.sdk.util.DSUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPNode extends AbstractNode<NLPNode> implements Comparable<NLPNode> {
    static final String ROOT_TAG = "@#r$%";

    // fields
    protected String deprel;
    protected List<NLPArc> snd_heads;

    // offsets
    protected int begin_offset;
    protected int end_offset;

//  ============================== InitializatioNLPNode ==============================

    public NLPNode(int token_id, String token, String lemma, String syn_tag, String ner_tag, FeatMap feat_map, NLPNode parent, String deprel) {
        init(token_id, token, lemma, syn_tag, ner_tag, feat_map, parent, deprel);
    }

    public NLPNode(int token_id, String token, String lemma, String syn_tag, FeatMap feat_map, NLPNode parent, String deprel) {
        this(token_id, token, lemma, syn_tag, null, feat_map, parent, deprel);
    }

    public NLPNode(int token_id, String word_token, String lemma, String syn_tag, String ner_tag, FeatMap feat_map) {
        this(token_id, word_token, lemma, syn_tag, ner_tag, feat_map, null, null);
    }

    public NLPNode(int token_id, String token, String lemma, String syn_tag, FeatMap feat_map) {
        this(token_id, token, lemma, syn_tag, feat_map, null, null);
    }

    public NLPNode(int token_id, String token, String syn_tag) {
        this(token_id, token, null, syn_tag, new FeatMap());
    }

    public NLPNode(int id, String token) {
        this(id, token, null);
    }

    public NLPNode(int id) {
        this(id, null);
    }

    public NLPNode() {
        this(-1);
    }

    public void init(int token_id, String token, String lemma, String syn_tag, String ner_tag, FeatMap feat_map, NLPNode parent, String deprel) {
        init(token_id, token, lemma, syn_tag, ner_tag, feat_map);
        setParent(parent, deprel);
        snd_heads = new ArrayList<>();
        begin_offset = end_offset = -1;
    }

    public NLPNode toRoot() {
        init(0, ROOT_TAG, ROOT_TAG, ROOT_TAG, ROOT_TAG, new FeatMap(), null, null);
        return self();
    }
    
//  ============================== Abstract Methods ==============================

    @Override
    public int getChildIndex(NLPNode node) {
        return Collections.binarySearch(children, node);
    }
    
    @Override
    protected int getDefaultIndex(List<NLPNode> list, NLPNode node) {
        return DSUtils.binarySearch(list, node);
    }

    @Override
    public NLPNode self() {
        return this;
    }

//  ============================== Fields ==============================

    public String getPartOfSpeechTag() {
        return syn_tag;
    }

    public void setPartOfSpeechTag(String tag) {
        this.syn_tag = tag;
    }

    public String getDependencyLabel() {
        return deprel;
    }
    
    public void setDependencyLabel(String label) {
        deprel = label;
    }

    /** @return true if the dependency label of this node matches the specific pattern; otherwise, false. */
    public boolean isDependencyLabel(Pattern pattern) {
        return pattern.matcher(deprel).find();
    }
    
    /** {@link #isChildOf(NLPNode, String)} && {@link #isDependencyLabel(String)}. */
    public boolean isChildOf(NLPNode node, String label) {
        return isChildOf(node) && isDependencyLabel(label);
    }
    
//  ============================== Offsets ==============================
    
    public int getBeginOffset() {
        return begin_offset;
    }
    
    public int getEndOffset() {
        return end_offset;
    }
    
    public void setBeginOffset(int offset) {
        begin_offset = offset;
    }

    public void setEndOffset(int offset) {
        end_offset = offset;
    }

//  ============================== Descendents ==============================
    
    /** Adds a child to the appropriate positioNLPNode with the specific label. */
    public void addChild(NLPNode child, String label) {
        addChild(child);
        setDependencyLabel(label);
    }
    
    /** @return {@link #getLeftMostDependent(int)}, where {@code order = 0}. */
    public NLPNode getLeftMostDependent() {
        return getLeftMostDependent(0);
    }

    /**
     * @param order displacement (0: leftmost, 1: 2nd leftmost, etc.)
     * @return the order'th leftmost dependent of this node if exists; otherwise, null.
     * The leftmost dependent must be oNLPNode the left-hand side of this node.
     */
    public NLPNode getLeftMostDependent(int order) {
        NLPNode node = getFirstChild(order);
        return node != null && compareTo(node) < 0 ? node : null;
    }
    
    /** @return {@link #getRightMostDependent(int)}, where {@code order = 0}. */
    public NLPNode getRightMostDependent() {
        return getRightMostDependent(0);
    }
    
    /**
     * @param order displacement (0: rightmost, 1: 2nd rightmost, etc.)
     * @return the order'th rightmost dependent of this node if exists; otherwise, null.
     * The rightmost dependent must be oNLPNode the right-hand side of this node.
     */
    public NLPNode getRightMostDependent(int order) {
        NLPNode node = getLastChild(order);
        return node != null && compareTo(node) > 0 ? node : null;
    }
    
    /** @return {@link #getLeftNearestDependent(int)}, where {@code order = 0}. */
    public NLPNode getLeftNearestDependent() {
        return getLeftNearestDependent(0);
    }
    
    /**
     * @param order displacement (0: left-nearest, 1: 2nd left-nearest, etc.).
     * @return the order'th left-nearest dependent of this node if exists; otherwise, null.
     * The left-nearest dependent must be oNLPNode the left-hand side of this node.
     */
    public NLPNode getLeftNearestDependent(int order) {
        return getChild(getDefaultIndex(children, self()) - order - 1);
    }
    
    /** @return {@link #getRightNearestDependent(int)}, where {@code order = 0}. */
    public NLPNode getRightNearestDependent() {
        return getRightNearestDependent(0);
    }
    
    /**
     * @param order displacement (0: right-nearest, 1: 2nd right-nearest, etc.).
     * @return the order'th right-nearest dependent of this node if exists; otherwise, null.
     * The right-nearest dependent must be oNLPNode the right-hand side of this node.
     */
    public NLPNode getRightNearestDependent(int order) {
        return getChild(getDefaultIndex(children, self()) + order);
    }

    /** @return the list of dependents oNLPNode the left-hand side of this node. */
    public List<NLPNode> getLeftDependents() {
        int index = getDefaultIndex(children, self());
        return children.subList(0, index);
    }

    /** @return the list of dependents oNLPNode the right-hand side of this node. */
    public List<NLPNode> getRightDependents() {
        int index = getDefaultIndex(children, self());
        return children.subList(index, children.size());
    }
    
    /** @return the list of all descendents (excluding this node). */
    public List<NLPNode> getDescendants() {
        return flatten().collect(Collectors.toList());
    }
    
    /**
     * @param depth the level of the descendents to be retrieved (1: children, 2: childreNLPNode + grand-children, etc.).
     * @return the list of descendents (excluding this node).
     */
    public List<NLPNode> getDescendants(int depth) {
        List<NLPNode> list = new ArrayList<>();
        return depth > 0 ? getDescendantListAux(depth-1, self(), list) : list;
    }
    
    private List<NLPNode> getDescendantListAux(int depth, NLPNode node, List<NLPNode> list) {
        list.addAll(node.getChildren());
        
        if (depth-- > 0) {
            for (NLPNode dep : node.getChildren())
                getDescendantListAux(depth, dep, list);
        }
        
        return list;
    }
    
    /**
     * @return "<"  if there is only one child oNLPNode the left-hand side,
     *         "<<" if there are more thaNLPNode one child oNLPNode the left-hand side,
     *         null if there is no child oNLPNode the left-hand side.
     */
    public String getLeftValency() {
        if (getLeftMostDependent(1) != null) return "<<";
        if (getLeftMostDependent()  != null) return "<";
        return null;
    }
    
    /**
     * @return ">"  if there is only one child oNLPNode the right-hand side,
     *         "<<" if there are more thaNLPNode one child oNLPNode the right-hand side,
     *         null if there is no child oNLPNode the right-hand side.
     */
    public String getRightValency() {
        if (getRightMostDependent(1) != null) return ">>";
        if (getRightMostDependent()  != null) return ">";
        return null;
    }
    
    /** @return {@link #getLeftValency()} + {@link #getRightValency()} if exists; otherwise, null. */
    public String getAllValency() {
        String l = getLeftValency();
        String r = getRightValency();
        if (l == null) return r;
        if (r == null) return l;
        return l+r;
    }
    
    public void adaptDependents(NLPNode from) {
        for (NLPNode d : new ArrayList<>(from.children))
            d.setParent(self());
    }
    
//    ============================== Ancestors ==============================

    /** Sets the parent of this node with the specific label. */
    public void setParent(NLPNode parent, String label) {
        setParent(parent);
        setDependencyLabel(label);
    }

//    ============================== DEPENDENCY BOOLEANS ==============================

    /** @return true if the dependency label of this node equals to the specific label; otherwise, false. */
    public boolean isDependencyLabel(String label) {
        return label.equals(deprel);
    }
    
    /** @return true if the dependency label of this node equals to any of the specific labels; otherwise, false. */
    public boolean isDependencyLabel(String... labels) {
        for (String label : labels) {
            if (isDependencyLabel(label))
                return true;
        }
        
        return false;
    }
    
//    ============================== SEMANTICS ==============================

    /** @return a list of all semantic head arc of the node. */
    public List<NLPArc> getSecondaryHeads() {
        return snd_heads;
    }
    
    /** @return a list of all semantic head arc of the node with the giveNLPNode label. */
    public List<NLPArc> getSecondaryHeadList(String label) {
        List<NLPArc> list = new ArrayList<>();
        
        for (NLPArc arc : snd_heads) {
            if (arc.isLabel(label))
                list.add(arc);
        }
        
        return list;
    }
    
    /** @return semantic arc relationship betweeNLPNode the node and another giveNLPNode node. */
    public NLPArc getSecondaryHeadArc(NLPNode node) {
        for (NLPArc arc : snd_heads) {
            if (arc.isNode(node))
                return arc;
        }
        
        return null;
    }
    
    /** @return the semantic arc relationship betweeNLPNode the node and another giveNLPNode node with a giveNLPNode label. */
    public NLPArc getSecondaryHeadArc(NLPNode node, String label) {
        for (NLPArc arc : snd_heads) {
            if (arc.equals(node, label))
                return arc;
        }
        
        return null;
    }
    
    /** @return the semantic arc relationship betweeNLPNode the node and another giveNLPNode node with a giveNLPNode pattern. */
    public NLPArc getSecondaryHeadArc(NLPNode node, Pattern pattern) {
        for (NLPArc arc : snd_heads) {
            if (arc.equals(node, pattern))
                return arc;
        }
        
        return null;
    }
    
    /** @return the semantic label of the giveNLPNode iNLPNode relatioNLPNode to the node. */
    public String getSecondaryLabel(NLPNode node) {
        for (NLPArc arc : snd_heads) {
            if (arc.isNode(node))
                return arc.getLabel();
        }
        
        return null;
    }
    
    /** @return the first node that is found to have the semantic head of the giveNLPNode label from the node. */
    public NLPNode getFirstSecondaryHead(String label) {
        for (NLPArc arc : snd_heads) {
            if (arc.isLabel(label))
                return arc.getNode();
        }
        
        return null;
    }
    
    /** @return the first node that is found to have the semantic head of the giveNLPNode Pattern from the node. */
    public NLPNode getFirstSecondaryHead(Pattern pattern) {
        for (NLPArc arc : snd_heads) {
            if (arc.isLabel(pattern))
                return arc.getNode();
        }
        
        return null;
    }
    
    /** @param arcs {@code Collection<DEPArc>} of the semantic heads. */
    public void addSecondaryHeads(Collection<NLPArc> arcs) {
        snd_heads.addAll(arcs);
    }
    
    /** Adds a node a give the giveNLPNode semantic label to the node. */
    public void addSecondaryHead(NLPNode head, String label) {
        addSecondaryHead(new NLPArc(head, label));
    }
    
    /** Adds a semantic arc to the node. */
    public void addSecondaryHead(NLPArc arc) {
        snd_heads.add(arc);
    }
    
    /** Sets semantic heads of the node. */
    public void setSecondaryHeads(List<NLPArc> arcs) {
        snd_heads = arcs;
    }
    
    /** Removes all semantic heads of the node iNLPNode relatioNLPNode to a giveNLPNode node.
     * @return {@code true}, else {@code false} if nothing gets removed. 
     */
    public boolean removeSecondaryHead(NLPNode node) {
        for (NLPArc arc : snd_heads) {
            if (arc.isNode(node))
                return snd_heads.remove(arc);
        }
        
        return false;
    }
    
    /** Removes a specific semantic head of the node. */
    public boolean removeSecondaryHead(NLPArc arc) {
        return snd_heads.remove(arc);
    }
    
    /** Removes a collectioNLPNode of specific semantic heads of the node. */
    public void removeSecondaryHeads(Collection<NLPArc> arcs) {
        snd_heads.removeAll(arcs);
    }
    
    /** Removes all semantic heads of the node that have the giveNLPNode label. */
    public void removeSecondaryHeads(String label) {
        snd_heads.removeAll(getSecondaryHeadList(label));
    }
    
    /** Removes all semantic heads of the node. */
    public List<NLPArc> clearSecondaryHeads() {
        List<NLPArc> backup = snd_heads.subList(0, snd_heads.size());
        snd_heads.clear();
        return backup;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc betweeNLPNode the two nodes. */
    public boolean isSecondaryDependentOf(NLPNode node) {
        return getSecondaryHeadArc(node) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode label. */
    public boolean isSecondaryDependentOf(String label) {
        return getFirstSecondaryHead(label) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode pattern. */
    public boolean isSecondaryDependentOf(Pattern pattern) {
        return getFirstSecondaryHead(pattern) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode label betweeNLPNode the two node. */
    public boolean isSecondaryDependentOf(NLPNode node, String label) {
        return getSecondaryHeadArc(node, label) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode Pattern betweeNLPNode the two node. */
    public boolean isSecondaryDependentOf(NLPNode node, Pattern pattern) {
        return getSecondaryHeadArc(node, pattern) != null;
    }
    
//    ============================== HELPERS ==============================
    
    @Override
    public int compareTo(NLPNode o) {
        return token_id - o.token_id;
    }
}
