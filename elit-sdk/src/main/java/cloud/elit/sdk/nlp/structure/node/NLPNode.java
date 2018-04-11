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

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPNode extends AbstractNode<NLPNode> implements Comparable<NLPNode> {

    // fields
    protected String dep_label;
    protected List<NLPArc> snd_parents;

    // offsets
    protected int begin_offset;
    protected int end_offset;

//  ============================== Constructors ==============================

    public NLPNode(int token_id, String token, String lemma, String pos_tag, String ner_tag, Map<String, String> feat_map, NLPNode parent, String dep_label) {
        super(token_id, token, lemma, pos_tag, ner_tag, feat_map);
        setParent(parent, dep_label);
        snd_parents = new ArrayList<>();
        begin_offset = end_offset = -1;
    }

    public NLPNode(int token_id, String token, String lemma, String pos_tag, Map<String, String> feat_map, NLPNode parent, String dep_label) {
        this(token_id, token, lemma, pos_tag, null, feat_map, parent, dep_label);
    }

    public NLPNode(int token_id, String word_token, String lemma, String pos_tag, String ner_tag, Map<String, String> feat_map) {
        this(token_id, word_token, lemma, pos_tag, ner_tag, feat_map, null, null);
    }

    public NLPNode(int token_id, String token, String lemma, String pos_tag, Map<String, String> feat_map) {
        this(token_id, token, lemma, pos_tag, feat_map, null, null);
    }

    public NLPNode(int token_id, String token, String pos_tag) {
        this(token_id, token, null, pos_tag, new HashMap<String, String>());
    }

    public NLPNode(int id, String token) {
        this(id, token, null);
    }

    public NLPNode(int id) {
        this(id, null);
    }

    public NLPNode() {
        this(-2);
    }

//  ============================== Abstract Methods ==============================

    @Override
    public NLPNode self() {
        return this;
    }

    @Override
    public int getChildIndex(NLPNode node) {
        return Collections.binarySearch(children, node);
    }
    
    @Override
    protected int getDefaultIndex(List<NLPNode> list, NLPNode node) {
        return DSUtils.binarySearch(list, node);
    }

//  ============================== Fields ==============================

    public String getPartOfSpeechTag() {
        return syn_tag;
    }

    public void setPartOfSpeechTag(String tag) {
        this.syn_tag = tag;
    }

    public String getDependencyLabel() {
        return dep_label;
    }
    
    public void setDependencyLabel(String label) {
        dep_label = label;
    }

    /** @return true if the dependency label of this node equals to the specific label; otherwise, false. */
    public boolean isDependencyLabel(String label) {
        return label.equals(dep_label);
    }

    /** @return true if the dependency label of this node matches the specific pattern; otherwise, false. */
    public boolean isDependencyLabel(Pattern pattern) {
        return pattern.matcher(dep_label).find();
    }

    /** @return true if the dependency label of this node equals to any of the specific labels; otherwise, false. */
    public boolean isDependencyLabel(String... labels) {
        for (String label : labels) {
            if (isDependencyLabel(label))
                return true;
        }

        return false;
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

//  ============================== Primary Dependencies ==============================

    /** {@link #isChildOf(NLPNode, String)} && {@link #isDependencyLabel(String)}. */
    public boolean isChildOf(NLPNode node, String label) {
        return isChildOf(node) && isDependencyLabel(label);
    }

    /** Adds a child to the appropriate positioNLPNode with the specific label. */
    public void addChild(NLPNode child, String label) {
        addChild(child);
        setDependencyLabel(label);
    }
    
    /** @return {@link #getLeftMostChild(int)}, where {@code order = 0}. */
    public NLPNode getLeftMostChild() {
        return getLeftMostChild(0);
    }

    /**
     * @param order displacement (0: leftmost, 1: 2nd leftmost, etc.)
     * @return the order'th leftmost dependent of this node if exists; otherwise, null.
     * The leftmost dependent must be oNLPNode the left-hand side of this node.
     */
    public NLPNode getLeftMostChild(int order) {
        NLPNode node = getFirstChild(order);
        return node != null && compareTo(node) < 0 ? node : null;
    }
    
    /** @return {@link #getRightMostChild(int)}, where {@code order = 0}. */
    public NLPNode getRightMostChild() {
        return getRightMostChild(0);
    }
    
    /**
     * @param order displacement (0: rightmost, 1: 2nd rightmost, etc.)
     * @return the order'th rightmost dependent of this node if exists; otherwise, null.
     * The rightmost dependent must be oNLPNode the right-hand side of this node.
     */
    public NLPNode getRightMostChild(int order) {
        NLPNode node = getLastChild(order);
        return node != null && compareTo(node) > 0 ? node : null;
    }
    
    /** @return {@link #getLeftNearestChild(int)}, where {@code order = 0}. */
    public NLPNode getLeftNearestChild() {
        return getLeftNearestChild(0);
    }
    
    /**
     * @param order displacement (0: left-nearest, 1: 2nd left-nearest, etc.).
     * @return the order'th left-nearest dependent of this node if exists; otherwise, null.
     * The left-nearest dependent must be oNLPNode the left-hand side of this node.
     */
    public NLPNode getLeftNearestChild(int order) {
        return getChild(getDefaultIndex(children, self()) - order - 1);
    }
    
    /** @return {@link #getRightNearestChild(int)}, where {@code order = 0}. */
    public NLPNode getRightNearestChild() {
        return getRightNearestChild(0);
    }
    
    /**
     * @param order displacement (0: right-nearest, 1: 2nd right-nearest, etc.).
     * @return the order'th right-nearest dependent of this node if exists; otherwise, null.
     * The right-nearest dependent must be oNLPNode the right-hand side of this node.
     */
    public NLPNode getRightNearestChild(int order) {
        return getChild(getDefaultIndex(children, self()) + order);
    }

    /** @return the list of dependents oNLPNode the left-hand side of this node. */
    public List<NLPNode> getLeftChildren() {
        int index = getDefaultIndex(children, self());
        return children.subList(0, index);
    }

    /** @return the list of dependents oNLPNode the right-hand side of this node. */
    public List<NLPNode> getRightChildren() {
        int index = getDefaultIndex(children, self());
        return children.subList(index, children.size());
    }

    /** @return {@link #getLeftValency()} + {@link #getRightValency()} if exists; otherwise, null. */
    public String getValency() {
        String l = getLeftValency();
        String r = getRightValency();
        if (l == null) return r;
        if (r == null) return l;
        return l+r;
    }
    
    /**
     * @return "<"  if there is only one child oNLPNode the left-hand side,
     *         "<<" if there are more thaNLPNode one child oNLPNode the left-hand side,
     *         null if there is no child oNLPNode the left-hand side.
     */
    public String getLeftValency() {
        if (getLeftMostChild(1) != null) return "<<";
        if (getLeftMostChild()  != null) return "<";
        return null;
    }
    
    /**
     * @return ">"  if there is only one child oNLPNode the right-hand side,
     *         "<<" if there are more thaNLPNode one child oNLPNode the right-hand side,
     *         null if there is no child oNLPNode the right-hand side.
     */
    public String getRightValency() {
        if (getRightMostChild(1) != null) return ">>";
        if (getRightMostChild()  != null) return ">";
        return null;
    }

    /** Sets the parent of this node with the specific label. */
    public void setParent(NLPNode parent, String label) {
        setParent(parent);
        setDependencyLabel(label);
    }

//  ============================== Secondary Dependencies ==============================

    /** @return a list of all semantic head arc of the node. */
    public List<NLPArc> getSecondaryArcs() {
        return snd_parents;
    }
    
    /** @return a list of all semantic head arc of the node with the giveNLPNode label. */
    public List<NLPArc> getSecondaryArcs(String label) {
        List<NLPArc> list = new ArrayList<>();
        
        for (NLPArc arc : snd_parents) {
            if (arc.isLabel(label))
                list.add(arc);
        }
        
        return list;
    }
    
    /** @return semantic arc relationship betweeNLPNode the node and another giveNLPNode node. */
    public NLPArc getSecondaryArc(NLPNode node) {
        for (NLPArc arc : snd_parents) {
            if (arc.isNode(node))
                return arc;
        }
        
        return null;
    }
    
    /** @return the semantic arc relationship betweeNLPNode the node and another giveNLPNode node with a giveNLPNode label. */
    public NLPArc getSecondaryArc(NLPNode node, String label) {
        for (NLPArc arc : snd_parents) {
            if (arc.equals(node, label))
                return arc;
        }
        
        return null;
    }
    
    /** @return the semantic arc relationship betweeNLPNode the node and another giveNLPNode node with a giveNLPNode pattern. */
    public NLPArc getSecondaryArc(NLPNode node, Pattern pattern) {
        for (NLPArc arc : snd_parents) {
            if (arc.equals(node, pattern))
                return arc;
        }
        
        return null;
    }
    
    /** @return the semantic label of the giveNLPNode iNLPNode relatioNLPNode to the node. */
    public String getSecondaryLabel(NLPNode node) {
        for (NLPArc arc : snd_parents) {
            if (arc.isNode(node))
                return arc.getLabel();
        }
        
        return null;
    }
    
    /** @return the first node that is found to have the semantic head of the giveNLPNode label from the node. */
    public NLPNode getFirstSecondaryParent(String label) {
        for (NLPArc arc : snd_parents) {
            if (arc.isLabel(label))
                return arc.getNode();
        }
        
        return null;
    }
    
    /** @return the first node that is found to have the semantic head of the giveNLPNode Pattern from the node. */
    public NLPNode getFirstSecondaryParent(Pattern pattern) {
        for (NLPArc arc : snd_parents) {
            if (arc.isLabel(pattern))
                return arc.getNode();
        }
        
        return null;
    }
    
    /** @param arcs {@code Collection<DEPArc>} of the semantic heads. */
    public void addSecondaryArcs(Collection<NLPArc> arcs) {
        snd_parents.addAll(arcs);
    }
    
    /** Adds a node a give the giveNLPNode semantic label to the node. */
    public void addSecondaryArc(NLPNode head, String label) {
        addSecondaryArc(new NLPArc(head, label));
    }
    
    /** Adds a semantic arc to the node. */
    public void addSecondaryArc(NLPArc arc) {
        snd_parents.add(arc);
    }
    
    /** Sets semantic heads of the node. */
    public void setSecondaryArcs(List<NLPArc> arcs) {
        snd_parents = arcs;
    }
    
    /** Removes all semantic heads of the node iNLPNode relatioNLPNode to a giveNLPNode node.
     * @return {@code true}, else {@code false} if nothing gets removed. 
     */
    public boolean removeSecondaryArc(NLPNode node) {
        for (NLPArc arc : snd_parents) {
            if (arc.isNode(node))
                return snd_parents.remove(arc);
        }
        
        return false;
    }
    
    /** Removes a specific semantic head of the node. */
    public boolean removeSecondaryArc(NLPArc arc) {
        return snd_parents.remove(arc);
    }
    
    /** Removes a collectioNLPNode of specific semantic heads of the node. */
    public void removeSecondaryArcs(Collection<NLPArc> arcs) {
        snd_parents.removeAll(arcs);
    }
    
    /** Removes all semantic heads of the node that have the giveNLPNode label. */
    public void removeSecondaryArcs(String label) {
        snd_parents.removeAll(getSecondaryArcs(label));
    }
    
    /** Removes all semantic heads of the node. */
    public List<NLPArc> clearSecondaryArcs() {
        List<NLPArc> backup = snd_parents.subList(0, snd_parents.size());
        snd_parents.clear();
        return backup;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc betweeNLPNode the two nodes. */
    public boolean isSecondaryChildOf(NLPNode node) {
        return getSecondaryArc(node) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode label. */
    public boolean isSecondaryChildOf(String label) {
        return getFirstSecondaryParent(label) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode pattern. */
    public boolean isSecondaryChildOf(Pattern pattern) {
        return getFirstSecondaryParent(pattern) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode label betweeNLPNode the two node. */
    public boolean isSecondaryChildOf(NLPNode node, String label) {
        return getSecondaryArc(node, label) != null;
    }
    
    /** @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode Pattern betweeNLPNode the two node. */
    public boolean isSecondaryChildOf(NLPNode node, Pattern pattern) {
        return getSecondaryArc(node, pattern) != null;
    }
    
//  ============================== HELPERS ==============================
    
    @Override
    public int compareTo(NLPNode o) {
        return token_id - o.token_id;
    }
}
