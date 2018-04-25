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

package cloud.elit.ddr.lemmatize.english;

import cloud.elit.ddr.lemmatize.Lemmatizer;
import cloud.elit.ddr.util.MetaConst;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnglishLemmatizerTest {
    @Test
    public void test() {
        String[][] tokens = {
                // abbreviation
                {"n't", "RB", "not"},
                {"na", "TO", "to"},

                // ordinal
                {"1st", "XX", MetaConst.ORDINAL},
                {"12nd", "XX", MetaConst.ORDINAL},
                {"23rd", "XX", MetaConst.ORDINAL},
                {"34th", "XX", MetaConst.ORDINAL},
                {"first", "XX", MetaConst.ORDINAL},
                {"third", "XX", MetaConst.ORDINAL},
                {"fourth", "XX", MetaConst.ORDINAL},

                // cardinal
                {"zero", "NN", MetaConst.CARDINAL},
                {"ten", "CD", MetaConst.CARDINAL},
                {"tens", "NNS", MetaConst.CARDINAL},
                {"eleven", "CD", MetaConst.CARDINAL},
                {"fourteen", "CD", MetaConst.CARDINAL},
                {"thirties", "NNS", MetaConst.CARDINAL},

                // verb: 3rd-person singular
                {"studies", "VBZ", "study"},
                {"pushes", "VBZ", "push"},
                {"takes", "VBZ", "take"},

                // verb: gerund
                {"lying", "VBG", "lie"},
                {"feeling", "VBG", "feel"},
                {"running", "VBG", "run"},
                {"taking", "VBG", "take"},

                // verb: past (participle)
                {"denied", "VBD", "deny"},
                {"entered", "VBD", "enter"},
                {"zipped", "VBD", "zip"},
                {"heard", "VBD", "hear"},
                {"drawn", "VBN", "draw"},
                {"clung", "VBN", "cling"},

                // verb: irregular
                {"chivvies", "VBZ", "chivy"},
                {"took", "VBD", "take"},
                {"beaten", "VBN", "beat"},
                {"forbidden", "VBN", "forbid"},
                {"bitten", "VBN", "bite"},
                {"spoken", "VBN", "speak"},
                {"woven", "VBN", "weave"},
                {"woken", "VBN", "wake"},
                {"slept", "VBD", "sleep"},
                {"fed", "VBD", "feed"},
                {"led", "VBD", "lead"},
                {"learnt", "VBD", "learn"},
                {"rode", "VBD", "ride"},
                {"spoke", "VBD", "speak"},
                {"woke", "VBD", "wake"},
                {"wrote", "VBD", "write"},
                {"bore", "VBD", "bear"},
                {"stove", "VBD", "stave"},
                {"drove", "VBD", "drive"},
                {"wove", "VBD", "weave"},

                // noun: plural
                {"studies", "NNS", "study"},
                {"crosses", "NNS", "cross"},
                {"areas", "NNS", "area"},
                {"gentlemen", "NNS", "gentleman"},
                {"vertebrae", "NNS", "vertebra"},
                {"foci", "NNS", "focus"},

                // noun: irregular
                {"indices", "NNS", "index"},
                {"appendices", "NNS", "appendix"},
                {"wolves", "NNS", "wolf"},
                {"knives", "NNS", "knife"},
                {"quizzes", "NNS", "quiz"},
                {"mice", "NNS", "mouse"},
                {"geese", "NNS", "goose"},
                {"teeth", "NNS", "tooth"},
                {"feet", "NNS", "foot"},
                {"analyses", "NNS", "analysis"},
                {"optima", "NNS", "optimum"},
                {"lexica", "NNS", "lexicon"},
                {"corpora", "NNS", "corpus"},

                // adjective: comparative
                {"easier", "JJR", "easy"},
                {"smaller", "JJR", "small"},
                {"bigger", "JJR", "big"},
                {"larger", "JJR", "large"},

                // adjective: superative
                {"easiest", "JJS", "easy"},
                {"smallest", "JJS", "small"},
                {"biggest", "JJS", "big"},
                {"largest", "JJS", "large"},

                // adjective: irregular
                {"best", "JJS", "good"},

                // adverb: comparative
                {"earlier", "RBR", "early"},
                {"sooner", "RBR", "soon"},
                {"larger", "RBR", "large"},

                {"earliest", "RBS", "early"},
                {"soonest", "RBS", "soon"},
                {"largest", "RBS", "large"},

                // adverb: irregular
                {"best", "RBS", "well"},

                // URL
                {"http://www.google.com", "XX", MetaConst.HYPERLINK},
                {"www.google.com", "XX", MetaConst.HYPERLINK},
                {"mailto:somebody@google.com", "XX", MetaConst.HYPERLINK},
                {"some-body@google.com", "XX", MetaConst.HYPERLINK},

                // numbers
                {"10%", "XX", "0"},
                {"$10", "XX", "0"},
                {".01", "XX", "0"},
                {"12.34", "XX", "0"},
                {"12,34,56", "XX", "0"},
                {"12-34-56", "XX", "0"},
                {"12/34/46", "XX", "0"},
                {"A.01", "XX", "a.0"},
                {"A:01", "XX", "a:0"},
                {"A/01", "XX", "a/0"},
                {"$10.23,45:67-89/10%", "XX", "0"},

                // punctuation
                {".!?-*=~,", "XX", ".!?-*=~,"},
                {"..!!??--**==~~,,", "XX", "..!!??--**==~~,,"},
                {"...!!!???---***===~~~,,,", "XX", "..!!??--**==~~,,"},
                {"....!!!!????----****====~~~~,,,,", "XX", "..!!??--**==~~,,"}
        };

        Lemmatizer lemmatizer = new EnglishLemmatizer();
        String lemma;

        for (String[] token : tokens) {
            lemma = lemmatizer.getLemma(token[0], token[1]);
            assertEquals(token[2], lemma);
        }
    }
}