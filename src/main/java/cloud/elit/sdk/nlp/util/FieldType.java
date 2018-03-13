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

package cloud.elit.sdk.nlp.util;

/**
 * Fields generated by NLP tools.
 */
public interface FieldType {
    /** Token. */
    String TOKEN = "token";
    /** Character-level offset of each token w.r.t. the original text. */
    String OFFSET = "offset";
    /** Lemma. */
    String LEMMA = "lemma";
    /** Part-of-speech tag. */
    String POS = "pos";
    /** Named entity tag. */
    String NER = "ner";
    /** Dependency relation. */
    String DEP = "dep";
    /** Coreference relation. */
    String COREF = "coref";
}
