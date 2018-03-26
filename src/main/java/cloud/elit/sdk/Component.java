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

package cloud.elit.sdk;

import java.util.List;

public abstract class Component<I, O> {
    /**
     * Loads a model from the specific directory to initialize this component.
     * @param model_path the path to the directory where the model is saved.
     * @param params custom parameters.
     */
    public abstract <P extends Parameters>void load(String model_path, P params);

    /**
     * Saves a trained model to the specific directory for this component.
     * @param model_path the path to the directory where the model is saved.
     * @param params custom parameters.
     */
    public abstract <P extends Parameters>void save(String model_path, P params);

    /**
     * Decodes input data.
     * @param input the input data.
     * @param params custom parameters.
     * @return the output of the decoding.
     */
    public abstract <P extends Parameters>O decode(I input, P params);

    /**
     * Trains a model using the training and the development data.
     * @param trn_data the training data (required).
     * @param dev_data the development data (optional).
     * @param params custom parameters.
     */
    public abstract <P extends Parameters>void train(List<I> trn_data, List<I> dev_data, P params);

    /**
     * Evaluates predicted labels in the output of this component using the gold labels.
     * @param output the output of this component.
     * @param gold the gold labels.
     */
    public abstract <G, P extends Parameters>void eval(I output, G gold);
}
