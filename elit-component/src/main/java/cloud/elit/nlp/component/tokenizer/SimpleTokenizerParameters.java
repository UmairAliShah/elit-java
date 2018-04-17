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

import cloud.elit.sdk.component.Parameters;

public class SimpleTokenizerParameters implements Parameters {
    /**
     * The number of the line in the model file including the regular expression to be used for tokenization.
     */
    private int choice;

    public SimpleTokenizerParameters(int choice) {
        setChoice(choice);
    }

    public String getModelName() {
        /*
      The name of the model file where regular expressions are saved.
     */
        String model_name = "simple_tokenizer.txt";
        return model_name;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
