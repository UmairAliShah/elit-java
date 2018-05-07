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
package cloud.elit.sdk.structure;

import cloud.elit.sdk.structure.node.NLPNode;
import cloud.elit.sdk.structure.util.Fields;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Document implements Serializable, Iterable<Sentence> {
    private List<Sentence> sentences;

//  =================================== Constructors ===================================

    public Document() {
        sentences = new ArrayList<>();
    }

    public Document(String json) {
        this();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode top = mapper.readTree(json);
            JsonNode output = top.findValue("output");

            for (JsonNode o : output) {
                // required fields
                int sen_id = o.findValue(Fields.SID).asInt();
                List<NLPNode> nodes = toNodeList(o.findValue(Fields.TOK));
                Sentence sentence = new Sentence(sen_id, nodes);
                add(sentence);

                // optional fields
                setOffsets(nodes, o.findValue(Fields.OFF));
                setStringList(nodes, o.findValue(Fields.LEM), NLPNode::setLemma);
                setStringList(nodes, o.findValue(Fields.POS), NLPNode::setPartOfSpeechTag);
                sentence.setNamedEntities(toEntityList(nodes, o.findValue(Fields.NER)));
                setPrimaryDependencies(sentence, o.findValue(Fields.DEP));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<NLPNode> toNodeList(JsonNode json) {
        List<NLPNode> list = new ArrayList<>();
        int i = 0;

        for (JsonNode token : json) {
            list.add(new NLPNode(i++, token.asText()));
        }

        return list;
    }

    private void setOffsets(List<NLPNode> nodes, JsonNode json) {
        if (json == null) return;
        int i = 0;

        for (JsonNode node : json) {
            Iterator<JsonNode> it = node.iterator();
            nodes.get(i).setBeginOffset(it.next().asInt());
            nodes.get(i++).setEndOffset(it.next().asInt());
        }
    }

    private void setStringList(List<NLPNode> nodes, JsonNode json, BiConsumer<NLPNode, String> c) {
        if (json == null) return;
        int i = 0;

        for (JsonNode node : json) {
            c.accept(nodes.get(i++), node.asText());
        }
    }

    private List<Chunk> toEntityList(List<NLPNode> nodes, JsonNode json) {
        if (json == null) return null;
        List<Chunk> chunks = new ArrayList<>();

        for (JsonNode node : json) {
            Iterator<JsonNode> it = node.iterator();
            int begin = it.next().asInt();
            int end = it.next().asInt();
            String label = it.next().asText();

            Chunk chunk = new Chunk();
            chunk.setNodes(IntStream.range(begin, end).mapToObj(nodes::get).collect(Collectors.toList()));
            chunk.setLabel(label);
            chunks.add(chunk);
        }

        return chunks;
    }

    private void setPrimaryDependencies(Sentence sentence, JsonNode json) {
        if (json == null) return;
        List<NLPNode> nodes = sentence.getNodes();
        int i = 0;

        for (JsonNode node : json) {
            Iterator<JsonNode> it = node.iterator();
            int head_id = it.next().asInt();
            NLPNode head = (head_id < 0) ? sentence.getRoot() : nodes.get(head_id);
            String label = it.next().asText();
            nodes.get(i++).setParent(head, label);
        }
    }

//  =================================== Getters and Setters ===================================

    public List<Sentence> get() {
        return sentences;
    }

    public void set(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public int size() {
        return sentences.size();
    }

    public Sentence get(int index) {
        return sentences.get(index);
    }

    public boolean add(Sentence sentence) {
        return sentences.add(sentence);
    }

    public void add(int index, Sentence sentence) {
        sentences.add(index, sentence);
    }

    public Sentence set(int index, Sentence sentence) {
        return sentences.set(index, sentence);
    }

    public Sentence remove(int index) {
        return sentences.remove(index);
    }

    public boolean remove(Sentence sentence) {
        return sentences.remove(sentence);
    }

//  =================================== Iterator ===================================

    @NotNull
    @Override
    public Iterator<Sentence> iterator() {
        return sentences.iterator();
    }

//  =================================== String ===================================

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        for (Sentence s : sentences) joiner.add(s.toString());
        return "[" + joiner.toString() + "]";
    }

    public String toTSV() {
        StringJoiner joiner = new StringJoiner("\n\n");
        for (Sentence s : sentences) joiner.add(s.toTSV());
        return joiner.toString();
    }
}
