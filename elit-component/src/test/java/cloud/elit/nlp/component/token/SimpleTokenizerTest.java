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

package cloud.elit.nlp.component.token;

import cloud.elit.sdk.nlp.structure.Document;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleTokenizerTest {
    @Test
    public void test() {
//      final String model_path = "elit-component/src/test/resources/token";
        final String model_path = "src/test/resources/token";
        String s = "a b\tc d\ne";

        SimpleTokenizer tok = new SimpleTokenizer();
        assertEquals(List.of("a", "b", "c", "d", "e"), tok.decode(s).get(0).getTokens());

        tok.load(model_path, new SimpleTokenizerParameters(0));
        assertEquals(List.of("a", "b", "c", "d", "e"), tok.decode(s).get(0).getTokens());

        tok = tok.factory(model_path, new SimpleTokenizerParameters(1));
        assertEquals(List.of("a b", "c d", "e"), tok.decode(s).get(0).getTokens());

        s = "a\tb.\nc\td.\ne";
        Document d = tok.decode(s);
        assertEquals(List.of("a", "b."), d.get(0).getTokens());
        assertEquals(List.of("c", "d."), d.get(1).getTokens());
        assertEquals(List.of("e")      , d.get(2).getTokens());
    }
}