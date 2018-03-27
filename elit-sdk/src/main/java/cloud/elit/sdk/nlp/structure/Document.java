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

import java.util.ArrayList;
import java.util.List;

public class Document
{
    private List<Sentence> sentences;

    // ============================ Constructors ============================

    public Document() {
        sentences = new ArrayList<>();
    }

    // ============================ Getters and Setters ============================

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public Sentence getSentence(int index) {
        return sentences.get(index);
    }

    public Sentence setSentence(int index, Sentence sentence) {
        return sentences.set(index, sentence);
    }

    public boolean addSentence(Sentence sentence) {
        return sentences.add(sentence);
    }
}
