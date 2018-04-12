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

package cloud.elit.sdk.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DSUtils {
    static public <N>N getFirst(List<N> list) {
        return list.get(0);
    }

    static public <N>N getLast(List<N> list) {
        return list.get(list.size() - 1);
    }

    /**
     * e.g., getFirst(list, "VB", (n,s) - AbstractNode::isSyntacticTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return the first item in the list matching the condition if exists; otherwise, {@code null}.
     */
    static public <N>N getFirst(List<N> list, Predicate<N> matcher)
    {
        return list.stream().filter(matcher).findFirst().orElse(null);
    }

    /**
     * e.g., getLast(list, "VB", (n,s) - AbstractNode::isSyntacticTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return the last node in the list matching the condition.
     */
    static public <N>N getLast(List<N> list, Predicate<N> matcher)
    {
        return list.stream().filter(n -> matcher.test(n)).reduce((a, b) -> b).orElse(null);
    }

    /**
     * e.g., getMatchedList(list, "VB", (n,s) - AbstractNode::isSyntacticTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return the sublist of the original list containing only matched items.
     */
    static public <N>List<N> getMatchedList(List<N> list, Predicate<N> matcher)
    {
        return list.stream().filter(n -> matcher.test(n)).collect(Collectors.toList());
    }

    /**
     * e.g., contains(list, "VB", (n,s) - AbstractNode::isSyntacticTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return true if the list contains any item matching the condition.
     */
    static public <N>boolean contains(List<N> list, Predicate<N> matcher)
    {
        return list.stream().anyMatch(n -> matcher.test(n));
    }

    /**
     * @param list the list.
     * @param index the index.
     * @return {@code true} if the index is within a valid range of the list.
     */
    static public <N>boolean isRange(List<N> list, int index) {
        return 0 <= index && index < list.size();
    }

    static public <T extends Comparable<T>>int binarySearch(List<T> list, T key) {
        int index = Collections.binarySearch(list, key);
        return (index < 0) ? -(index+1) : index+1;
    }
}
