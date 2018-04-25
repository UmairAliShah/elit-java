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

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MorphologicalAnalyzerTest {
    MorphologicalAnalyzer analyzer = new MorphologicalAnalyzer();

    @Test
    void test() throws Exception {
        final String suffix_rules_file = "/Users/jdchoi/workspace/unigram-suffix-rules.jobj";
        final String suffix_rules_by_sources_file = "/Users/jdchoi/workspace/unigram-suffix-rules-by-sources.jobj";
        final String suffix_weight_file = "/Users/jdchoi/workspace/unigram-suffix-weights.jobj";
        ObjectInputStream oin;

//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/jdchoi/workspace/unigram.tsv")));
//        Object2IntMap<String> vocab = new Object2IntOpenHashMap<>();
//        Pattern split = Pattern.compile("\t");
//        String line;
//
//        while ((line = reader.readLine()) != null) {
//            String[] t = split.split(line);
//            vocab.mergeInt(t[0], Integer.parseInt(t[6]), (o, n) -> o + n);
//        }
//
//        System.out.println("Vocab: "+vocab.size());
//
//        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(new FileInputStream(suffix_rules_file)));
//        Map<String, Set<AffixRule>> suffix_rules = (Map<String, Set<AffixRule>>)oin.readObject();
//        oin.close();
//        System.out.println("Suffix rules: "+suffix_rules.size());
//
//        Map<String, Map<String, List<AffixRule>>> source_map = analyzer.groupAffixRulesBySources(suffix_rules, AffixType.SUFFIX);
//        System.out.println("Source: "+source_map.size());
//
//        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(suffix_rules_by_sources_file)));
//        oout.writeObject(source_map);
//        oout.close();
//
//        Object2DoubleMap<AffixRule> weights = analyzer.weightAffixRules(vocab.keySet(), source_map, AffixType.SUFFIX, 100);
//        System.out.println("Weights: "+weights.size());
//
//        oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(suffix_weight_file)));
//        oout.writeObject(weights);
//        oout.close();





        oin = new ObjectInputStream(new BufferedInputStream(new FileInputStream(suffix_rules_by_sources_file)));
        Map<String, Map<String, List<AffixRule>>> source_map = (Map<String, Map<String, List<AffixRule>>>)oin.readObject();
        oin.close();
        System.out.println("Source: "+source_map.size());

        oin = new ObjectInputStream(new BufferedInputStream(new FileInputStream(suffix_weight_file)));
        Object2DoubleMap<AffixRule> weights = (Object2DoubleMap<AffixRule>)oin.readObject();
        oin.close();
        System.out.println("Weights: "+weights.size());

        Map<String, AffixRule> argmax = analyzer.argmaxAffixRules(source_map, weights, AffixType.SUFFIX, 0.2);
        PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-count.out")));
        for (Map.Entry<String, AffixRule> e : argmax.entrySet())
            fout.println(e.getKey()+" "+e.getValue());
        fout.close();




//        Map<String, AffixRule> argmaxAffixRules


//        Object2LongMap<String> counts = analyzer.countTargetAffixes(vocab.keySet(), source_map);
//        PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-count.out")));
//        for (Object2LongMap.Entry<String> e : counts.object2LongEntrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList()))
//            fout.println(e.getKey()+" "+e.getLongValue());
//        fout.close();

//
//        oin = new ObjectInputStream(new BufferedInputStream(new FileInputStream(suffix_weight_file)));
//        Object2DoubleMap<AffixRule> suffix_weights = (Object2DoubleMap<AffixRule>)oin.readObject();
//        oin.close();
//        System.out.println("Suffix weights: "+suffix_weights.size());
//
//        Map<String, AffixRule> suffix_final = analyzer.argmaxAffixRules(source_map, suffix_weights, AffixType.SUFFIX, 0.3);
//
//        PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-sf.out")));
//        for (Map.Entry<String, AffixRule> e : suffix_final.entrySet()) fout.println(e.getKey()+" "+e.getValue());
//        fout.close();


//        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(suffix_weight_file)));
//        oout.writeObject(suffix_weights);
//        oout.close();
//
//        PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-weights.out")));
//        for (Map.Entry<AffixRule, Double> e : suffix_weights.object2DoubleEntrySet()) fout.println(e.getKey()+" "+e.getValue());
//        fout.close();




//        PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-r5a.out")));
//        for (Map.Entry<String, Set<AffixRule>> e : suffix_rules.entrySet()) fout.println(e.getKey()+" "+e.getValue());
//        fout.close();

//        suffix_rules = analyzer.argmaxAffixRules(AffixType.SUFFIX, suffix_rules);
//        fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-r5t.out")));
//        for (Map.Entry<String, Set<AffixRule>> e : suffix_rules.entrySet()) fout.println(e.getKey()+" "+e.getValue());
//        fout.close();



//        List<Object2IntMap.Entry<AffixRule>> list = analyzer.weightAffixRules(AffixType.SUFFIX, suffix_rules, 2);
//

//
//        fout = new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/jdchoi/workspace/unigram-r4.out")));
//        int total = 0;
//
//        for (int d=list.size()-1; d>=0; d--) {
//            Object2IntMap.Entry<AffixRule> e = list.get(d);
//            fout.println(e.getKey()+"\t"+e.getIntValue());
//            total += e.getIntValue();
//        }
//
//        fout.close();
//        System.out.println(total);
    }

//    @Test
    void getSuffixRules() {
        List<String> tokens = List.of("study", "studies", "studied", "studying", "student", "students", "stud", "studded", "studio", "studs", "studding");
        Map<String, Set<AffixRule>> rules = analyzer.getAffixRules(tokens, AffixType.SUFFIX, 4);
        assertEquals(4, rules.size());

        List<AffixRule> list = rules.get("study").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"\",\"ing\",1)]", list.toString());

        list = rules.get("student").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"\",\"s\",1)]", list.toString());

        list = rules.get("stud").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"y\",\"ent\",1), (\"y\",\"ied\",1), (\"y\",\"ies\",1)]", list.toString());

        list = rules.get("studie").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"s\",\"d\",1)]", list.toString());

        tokens = List.of("beauty", "beautiful", "beautifully", "beautifuler");
        rules = analyzer.getAffixRules(tokens, AffixType.SUFFIX, 4);
        assertEquals(2, rules.size());

        list = rules.get("beaut").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"y\",\"iful\",1)]", list.toString());

        list = rules.get("beautiful").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"\",\"er\",1), (\"\",\"ly\",1)]", list.toString());

        tokens = List.of("attach", "attachment", "attaching", "attached", "attaches");
        rules = analyzer.getAffixRules(tokens, AffixType.SUFFIX, 4);
        assertEquals(2, rules.size());

        list = rules.get("attach").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"\",\"ed\",1), (\"\",\"es\",1), (\"\",\"ing\",1), (\"\",\"ment\",1)]", list.toString());

        list = rules.get("attache").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"d\",\"s\",1)]", list.toString());

        tokens = List.of("possible", "impossible", "ultra-impossible");
        rules = analyzer.getAffixRules(tokens, AffixType.PREFIX, 4);
        assertEquals(2, rules.size());

        list = rules.get("possible").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"\",\"im\",-1)]", list.toString());

        list = rules.get("impossible").stream().sorted().collect(Collectors.toList());
        assertEquals("[(\"\",\"ultra-\",-1)]", list.toString());
    }

    @Test
    void getSuffixRule() {
        String[] e;

        e = analyzer.getAffixRule("study", "studying", AffixType.SUFFIX, 4);
        assertEquals("study", e[0]);
        assertEquals("", e[1]);
        assertEquals("ing", e[2]);

        e = analyzer.getAffixRule("studying", "study", AffixType.SUFFIX, 4);
        assertEquals("study", e[0]);
        assertEquals("ing", e[1]);
        assertEquals("", e[2]);

        e = analyzer.getAffixRule("study", "studied", AffixType.SUFFIX, 4);
        assertEquals("stud", e[0]);
        assertEquals("y", e[1]);
        assertEquals("ied", e[2]);

        // stem not long enough
        e = analyzer.getAffixRule("study", "studied", AffixType.SUFFIX, 5);
        assertNull(e);

        // no matching affix
        e = analyzer.getAffixRule("study", "study", AffixType.SUFFIX, 4);
        assertNull(e);

        e = analyzer.getAffixRule("underpants", "pants", AffixType.PREFIX, 4);
        assertEquals("pants", e[0]);
        assertEquals("under", e[1]);
        assertEquals("", e[2]);

        e = analyzer.getAffixRule("pants", "underpants", AffixType.PREFIX, 4);
        assertEquals("pants", e[0]);
        assertEquals("", e[1]);
        assertEquals("under", e[2]);
    }
}