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
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Chunk implements Serializable, Iterable<NLPNode> {
    private List<NLPNode> nodes;
    private String label;

//  =================================== Constructors ===================================

    public Chunk(List<NLPNode> nodes, String label) {
        setNodes(nodes);
        setLabel(label);
    }

    public Chunk(List<NLPNode> nodes) {
        this(nodes, null);
    }

    public Chunk() {
        this(new ArrayList<>());
    }

//  =================================== Getters, Setters ===================================

    public List<NLPNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<NLPNode> nodes) {
        this.nodes = nodes;
    }

    public int size() {
        return nodes.size();
    }

    public NLPNode get(int index) {
        return nodes.get(index);
    }

    public boolean add(NLPNode node) {
        return nodes.add(node);
    }

    public void add(int index, NLPNode node) {
        nodes.add(index, node);
    }

    public NLPNode set(int index, NLPNode node) {
        return nodes.set(index, node);
    }

    public NLPNode remove(int index) {
        return nodes.remove(index);
    }

    public boolean remove(NLPNode node) {
        return nodes.remove(node);
    }

    public boolean contains(NLPNode node) {
        return nodes.contains(node);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isLabel(String label) {
        return label.equals(this.label);
    }

//  =================================== Helpers ===================================

    public String toString() {
        if (nodes.isEmpty()) return "[]";
        StringBuilder build = new StringBuilder();

        build.append("[");
        build.append(nodes.get(0).getTokenID());
        build.append(",");
        build.append(nodes.get(nodes.size() - 1).getTokenID() + 1);

        if (label != null) {
            build.append(",\"");
            build.append(label);
            build.append("\"");
        }

        build.append("]");
        return build.toString();
    }

    @NotNull
    @Override
    public Iterator<NLPNode> iterator() {
        return nodes.iterator();
    }
}
