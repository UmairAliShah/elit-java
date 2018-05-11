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

import java.io.Serializable;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Arc<N extends Comparable<N>> implements Comparable<Arc<N>>, Serializable {
    private static final long serialVersionUID = -5463365667122231165L;
    protected N node;
    protected String label;

    public Arc(N node, String label) {
        set(node, label);
    }

    public N getNode() {
        return node;
    }

    public String getLabel() {
        return label;
    }

    public void setNode(N node) {
        this.node = node;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void set(N node, String label) {
        setNode(node);
        setLabel(label);
    }

    public boolean isNode(N node) {
        return this.node == node;
    }

    public boolean isLabel(String label) {
        return label != null && label.equals(this.label);
    }

    public boolean isLabel(Pattern labels) {
        return label != null && labels.matcher(label).find();
    }

    public boolean equals(N node, String label) {
        return isNode(node) && isLabel(label);
    }

    public boolean equals(N node, Pattern labels) {
        return isNode(node) && isLabel(labels);
    }

    public void clear() {
        set(null, null);
    }

    @Override
    public int compareTo(@NotNull Arc<N> arc) {
        return node.compareTo(arc.node);
    }
}