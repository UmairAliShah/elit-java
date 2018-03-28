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

import cloud.elit.sdk.nlp.util.TaskType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {
    private Map<String, Object> field_map;

    //  =================================== Constructors ===================================

    public Sentence() {
        field_map = new HashMap<>();
    }

    public Sentence(Sentence sentence) {
        field_map = new HashMap<>(sentence.field_map);
    }

    public Sentence(List<String> tokens) {
        this();
        setTokens(tokens);
    }

    //  =================================== Getters and setters ===================================

    public <V>V getFields(String field) {
        return (V)field_map.get(field);
    }

    public <V>void setFields(String field, V values) {
        field_map.put(field, values);
    }

    public List<String> getTokens() {
        return getFields(TaskType.TOK);
    }

    public void setTokens(List<String> tokens) {
        setFields(TaskType.TOK, tokens);
    }

    public List<Offset> getOffsets() {
        return getFields(TaskType.OFF);
    }

    public void setOffsets(List<Offset> offsets) {
        setFields(TaskType.OFF, offsets);
    }

    public List<String> getLemmas() {
        return getFields(TaskType.LEM);
    }

    public void setLemmas(List<String> lemmas) {
        setFields(TaskType.LEM, lemmas);
    }

    public List<String> getPartOfSpeechTags() {
        return getFields(TaskType.POS);
    }

    public void setPartOfSpeechTags(List<String> tags) {
        setFields(TaskType.POS, tags);
    }

    public List<Chunk> getNamedEntityTags() {
        return getFields(TaskType.NER);
    }

    public void setNamedEntityTags(List<Chunk> tags) {
        setFields(TaskType.NER, tags);
    }

    public List<Arc> getDependencyRelations() {
        return getFields(TaskType.DEP);
    }

    public void setDependencyRelations(List<Arc> arcs) {
        setFields(TaskType.DEP, arcs);
    }
}

