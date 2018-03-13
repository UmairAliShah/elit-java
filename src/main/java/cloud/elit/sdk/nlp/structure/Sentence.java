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
import cloud.elit.sdk.nlp.util.ModelType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {
    private Map<String, Object> field_map;

    public Sentence() {
        field_map = new HashMap<>();
    }

//  =================================== Getters ===================================

    public <V>V getFields(String field, String model) {
        return (V)field_map.get(getKey(field, model));
    }

    public List<String> getTokens(String model) {
        return getFields(FieldType.TOKEN, model);
    }

    public List<Offset> getOffsets(String model) {
        return getFields(FieldType.OFFSET, model);
    }

    public List<String> getLemmas(String model) {
        return getFields(FieldType.LEMMA, model);
    }

    public List<String> getPartOfSpeechTags(String model) {
        return getFields(FieldType.POS, model);
    }

    public List<String> getNamedEntityTags(String model) {
        return getFields(FieldType.NER, model);
    }

    public List<Arc> getDependencyRelations(String model) {
        return getFields(FieldType.DEP, model);
    }

//  =================================== Setters ===================================

    public <V>void setFields(String field, String model, V values) {
        field_map.put(getKey(field, model), values);
    }

    public void setTokens(String model, List<String> tokens) {
        setFields(FieldType.TOKEN, model, tokens);
    }

    public void setOffsets(String model, List<Offset> offsets) {
        setFields(FieldType.OFFSET, model, offsets);
    }

    public void setLemmas(String model, List<String> lemmas) {
        setFields(FieldType.LEMMA, model, lemmas);
    }

    public void setPartOfSpeechTags(String model, List<String> tags) {
        setFields(FieldType.POS, model, tags);
    }

    public void setNamedEntityTags(String model, List<String> tags) {
        setFields(FieldType.NER, model, tags);
    }

    public void setDependencyRelations(String model, List<Arc> arcs) {
        setFields(FieldType.NER, model, arcs);
    }

//  =================================== Helpers ===================================

    private String getKey(String field, String model) {
        return field + "_" + model;
    }
}

