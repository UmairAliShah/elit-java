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
    private int head_id;
    private String label;

    public Arc(int head_id, String label) {
        setHeadID(head_id);
        setLabel(label);
    }

    public Arc(int head_id) {
        this(head_id, null);
    }

    public Arc(String label) {
        this(-1, label);
    }

    public Arc(Arc arc) {
        this(arc.head_id, arc.label);
    }

    public int getHeadID() {
        return head_id;
    }

    public String getLabel() {
        return label;
    }

    public void setHeadID(int head_id) {
        this.head_id = head_id;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
