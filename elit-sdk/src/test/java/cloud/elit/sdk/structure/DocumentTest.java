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

package cloud.elit.sdk.structure;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void test() {
        String json = "{\"output\": [" +
                "{" +
                    "\"sid\": 0," +
                    "\"tok\": [\"Hello\", \"World\", \"!\"]," +
                    "\"off\": [[0, 5], [6, 11], [12, 13]]," +
                    "\"lem\": [\"hello\", \"world\", \"!\"]," +
                    "\"pos\": [\"UH\", \"NN\", \".\"]," +
                    "\"ner\": []," +
                    "\"dep\": [[1, \"intj\"], [-1, \"root\"], [1, \"punct\"]]" +
                "}," +
                "{" +
                    "\"sid\": 1," +
                    "\"tok\": [\"Welcome\", \"to\", \"ELIT\", \".\"]," +
                    "\"off\": [[0, 7], [8, 10], [11, 15], [16, 17]]," +
                    "\"lem\": [\"welcome\", \"to\", \"elit\", \".\"]," +
                    "\"pos\": [\"VBP\", \"IN\", \"NNP\", \".\"]," +
                    "\"ner\": [[2, 3, \"ORG\"]]," +
                    "\"dep\": [[-1, \"root\"], [2, \"aux\"], [0, \"xcomp\"], [0, \"punct\"]]" +
                "}]," +
                "\"pipeline\": \"tok\"}";

        Document doc = new Document(json);
        String s = doc.toString();

        doc = new Document("{\"output\":"+s+"}");
        assertEquals(s, doc.toString());
    }
}