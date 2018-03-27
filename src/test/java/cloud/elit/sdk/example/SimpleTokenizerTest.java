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

package cloud.elit.sdk.example;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SimpleTokenizerTest {
    @Test
    public void test() {
        final String model_path = "src/test/resources/example";
        String s = "a b\tc d\ne";

        SimpleTokenizer tok = new SimpleTokenizer();
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, tok.decode(s));

        tok.load(model_path, new SimpleTokenizerParameters(0));
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, tok.decode(s));

        tok.load(model_path, new SimpleTokenizerParameters(1));
        assertArrayEquals(new String[]{"a b", "c d", "e"}, tok.decode(s));
    }
}