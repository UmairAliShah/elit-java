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

public class Arc {
    private int sentence_id;
    private int token_id;
    private String label;

    // ============================ Constructors ============================

    public Arc(int sentence_id, int node_id, String label) {
        setSentenceID(sentence_id);
        setNodeID(node_id);
        setLabel(label);
    }

    public Arc(int node_id, String label) {
        this(-1, node_id, label);
    }

    public Arc(int head_id) {
        this(-1, head_id, null);
    }

    public Arc(String label) {
        this(-1, -1, label);
    }

    public Arc(Arc arc) {
        this(arc.sentence_id, arc.token_id, arc.label);
    }

    public Arc() {
        this(-1, -1, null);
    }

    // ============================ Getters and Setters ============================

    public int getSentenceID() {
        return sentence_id;
    }

    public void setSentenceID(int sentence_id) {
        this.sentence_id = sentence_id;
    }

    public int getNodeID() {
        return token_id;
    }

    public void setNodeID(int head_id) {
        this.token_id = head_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
