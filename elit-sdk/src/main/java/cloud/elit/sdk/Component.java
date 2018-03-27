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

/**
 * @param <I> the type of input data.
 * @param <O> the type of output data.
 * @param <P> the type of custom parameters.
 */
public abstract class Component<I, O, P extends Parameters> {
    /**
     * Loads a model from the specific directory to initialize this component.
     * @param model_path the path to the directory where the model is saved.
     * @param params custom parameters.
     */
    public abstract void load(String model_path, P params);

    /**
     * Decodes input data.
     * @param input the input data.
     * @param params custom parameters.
     * @return the output of the decoding.
     */
    public abstract O decode(I input, P params);

    /**
     * Saves a trained model to the specific directory for this component.
     * @param model_path the path to the directory where the model is saved.
     * @param params custom parameters.
     */
    public abstract void save(String model_path, P params);

    /**
     * Trains a model using the training and the development data.
     * It is also responsible to set this component with the final model so it can be saved by {@link #save(String, Parameters)}.
     * @param trn_data the training data (required).
     * @param dev_data the development data (optional).
     * @param params custom parameters.
     */
    public abstract void train(List<I> trn_data, List<I> dev_data, P params);

    /**
     * Calls {@link #load(String, Parameters)} with {@code params = null}.
     * @param model_path the path to the directory where the model is saved.
     */
    public void load(String model_path) {
        load(model_path, null);
    }

    /**
     * Calls {@link #decode(Object, Parameters)} with {@code params = null}.
     * @param input the input data.
     * @return the output of the decoding.
     */
    public O decode(I input) {
        return decode(input, null);
    }
}
