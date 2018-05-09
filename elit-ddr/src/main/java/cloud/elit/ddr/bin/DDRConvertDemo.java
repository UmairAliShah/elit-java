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

package cloud.elit.ddr.bin;

import cloud.elit.ddr.constituency.CTTree;
import cloud.elit.ddr.conversion.C2DConverter;
import cloud.elit.ddr.conversion.EnglishC2DConverter;
import cloud.elit.ddr.util.Language;
import cloud.elit.sdk.structure.Document;
import cloud.elit.sdk.structure.Sentence;

public class DDRConvertDemo {
    public static void main(String[] args) {
        final String parseFile = "/Users/jdchoi/workspace/elit-java/relcl.parse";
        final String tsvFile = "/Users/jdchoi/workspace/elit-java/relcl.tsv";
        C2DConverter converter = new EnglishC2DConverter();
        DDRConvert ddr = new DDRConvert();
        ddr.convert(converter, Language.ENGLISH, parseFile, tsvFile, false);
    }
}
