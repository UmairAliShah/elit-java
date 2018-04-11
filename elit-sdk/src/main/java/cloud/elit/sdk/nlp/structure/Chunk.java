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
import cloud.elit.sdk.util.DSUtils;

import java.util.ArrayList;
import java.util.List;

public class Chunk extends NLPNodeList {
    private String label;

    public Chunk(List<NLPNode> nodes, String label) {
        super(nodes);
        setLabel(label);
    }

    public Chunk(List<NLPNode> nodes) {
        this(nodes, null);
    }

    public Chunk() {
        this(new ArrayList<>());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();

        build.append("[");
        build.append(DSUtils.getFirst(nodes).getTokenID());
        build.append(", ");
        build.append(DSUtils.getLast(nodes).getTokenID()+1);

        if (label != null) {
            build.append(", \"");
            build.append(label);
            build.append("\"");
        }

        build.append("]");
        return build.toString();
    }
}
