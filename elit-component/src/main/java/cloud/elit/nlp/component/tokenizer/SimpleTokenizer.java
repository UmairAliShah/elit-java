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

package cloud.elit.nlp.component.tokenizer;

import cloud.elit.sdk.component.Tokenizer;
import cloud.elit.sdk.structure.Document;
import cloud.elit.sdk.structure.Sentence;
import cloud.elit.sdk.structure.util.NLPUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleTokenizer extends Tokenizer<SimpleTokenizerParameters> {
    private Pattern DELIM;

    public SimpleTokenizer() {
        DELIM = Pattern.compile("\\s+");
    }

    @Override
    public void load(String model_path, SimpleTokenizerParameters params) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(model_path + "/" + params.getModelName()));
            String line;

            for (int i = 0; (line = reader.readLine()) != null; i++) {
                if (i == params.getChoice()) {
                    DELIM = Pattern.compile(line.trim());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Document decode(String input, SimpleTokenizerParameters params) {
        String[] tokens = DELIM.split(input);
        Document document = new Document();
        int sen_id = 0, begin = 0;

        for (int end = 0; end < tokens.length; end++) {
            String token = tokens[end];
            if (token.endsWith(".") || token.endsWith("?") || token.endsWith("!")) {
                document.add(new Sentence(sen_id++, NLPUtils.toNLPNodes(tokens, begin, end + 1)));
                begin = end + 1;
            }
        }

        if (begin < tokens.length)
            document.add(new Sentence(NLPUtils.toNLPNodes(tokens, begin, tokens.length)));

        return document;
    }

    @Override
    public void save(String model_path, SimpleTokenizerParameters params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void train(List<String> trn_data, List<String> dev_data, SimpleTokenizerParameters params) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param begin inclusive.
     * @param end   exclusive.
     */
    private List<String> getSubList(String[] array, int begin, int end) {
        return Arrays.stream(array, begin, end).collect(Collectors.toList());
    }
}
