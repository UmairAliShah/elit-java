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

package cloud.elit.sdk.nlp.structure.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPNodeList implements Serializable {
    protected List<NLPNode> nodes;

    public NLPNodeList(List<NLPNode> nodes) {
        setNodes(nodes);
    }

    public NLPNodeList() {
        this(new ArrayList<>());
    }

    public List<NLPNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<NLPNode> nodes) {
        this.nodes = nodes;
    }

    public int numNodes() {
        return nodes.size();
    }

    public NLPNode getNode(int index) {
        return nodes.get(index);
    }

    public boolean addNode(NLPNode node) {
        return nodes.add(node);
    }

    public void addNode(int index, NLPNode node) {
        nodes.add(index, node);
    }

    public NLPNode setNode(int index, NLPNode node) {
        return nodes.set(index, node);
    }

    public NLPNode removeNode(int index) {
        return nodes.remove(index);
    }

    public boolean removeNode(NLPNode node) {
        return nodes.remove(node);
    }
}
