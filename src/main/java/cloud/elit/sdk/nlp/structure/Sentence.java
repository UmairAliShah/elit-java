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

import cloud.elit.sdk.nlp.util.FieldType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {
    private Map<String, Object> field_map;

    public Sentence() {
        field_map = new HashMap<>();
    }

    public Sentence(List<String> tokens) {
        this();
        setTokens(tokens);
    }

//  =================================== Getters ===================================

    public <V>V getFields(String field) {
        return (V)field_map.get(field);
    }

    public List<String> getTokens() {
        return getFields(FieldType.TOKEN);
    }

    public List<Offset> getOffsets() {
        return getFields(FieldType.OFFSET);
    }

    public List<String> getLemmas() {
        return getFields(FieldType.LEMMA);
    }

    public List<String> getPartOfSpeechTags() {
        return getFields(FieldType.POS);
    }

    public List<String> getNamedEntityTags() {
        return getFields(FieldType.NER);
    }

    public List<Arc> getDependencyRelations() {
        return getFields(FieldType.DEP);
    }

//  =================================== Setters ===================================

    public <V>void setFields(String field, V values) {
        field_map.put(field, values);
    }

    public void setTokens(List<String> tokens) {
        setFields(FieldType.TOKEN, tokens);
    }

    public void setOffsets(List<Offset> offsets) {
        setFields(FieldType.OFFSET, offsets);
    }

    public void setLemmas(List<String> lemmas) {
        setFields(FieldType.LEMMA, lemmas);
    }

    public void setPartOfSpeechTags(List<String> tags) {
        setFields(FieldType.POS, tags);
    }

    public void setNamedEntityTags(List<String> tags) {
        setFields(FieldType.NER, tags);
    }

    public void setDependencyRelations(List<Arc> arcs) {
        setFields(FieldType.NER, arcs);
    }
}

