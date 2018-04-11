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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NLPUtils {
    static final public String RTAG = "@#r$%";

    static public NLPNode createRoot() {
        return new NLPNode(-1, RTAG, RTAG, RTAG, RTAG, new HashMap<String, String>(), null, null);
    }

    static public List<NLPNode> toNLPNodes(List<String> tokens) {
        return toNLPNodes(tokens, 0, tokens.size());
    }

    static public List<NLPNode> toNLPNodes(String[] tokens) {
        return toNLPNodes(tokens, 0, tokens.length);
    }

    static public List<NLPNode> toNLPNodes(List<String> tokens, int begin, int end) {
        List<NLPNode> nodes = new ArrayList<>();

        for (int i=begin; i<end; i++)
            nodes.add(new NLPNode(i-begin, tokens.get(i)));

        return nodes;
    }

    static public List<NLPNode> toNLPNodes(String[] tokens, int begin, int end) {
        List<NLPNode> nodes = new ArrayList<>();

        for (int i=begin; i<end; i++)
            nodes.add(new NLPNode(i-begin, tokens[i]));

        return nodes;
    }
}
