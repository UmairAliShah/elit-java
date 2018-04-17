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

package cloud.elit.sdk.structure.util;

import cloud.elit.sdk.structure.node.NLPNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPUtils {
    static final public String RTAG = "@#r$%";

    static public NLPNode createRoot() {
        return new NLPNode(-1, RTAG, RTAG, RTAG, new HashMap<String, String>());
    }

    static public <T extends Comparable<T>> int binarySearch(List<T> list, T key) {
        int index = Collections.binarySearch(list, key);
        return (index < 0) ? -(index + 1) : index + 1;
    }
}
