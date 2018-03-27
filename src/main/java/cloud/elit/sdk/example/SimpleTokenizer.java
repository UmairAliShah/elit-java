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

import cloud.elit.sdk.DecodeComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;

public class SimpleTokenizer extends DecodeComponent<String, String[], SimpleTokenizerParameters> {
    private Pattern DELIM;

    public SimpleTokenizer() {
        DELIM = Pattern.compile("\\s+");
    }

    @Override
    public void load(String model_path, SimpleTokenizerParameters params) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(model_path+"/"+params.getModelName()));
            String line;

            for (int i=0; (line = reader.readLine()) != null; i++) {
                if (i == params.getRegexID()) {
                    DELIM = Pattern.compile(line.trim());
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public String[] decode(String input, SimpleTokenizerParameters params) {
        return DELIM.split(input);
    }
}
