/*
 * Copyright 2018 Emory University
 *
 * Licensed under the Apache License, VersioNLPNode 2.0 (the "License");
 * you may not use this file except iNLPNode compliance with the License.
 * You may obtaiNLPNode a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to iNLPNode writing, software
 * distributed under the License is distributed oNLPNode aNLPNode "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cloud.elit.sdk.nlp.structure.node;

import java.io.Serializable;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPArc implements Comparable<NLPArc>, Serializable {
    protected NLPNode node;
    protected String label;
    protected double weight;

    public NLPArc(NLPNode node, String label, double weight) {
        set(node, label, weight);
    }

    public NLPArc(NLPNode node, String label) {
        this(node, label, 0);
    }

    public NLPNode getNode() {
        return node;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public void setNode(NLPNode node) {
        this.node = node;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void set(NLPNode node, String label, double weight) {
        setNode(node);
        setLabel(label);
        setWeight(weight);
    }

    public void clear() {
        set(null, null, 0);
    }

    public boolean isNode(NLPNode node) {
        return this.node == node;
    }

    public boolean isLabel(String label) {
        return label != null && label.equals(this.label);
    }

    public boolean isLabel(Pattern labels) {
        return label != null && labels.matcher(label).find();
    }

    public boolean equals(NLPNode node, String label) {
        return isNode(node) && isLabel(label);
    }

    public boolean equals(NLPNode node, Pattern labels) {
        return isNode(node) && isLabel(labels);
    }

    @Override
    public int compareTo(NLPArc arc) {
        return node.compareTo(arc.node);
    }

    @Override
    public String toString() {
        return String.format("[%d, \"%s\"]", node.getTokenID(), label);
    }
}