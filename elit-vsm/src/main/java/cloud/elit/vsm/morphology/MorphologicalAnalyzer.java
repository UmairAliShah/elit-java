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

package cloud.elit.vsm.morphology;

import cloud.elit.sdk.collection.pair.ObjectDoublePair;
import it.unimi.dsi.fastutil.objects.*;

import java.util.*;
import java.util.stream.Collectors;

public class MorphologicalAnalyzer {
    private final int min_prefix = 4;
    private final int min_suffix = 4;
    private final int cutoff_suffix = 100;

    public void train(Object2IntMap<String> vocab, AffixType type, int min, int cutoff) {
        List<String> tokens = new ArrayList<>(vocab.keySet());
        Map<String, Set<AffixRule>> rules = getAffixRules(tokens, type, min);
        Map<String, Map<String, List<AffixRule>>> source_map = groupAffixRulesBySources(rules, type);
        Object2DoubleMap<AffixRule> weights = weightAffixRules(tokens, source_map, type, cutoff);

    }

    /**
     * @param vocab the list of all tokens in the vocabulary.
     * @param type  the affix type.
     * @param min   the minimum length of an acceptable stem.
     * @return a map where each key is a stem and its value is a set of affix rules.
     * Note that a shorter affix is assumed to be the source in each rule.
     */
    public Map<String, Set<AffixRule>> getAffixRules(List<String> vocab, AffixType type, int min) {
        Map<String, Set<AffixRule>> rules = new HashMap<>();

        for (int i = 0; i < vocab.size(); i++) {
            String tok1 = vocab.get(i);

            for (int j = i + 1; j < vocab.size(); j++) {
                String tok2 = vocab.get(j);
                String[] rule = getAffixRule(tok1, tok2, type, min);

                if (rule != null) {
                    String aff1 = rule[1];
                    String aff2 = rule[2];

                    if (aff2.length() < aff1.length()) {
                        String t = aff1;
                        aff1 = aff2;
                        aff2 = t;
                    }

                    rules.computeIfAbsent(rule[0], e -> new HashSet<>()).add(new AffixRule(type, aff1, aff2));
                }
            }
        }

        return rules;
    }

    /**
     * @param tok1 the first token.
     * @param tok2 the second token.
     * @param type the affix type.
     * @param min  the minimum length of an acceptable stem.
     * @return {stem, affix from tok1, affix from tok2} if satisfied; otherwise, null.
     */
    public String[] getAffixRule(String tok1, String tok2, AffixType type, int min) {
        int dir = type.getDirection(), idx1 = 0, idx2 = 0;
        char[] c1 = tok1.toCharArray();
        char[] c2 = tok2.toCharArray();

        if (dir < 0) {
            idx1 = c1.length - 1;
            idx2 = c2.length - 1;
        }

        for (; 0 <= idx1 && idx1 < c1.length && 0 <= idx2 && idx2 < c2.length; idx1 += dir, idx2 += dir) {
            if (c1[idx1] != c2[idx2]) break;
        }

        String stem, aff1, aff2;

        if (dir < 0) {
            idx1++;
            idx2++;

            stem = tok1.substring(idx1);
            aff1 = tok1.substring(0, idx1);
            aff2 = tok2.substring(0, idx2);
        } else {
            stem = tok1.substring(0, idx1);
            aff1 = tok1.substring(idx1);
            aff2 = tok2.substring(idx2);
        }

        return stem.length() < min || (aff1.isEmpty() && aff2.isEmpty()) ? null : new String[]{stem, aff1, aff2};
    }

    /**
     * @param rules     a collection of affix rules.
     * @param type      the affix type.
     * @param core_only if true, retain only core rules.
     * @return a map where the key is a source affix and the value is a list of affix rules associated with the source.
     */
    public Map<String, List<AffixRule>> groupAffixRulesBySources(Collection<AffixRule> rules, AffixType type, boolean core_only) {
        Map<String, List<AffixRule>> map = new HashMap<>();

        for (AffixRule rule : rules)
            map.computeIfAbsent(rule.getSource(), r -> new ArrayList<>()).add(rule);

        if (!core_only)
            return map;

        for (Map.Entry<String, List<AffixRule>> e : map.entrySet()) {
            List<AffixRule> list = e.getValue().stream().sorted().collect(Collectors.toList());
            Set<AffixRule> set = new HashSet<>();

            for (int i = 0; i < list.size(); i++) {
                AffixRule r1 = list.get(i);
                if (set.contains(r1)) continue;

                for (int j = i + 1; j < list.size(); j++) {
                    AffixRule r2 = list.get(j);
                    if (set.contains(r2)) continue;

                    switch (type) {
                        case SUFFIX:
                            if (r2.getTarget().startsWith(r1.getTarget())) set.add(r2);
                            break;
                        case PREFIX:
                            if (r2.getTarget().endsWith(r1.getTarget())) set.add(r2);
                            break;
                    }
                }
            }

            list.removeAll(set);
            e.setValue(list);
        }

        return map;
    }

