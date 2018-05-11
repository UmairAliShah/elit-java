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

package cloud.elit.sdk.api;

import cloud.elit.sdk.structure.Document;
import cloud.elit.sdk.structure.Sentence;
import cloud.elit.sdk.structure.node.NLPNode;
import cloud.elit.sdk.structure.util.Fields;
import cloud.elit.sdk.structure.util.Tools;
import org.junit.Ignore;
import org.junit.Test;

public class DecodeWebAPITest {
    @Test
    @Ignore
    public void test() {
        Client api = new Client();

        String input = "Hello World! Welcome to ELIT.";
        TaskRequest r = new TaskRequest(input, Fields.ALL, Tools.SPACY);

        String output = api.decode(r);
        System.out.println(output);

        Document doc = new Document(output);
        System.out.println(doc);

        for (Sentence sen : doc) {
            for (NLPNode node : sen)
                System.out.println(String.format("%s(%s, %s)", node.getDependencyLabel(), node.getToken(), node
                        .getParent().getToken()));
            System.out.println();
        }

        r = new TaskRequest(input, Fields.DEP, Tools.NLP4J);
        r.setDependencies(new TaskDependency(Fields.TOK, Tools.ELIT), new TaskDependency(Fields.POS, Tools.NLP4J));
        String s = api.decode(r);
        System.out.println(s);
    }
}