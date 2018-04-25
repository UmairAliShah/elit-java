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

package cloud.elit.ddr.conversion;

import cloud.elit.ddr.constituency.CTReader;
import cloud.elit.ddr.constituency.CTTree;
import cloud.elit.ddr.util.IOUtils;
import cloud.elit.sdk.structure.Sentence;
import cloud.elit.sdk.structure.util.ELITUtils;
import org.junit.Test;

public class EnglishC2DConverterTest {
    final String ROOT = ELITUtils.getModulePath(".", "elit-ddr") + "/src/test/resources/conversion/english/";
    EnglishC2DConverter ddg = new EnglishC2DConverter();

    @Test
    public void testACL() {
        CTReader reader = new CTReader(IOUtils.createFileInputStream(ROOT+"acl.parse"));
        Sentence dtree;
        CTTree ctree;

        while ((ctree = reader.next()) != null) {
            dtree = ddg.toDependencyGraph(ctree);
            System.out.println(dtree.toCoNLL()+"\n");
        }
    }
}