    /**
     * {@link #groupAffixRulesBySources(Collection, AffixType, boolean)}, where core_only = true.
     */
    public Map<String, List<AffixRule>> groupAffixRulesBySources(Collection<AffixRule> rules, AffixType type) {
        return groupAffixRulesBySources(rules, type, true);
    }

    /**
     * @param rules     a map where the key is a stem and the value is a set of affix rules associated with the stem.
     * @param type      the affix type.
     * @param core_only if true, retain only core rules.
     * @return a map where the key is a stem and the value is the output of {@link #groupAffixRulesBySources(Collection, AffixType, boolean)}.
     */
    public Map<String, Map<String, List<AffixRule>>> groupAffixRulesBySources(Map<String, Set<AffixRule>> rules, AffixType type, boolean core_only) {
        return rules.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, r -> groupAffixRulesBySources(r.getValue(), type)));
    }

    /**
     * {@link #groupAffixRulesBySources(Map, AffixType, boolean)}, where core_only = true.
     */
    public Map<String, Map<String, List<AffixRule>>> groupAffixRulesBySources(Map<String, Set<AffixRule>> rules, AffixType type) {
        return groupAffixRulesBySources(rules, type, true);
    }

    public Object2LongMap<String> countTargetAffixes(Collection<String> vocab, Map<String, Map<String, List<AffixRule>>> source_map) {
        Object2LongMap<String> map = new Object2LongOpenHashMap<>();

        for (Map<String, List<AffixRule>> em : source_map.values())
            for (Map.Entry<String, List<AffixRule>> es : em.entrySet())
                for (AffixRule rule : es.getValue())
                    if (!map.containsKey(rule.getTarget()))
                        map.put(rule.getTarget(), vocab.stream().filter(t -> t.endsWith(rule.getTarget())).count());

        return map;
    }

    public Object2DoubleMap<AffixRule> weightAffixRules(Collection<String> vocab, Map<String, Map<String, List<AffixRule>>> source_map, AffixType type, int cutoff) {
        Object2LongMap<AffixRule> weights = new Object2LongOpenHashMap<>();
        Object2LongMap<String> targets = new Object2LongOpenHashMap<>();

        for (Map<String, List<AffixRule>> em : source_map.values()) {
            for (Map.Entry<String, List<AffixRule>> es : em.entrySet()) {
                for (AffixRule rule : es.getValue()) {
                    weights.mergeLong(rule, 1, (o, n) -> o + n);

                    if (!targets.containsKey(rule.getTarget()))
                        targets.put(rule.getTarget(), vocab.stream().filter(t -> t.endsWith(rule.getTarget())).count());
                }
            }
        }

        Object2DoubleMap<AffixRule> t = new Object2DoubleOpenHashMap<>();

        for (Object2LongMap.Entry<AffixRule> e : weights.object2LongEntrySet()) {
            long count = targets.getLong(e.getKey().getTarget());
            if (cutoff <= count) t.put(e.getKey(), (double) e.getLongValue() / count);
        }

        return t;
    }

    public Map<String, AffixRule> argmaxAffixRules(Map<String, Map<String, List<AffixRule>>> source_map, Object2DoubleMap<AffixRule> weights, AffixType type, double threshold) {
        Map<String, ObjectDoublePair<AffixRule>> map = new HashMap<>();

        for (Map.Entry<String, Map<String, List<AffixRule>>> e : source_map.entrySet()) {
            String stem = e.getKey();

            for (Map.Entry<String, List<AffixRule>> er : e.getValue().entrySet()) {
                String source = er.getKey();

                for (AffixRule rule : er.getValue()) {
                    double weight = weights.getOrDefault(rule, 0);
                    if (weight < threshold) continue;
                    String token = stem + rule.getTarget();

                    if (map.containsKey(token)) {
                        ObjectDoublePair<AffixRule> prev = map.get(token);
                        if (prev.d < weight) prev.set(rule, weight);
                    } else
                        map.put(token, new ObjectDoublePair<>(rule, weight));
                }
            }
        }

        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().o));
    }
}
