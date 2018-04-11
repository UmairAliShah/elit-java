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
package cloud.elit.sdk.nlp.structure;

import cloud.elit.sdk.nlp.structure.node.NLPNode;
import cloud.elit.sdk.nlp.structure.node.NLPNodeList;
import cloud.elit.sdk.nlp.structure.node.NLPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Sentence extends NLPNodeList {
    private int sen_id;
    private NLPNode root;
    private List<Chunk> ner_list;

//  =================================== Constructors ===================================

    public Sentence(int sen_id, List<NLPNode> nodes) {
        super(nodes);
        setID(sen_id);
        root = NLPUtils.createRoot();
    }

    public Sentence(List<NLPNode> nodes) {
        this(-1, nodes);
    }

    public Sentence() {
        this(new ArrayList<>());
    }

//  =================================== Getters and Setters ===================================

    public int getID() {
        return sen_id;
    }

    public void setID(int id) {
        this.sen_id = id;
    }

    public NLPNode getRoot() {
        return root;
    }

    public void setRoot(NLPNode node) {
        this.root = node;
    }

    public List<String> getTokens() {
        return nodes.stream().map(NLPNode::getToken).collect(Collectors.toList());
    }

    public Chunk getNamedEntity(int index) {
        return ner_list.get(index);
    }

    public List<Chunk> getNamedEntities() {
        return ner_list;
    }

    public void setNamedEntities(List<Chunk> entities) {
        this.ner_list = entities;
    }

    public int numNamedEntities() {
        return ner_list.size();
    }

//  =================================== String ===================================

    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n");
        NLPNode node = nodes.get(0);

        joiner.add(String.format("  \"%s\": %d", JSONField.SID, sen_id));
        joiner.add(String.format("  \"%s\": %s", JSONField.TOK, fromStringList(NLPNode::getToken)));
        if (node.getEndOffset() > 0) joiner.add(String.format("  \"%s\": %s", JSONField.OFF, fromOffsets()));
        if (node.getLemma() != null) joiner.add(String.format("  \"%s\": %s", JSONField.LEM, fromStringList(NLPNode::getLemma)));
        if (node.getPartOfSpeechTag() != null) joiner.add(String.format("  \"%s\": %s", JSONField.POS, fromStringList(NLPNode::getPartOfSpeechTag)));
        if (ner_list != null) joiner.add(String.format("  \"%s\": %s", JSONField.NER, ner_list.toString()));
        if (node.getParent() != null) joiner.add(String.format("  \"%s\": %s", JSONField.DEP, fromPrimaryDependencies()));

        return "{\n" + joiner.toString() + "\n}";
    }

    private String fromStringList(Function<NLPNode, String> f) {
        StringJoiner joiner = new StringJoiner(", ");
        for (NLPNode node : nodes)
            joiner.add(String.format("\"%s\"", f.apply(node)));
        return "["+joiner.toString()+"]";
    }

    private String fromOffsets() {
        StringJoiner joiner = new StringJoiner(", ");
        for (NLPNode node : nodes)
            joiner.add(String.format("[%d, %d]", node.getBeginOffset(), node.getEndOffset()));
        return "["+joiner.toString()+"]";
    }

    private String fromPrimaryDependencies() {
        StringJoiner joiner = new StringJoiner(", ");
        for (NLPNode node : nodes)
            joiner.add(String.format("[%d, \"%s\"]", node.getParent().getTokenID(), node.getDependencyLabel()));
        return "["+joiner.toString()+"]";
    }
}

