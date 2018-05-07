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
package cloud.elit.ddr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DSUtils {
    private DSUtils() {
    }

    /**
     * e.g., getFirst(list, "VB", (n,s) - Token::isPartOfSpeechTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return the first item in the list matching the condition if exists; otherwise, {@code null}.
     */
    static public <N> N getFirst(List<N> list, Predicate<N> matcher) {
        return list.stream().filter(matcher).findFirst().orElse(null);
    }

    /**
     * e.g., getLast(list, "VB", (n,s) - Token::isPartOfSpeechTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return the last node in the list matching the condition.
     */
    static public <N> N getLast(List<N> list, Predicate<N> matcher) {
        return list.stream().filter(matcher).reduce((a, b) -> b).orElse(null);
    }

    /**
     * e.g., getMatchedList(list, "VB", (n,s) - Token::isPartOfSpeechTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return the sublist of the original list containing only matched items.
     */
    static public <N> List<N> getMatchedList(List<N> list, Predicate<N> matcher) {
        return list.stream().filter(matcher).collect(Collectors.toList());
    }

    /**
     * e.g., contains(list, "VB", (n,s) - Token::isPartOfSpeechTag);
     * @param matcher takes a node and the supplement, and returns true if its field matches to the specific predicate.
     * @return true if the list contains any item matching the condition.
     */
    static public <N> boolean contains(List<N> list, Predicate<N> matcher) {
        return list.stream().anyMatch(matcher);
    }

    static public <T extends Comparable<T>> int binarySearch(List<T> list, T key) {
        int index = Collections.binarySearch(list, key);
        return (index < 0) ? -(index + 1) : index + 1;
    }

    static public Set<String> createStringHashSet(InputStream in) {
        return createStringHashSet(in, true, false);
    }

    /**
     * @param in internally wrapped by {@code new BufferedReader(new InputStreamReader(in))}.
     *           The file that the input-stream is created from consists of one entry per line.
     */
    static public Set<String> createStringHashSet(InputStream in, boolean trim, boolean decap) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Set<String> set = new HashSet<>();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (trim) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                }

                if (decap)
                    line = StringUtils.toLowerCase(line);

                set.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return set;
    }

    static public Map<String, String> createStringHashMap(InputStream in, CharTokenizer tokenizer) {
        return createStringHashMap(in, tokenizer, true);
    }

    /**
     * @param in internally wrapped by {@code new BufferedReader(new InputStreamReader(in))}.
     *           The file that the input-stream is created from consists of one entry per line.
     */
    static public Map<String, String> createStringHashMap(InputStream in, CharTokenizer tokenizer, boolean trim) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Map<String, String> map = new HashMap<>();
        String[] t;
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (trim) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                }

                t = tokenizer.tokenize(line);
                map.put(t[0], t[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    static public <T extends Comparable<? extends T>> void sortReverseOrder(List<T> list) {
        list.sort(Collections.reverseOrder());
    }

    static public <T extends Comparable<? extends T>> void sortReverseOrder(T[] array) {
        Arrays.sort(array, Collections.reverseOrder());
    }

    static public <T> boolean hasIntersection(Collection<T> col1, Collection<T> col2) {
        if (col2.size() < col1.size()) {
            Collection<T> tmp = col1;
            col1 = col2;
            col2 = tmp;
        }

        for (T item : col1) {
            if (col2.contains(item))
                return true;
        }

        return false;
    }

    /**
     * @return a setForm containing all field values of this class.
     */
    static public Set<String> getFieldSet(Class<?> cs) {
        Set<String> set = new HashSet<>();

        try {
            for (Field f : cs.getFields())
                set.add(f.get(cs).toString());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return set;
    }

    /**
     * @return the index'th item if exists; otherwise, {@code null}.
     */
    static public <T> T get(List<T> list, int index) {
        return isRange(list, index) ? list.get(index) : null;
    }

    /**
     * @return the index'th item if exists; otherwise, {@code null}.
     */
    static public <T> T get(T[] array, int index) {
        return isRange(array, index) ? array[index] : null;
    }

    /**
     * @return the first item in the list if exists; otherwise, {@code null}.
     */
    static public <T> T getFirst(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * @return the last item in the list if exists; otherwise, {@code null}.
     */
    static public <T> T getLast(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    static public <T> boolean isRange(List<T> list, int index) {
        return 0 <= index && index < list.size();
    }

    static public <T> boolean isRange(T[] array, int index) {
        return 0 <= index && index < array.length;
    }

    /**
     * @param beginIndex inclusive
     * @param endIndex   exclusive
     */
    static public int[] range(int beginIndex, int endIndex, int gap) {
        double d = MathUtils.divide(endIndex - beginIndex, gap);
        if (d < 0) return new int[0];

        int[] array = new int[MathUtils.ceil(d)];
        int i, j;

        if (beginIndex < endIndex) {
            for (i = beginIndex, j = 0; i < endIndex; i += gap, j++)
                array[j] = i;
        } else {
            for (i = beginIndex, j = 0; i > endIndex; i += gap, j++)
                array[j] = i;
        }

        return array;
    }

    static public int[] range(int size) {
        return range(0, size, 1);
    }

    static public void swap(int[] array, int index0, int index1) {
        int tmp = array[index0];
        array[index0] = array[index1];
        array[index1] = tmp;
    }

    static public <T> void swap(List<T> list, int index0, int index1) {
        T tmp = list.get(index0);
        list.set(index0, list.get(index1));
        list.set(index1, tmp);
    }

    static public void shuffle(int[] array, Random rand) {
        shuffle(array, rand, array.length);
    }

    /**
     * Calls {@link #shuffle(List, Random, int)}, where {@code lastIndex = list.size()}.
     */
    static public <T> void shuffle(List<T> list, Random rand) {
        shuffle(list, rand, list.size());
    }

    static public void shuffle(int[] array, Random rand, int lastIndex) {
        int i, j, size = lastIndex - 1;

        for (i = 0; i < size; i++) {
            j = rand.nextInt(size - i) + i + 1;
            swap(array, i, j);
        }
    }

    /**
     * A slightly modified version of Durstenfeld's shuffle algorithm.
     * @param lastIndex shuffle up to this index (exclusive, cannot be greater than the list of the list).
     */
    static public <T> void shuffle(List<T> list, Random rand, int lastIndex) {
        int i, j, size = lastIndex - 1;

        for (i = 0; i < size; i++) {
            j = rand.nextInt(size - i) + i + 1;
            swap(list, i, j);
        }
    }

    /**
     * Adds all items in the specific array to the specific list.
     */
    static public void addAll(List<String> list, String[] array) {
        Collections.addAll(list, array);
    }

    static public <T> void removeLast(List<T> list) {
        if (!list.isEmpty()) list.remove(list.size() - 1);
    }

    static public int max(int[] array) {
        int i, size = array.length;
        int m = array[0];

        for (i = 1; i < size; i++)
            m = Math.max(m, array[i]);

        return m;
    }

    static public float max(float[] array) {
        int i, size = array.length;
        float m = array[0];

        for (i = 1; i < size; i++)
            m = Math.max(m, array[i]);

        return m;
    }

    static public double max(double[] array) {
        int i, size = array.length;
        double m = array[0];

        for (i = 1; i < size; i++)
            m = Math.max(m, array[i]);

        return m;
    }

    static public float min(float[] array) {
        int i, size = array.length;
        float m = array[0];

        for (i = 1; i < size; i++)
            m = Math.min(m, array[i]);

        return m;
    }

    static public double min(double[] array) {
        int i, size = array.length;
        double m = array[0];

        for (i = 1; i < size; i++)
            m = Math.min(m, array[i]);

        return m;
    }

    static public int maxIndex(double[] array) {
        int i, size = array.length, maxIndex = 0;
        double maxValue = array[maxIndex];

        for (i = 1; i < size; i++) {
            if (maxValue < array[i]) {
                maxIndex = i;
                maxValue = array[maxIndex];
            }
        }

        return maxIndex;
    }

    static public int maxIndex(double[] array, int[] indices) {
        int i, j, size = indices.length, maxIndex = indices[0];
        double maxValue = array[maxIndex];

        for (j = 1; j < size; j++) {
            i = indices[j];

            if (maxValue < array[i]) {
                maxIndex = i;
                maxValue = array[i];
            }
        }

        return maxIndex;
    }

    static public <T> List<?>[] createEmptyListArray(int size) {
        List<?>[] array = new ArrayList<?>[size];

        for (int i = 0; i < size; i++)
            array[i] = new ArrayList<T>();

        return array;
    }

    static public <T> PriorityQueue<?>[] createEmptyPriorityQueueArray(int size, boolean ascending) {
        PriorityQueue<?>[] queue = new PriorityQueue<?>[size];

        for (int i = 0; i < size; i++)
            queue[i] = ascending ? new PriorityQueue<>() : new PriorityQueue<>(Collections.reverseOrder());

        return queue;
    }

    @SuppressWarnings("unchecked")
    static public <T> List<T> toList(T... items) {
        return Arrays.stream(items).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    static public <T> Set<T> toHashSet(T... items) {
        return Arrays.stream(items).collect(Collectors.toSet());
    }

    static public <T> Set<T> merge(List<Set<T>> sets) {
        Set<T> merge = new HashSet<>();
        for (Set<T> set : sets) merge.addAll(set);
        return merge;
    }

    static public String[] toArray(Collection<String> col) {
        if (col == null) return null;
        String[] array = new String[col.size()];
        col.toArray(array);
        return array;
    }

    static public <T> List<T> removeAll(Collection<T> source, Collection<T> remove) {
        List<T> list = new ArrayList<>(source);
        list.removeAll(remove);
        return list;
    }

    /**
     * @return true if s2 is a subset of s1.
     */
    static public <T> boolean isSubset(Collection<T> s1, Collection<T> s2) {
        for (T t : s2) {
            if (!s1.contains(t))
                return false;
        }

        return true;
    }

    static public float[] toFloatArray(double[] array) {
        float[] f = new float[array.length];

        for (int i = 0; i < array.length; i++)
            f[i] = (float) array[i];

        return f;
    }

    @SuppressWarnings("unchecked")
    static public <T> Set<T> createSet(T... array) {
        return new HashSet<>(Arrays.asList(array));
    }

    static public void normalize01(float[] array) {
        float min = min(array);
        float div = max(array) - min;

        for (int i = 0; i < array.length; i++)
            array[i] = (array[i] - min) / div;
    }
}