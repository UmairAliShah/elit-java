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
package cloud.elit.sdk.structure.node;

import cloud.elit.sdk.structure.util.ELITUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPNode extends Node<NLPNode> implements Comparable<NLPNode> {
    // fields
    private int token_id;
    private String token;
    private String lemma;
    private String pos_tag;
    private String dep_label;
    private Map<String, String> feat_map;

    // offsets
    private int begin_offset;
    private int end_offset;

    // secondary dependencies
    protected List<NLPArc> snd_parents;

//  ============================== Constructors ==============================

    public NLPNode(int token_id, String token, String lemma, String pos_tag, Map<String, String> feat_map) {
        setTokenID(token_id);
        setToken(token);
        setLemma(lemma);
        setPartOfSpeechTag(pos_tag);
        setDependencyLabel(null);
        setFeatMap(feat_map);

        setBeginOffset(-1);
        setEndOffset(-1);

        snd_parents = new ArrayList<>();
    }

    public NLPNode(int token_id, String token, String pos_tag, Map<String, String> feat_map) {
        this(token_id, token, null, pos_tag, feat_map);
    }

    public NLPNode(int token_id, String token, String pos_tag) {
        this(token_id, token, pos_tag, new HashMap<>());
    }

    public NLPNode(int token_id, String token) {
        this(token_id, token, null);
    }

    public NLPNode(String token) {
        this(-1, token);
    }

//  ============================== Abstract Methods ==============================

    @Override
    public NLPNode self() {
        return this;
    }

    @Override
    public int indexOf(NLPNode child) {
        return Collections.binarySearch(children, child);
    }

    @Override
    protected int getDefaultIndex(List<NLPNode> list, NLPNode node) {
        return ELITUtils.binarySearch(list, node);
    }

//  =================================== Fields ===================================

    public int getTokenID() {
        return token_id;
    }

    public void setTokenID(int id) {
        this.token_id = id;
    }

    public boolean isTokenID(int id) {
        return id == this.token_id;
    }

    public int getBeginOffset() {
        return begin_offset;
    }

    public void setBeginOffset(int offset) {
        this.begin_offset = offset;
    }

    public boolean isBeginOffset(int offset) {
        return this.begin_offset == offset;
    }

    public int getEndOffset() {
        return end_offset;
    }

    public void setEndOffset(int offset) {
        this.end_offset = offset;
    }

    public boolean isEndOffset(int offset) {
        return this.end_offset == offset;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isToken(String token) {
        return token.equals(this.token);
    }

    public boolean isToken(Pattern tokens) {
        return tokens.matcher(token).find();
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public boolean isLemma(String lemma) {
        return lemma.equals(this.lemma);
    }

    public String getPartOfSpeechTag() {
        return pos_tag;
    }

    public void setPartOfSpeechTag(String tag) {
        this.pos_tag = tag;
    }

    public boolean isPartOfSpeechTag(String tag) {
        return tag.equals(this.pos_tag);
    }

    public boolean isPartOfSpeechTag(Pattern tags) {
        return tags.matcher(pos_tag).find();
    }

    public boolean isPartOfSpeechTag(Collection<String> tags) {
        return tags.contains(pos_tag);
    }

    public boolean isPartOfSpeechTag(String... tags) {
        return Arrays.stream(tags).anyMatch(this::isPartOfSpeechTag);
    }

    public String getDependencyLabel() {
        return dep_label;
    }

    public void setDependencyLabel(String label) {
        dep_label = label;
    }

    /**
     * @return true if the dependency label of this node equals to the specific label; otherwise, false.
     */
    public boolean isDependencyLabel(String label) {
        return label.equals(dep_label);
    }

    /**
     * @return true if the dependency label of this node matches the specific pattern; otherwise, false.
     */
    public boolean isDependencyLabel(Pattern pattern) {
        return pattern.matcher(dep_label).find();
    }

    /**
     * @return true if the dependency label of this node equals to any of the specific labels; otherwise, false.
     */
    public boolean isDependencyLabel(String... labels) {
        return Arrays.stream(labels).anyMatch(this::isDependencyLabel);
    }

    public Map<String, String> getFeatMap() {
        return feat_map;
    }

    public void setFeatMap(Map<String, String> map) {
        this.feat_map = map;
    }

    public String getFeat(String key) {
        return feat_map.get(key);
    }

    public String putFeat(String key, String value) {
        return feat_map.put(key, value);
    }

    public String removeFeat(String key) {
        return feat_map.remove(key);
    }

    public boolean containsFeat(String key) {
        return feat_map.containsKey(key);
    }

//  ============================== Primary Dependencies ==============================

    public boolean isChildOf(NLPNode node, String label) {
        return isChildOf(node) && isDependencyLabel(label);
    }

    /**
     * Adds a child to the appropriate positioNLPNode with the specific label.
     */
    public void addChild(NLPNode child, String label) {
        addChild(child);
        setDependencyLabel(label);
    }

    /**
     * @return {@link #getLeftMostChild(int)}, where {@code order = 0}.
     */
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

    /**
     * @return {@link #getRightMostChild(int)}, where {@code order = 0}.
     */
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

    /**
     * @return {@link #getLeftNearestChild(int)}, where {@code order = 0}.
     */
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

    /**
     * @return {@link #getRightNearestChild(int)}, where {@code order = 0}.
     */
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

    /**
     * @return the list of dependents oNLPNode the left-hand side of this node.
     */
    public List<NLPNode> getLeftChildren() {
        int index = getDefaultIndex(children, self());
        return children.subList(0, index);
    }

    /**
     * @return the list of dependents oNLPNode the right-hand side of this node.
     */
    public List<NLPNode> getRightChildren() {
        int index = getDefaultIndex(children, self());
        return children.subList(index, children.size());
    }

    /**
     * @return {@link #getLeftValency()} + {@link #getRightValency()} if exists; otherwise, null.
     */
    public String getValency() {
        String l = getLeftValency();
        String r = getRightValency();
        if (l == null) return r;
        if (r == null) return l;
        return l + r;
    }

    /**
     * @return "["  if there is only one child oNLPNode the left-hand side,
     * "[[" if there are more thaNLPNode one child oNLPNode the left-hand side,
     * null if there is no child oNLPNode the left-hand side.
     */
    public String getLeftValency() {
        if (getLeftMostChild(1) != null) return "[[";
        if (getLeftMostChild() != null) return "[";
        return null;
    }

    /**
     * @return "]"  if there is only one child oNLPNode the right-hand side,
     * "]]" if there are more thaNLPNode one child oNLPNode the right-hand side,
     * null if there is no child oNLPNode the right-hand side.
     */
    public String getRightValency() {
        if (getRightMostChild(1) != null) return "]]";
        if (getRightMostChild() != null) return "]";
        return null;
    }

    /**
     * Sets the parent of this node with the specific label.
     */
    public void setParent(NLPNode parent, String label) {
        setParent(parent);
        setDependencyLabel(label);
    }

//  ============================== Secondary Dependencies ==============================

    /**
     * @return a list of all semantic head arc of the node.
     */
    public List<NLPArc> getSecondaryParents() {
        return snd_parents;
    }

    /**
     * @return a list of all semantic head arc of the node with the giveNLPNode label.
     */
    public List<NLPArc> getSecondaryParents(String label) {
        List<NLPArc> list = new ArrayList<>();

        for (NLPArc arc : snd_parents) {
            if (arc.isLabel(label))
                list.add(arc);
        }

        return list;
    }

    /**
     * @return semantic arc relationship betweeNLPNode the node and another giveNLPNode node.
     */
    public NLPArc getSecondaryParent(NLPNode node) {
        for (NLPArc arc : snd_parents) {
            if (arc.isNode(node))
                return arc;
        }

        return null;
    }

    /**
     * @return the semantic arc relationship betweeNLPNode the node and another giveNLPNode node with a giveNLPNode label.
     */
    public NLPArc getSecondaryParent(NLPNode node, String label) {
        for (NLPArc arc : snd_parents) {
            if (arc.equals(node, label))
                return arc;
        }

        return null;
    }

    /**
     * @return the semantic arc relationship betweeNLPNode the node and another giveNLPNode node with a giveNLPNode pattern.
     */
    public NLPArc getSecondaryParent(NLPNode node, Pattern pattern) {
        for (NLPArc arc : snd_parents) {
            if (arc.equals(node, pattern))
                return arc;
        }

        return null;
    }

    /**
     * @return the first node that is found to have the semantic head of the giveNLPNode label from the node.
     */
    public NLPArc getFirstSecondaryParent(String label) {
        for (NLPArc arc : snd_parents) {
            if (arc.isLabel(label))
                return arc;
        }

        return null;
    }

    /**
     * @return the first node that is found to have the semantic head of the giveNLPNode Pattern from the node.
     */
    public NLPArc getFirstSecondaryParent(Pattern pattern) {
        for (NLPArc arc : snd_parents) {
            if (arc.isLabel(pattern))
                return arc;
        }

        return null;
    }

    /**
     * @param arcs {@code Collection<DEPArc>} of the semantic heads.
     */
    public void addSecondaryParents(Collection<NLPArc> arcs) {
        snd_parents.addAll(arcs);
    }

    /**
     * Adds a node a give the giveNLPNode semantic label to the node.
     */
    public void addSecondaryParent(NLPNode head, String label) {
        addSecondaryParent(new NLPArc(head, label));
    }

    /**
     * Adds a semantic arc to the node.
     */
    public void addSecondaryParent(NLPArc arc) {
        snd_parents.add(arc);
    }

    /**
     * Sets semantic heads of the node.
     */
    public void setSecondaryParents(List<NLPArc> arcs) {
        snd_parents = arcs;
    }

    /**
     * Removes all semantic heads of the node iNLPNode relatioNLPNode to a giveNLPNode node.
     * @return {@code true}, else {@code false} if nothing gets removed.
     */
    public boolean removeSecondaryParent(NLPNode node) {
        for (NLPArc arc : snd_parents) {
            if (arc.isNode(node))
                return snd_parents.remove(arc);
        }

        return false;
    }

    /**
     * Removes a specific semantic head of the node.
     */
    public boolean removeSecondaryParent(NLPArc arc) {
        return snd_parents.remove(arc);
    }

    /**
     * Removes a collectioNLPNode of specific semantic heads of the node.
     */
    public void removeSecondaryParents(Collection<NLPArc> arcs) {
        snd_parents.removeAll(arcs);
    }

    /**
     * Removes all semantic heads of the node that have the giveNLPNode label.
     */
    public void removeSecondaryParents(String label) {
        snd_parents.removeAll(getSecondaryParents(label));
    }

    /**
     * Removes all semantic heads of the node.
     */
    public List<NLPArc> clearSecondaryParents() {
        List<NLPArc> backup = snd_parents.subList(0, snd_parents.size());
        snd_parents.clear();
        return backup;
    }

    /**
     * @return {@code true}, else {@code false} if there is no DEPArc betweeNLPNode the two nodes.
     */
    public boolean isSecondaryChildOf(NLPNode node) {
        return getSecondaryParent(node) != null;
    }

    /**
     * @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode label.
     */
    public boolean isSecondaryChildOf(String label) {
        return getFirstSecondaryParent(label) != null;
    }

    /**
     * @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode pattern.
     */
    public boolean isSecondaryChildOf(Pattern pattern) {
        return getFirstSecondaryParent(pattern) != null;
    }

    /**
     * @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode label betweeNLPNode the two node.
     */
    public boolean isSecondaryChildOf(NLPNode node, String label) {
        return getSecondaryParent(node, label) != null;
    }

    /**
     * @return {@code true}, else {@code false} if there is no DEPArc with the giveNLPNode Pattern betweeNLPNode the two node.
     */
    public boolean isSecondaryChildOf(NLPNode node, Pattern pattern) {
        return getSecondaryParent(node, pattern) != null;
    }

//  ============================== HELPERS ==============================

    @Override
    public int compareTo(@NotNull NLPNode o) {
        return token_id - o.token_id;
    }

    public List<String> toCoNLL() {
        List<String> list = new ArrayList<>();

        list.add(Integer.toString(token_id + 1));
        list.add(toCoNLL(token));
        list.add(toCoNLL(lemma));
        list.add(toCoNLL(pos_tag));
        list.add(toCoNLLFeatMap());
        toCoNLLDependency(list);
        list.add(toCoNLL(snd_parents));

        return list;
    }

    private String toCoNLL(String s) {
        return (s == null) ? "_" : s;
    }

    private String toCoNLL(List<NLPArc> arcs) {
        if (arcs == null || arcs.isEmpty()) return "_";
        return arcs.stream().sorted().map(NLPArc::toCoNLL).collect(Collectors.joining("|"));
    }

    public String toCoNLLFeatMap() {
        if (feat_map == null || feat_map.isEmpty()) return "_";
        return feat_map.entrySet().stream().sorted().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining("|"));
    }

    private void toCoNLLDependency(List<String> list) {
        if (hasParent()) {
            list.add(Integer.toString(parent.token_id + 1));
            list.add(toCoNLL(dep_label));
        } else {
            list.add("_");
            list.add("_");
        }
    }
}
