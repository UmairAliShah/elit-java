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

package cloud.elit.sdk.component;

import java.util.List;

/**
 * This abstract class is inherited by components used for decoding, but not for training.
 */
public abstract class DecodeComponent<I, O, P extends Parameters> extends Component<I, O, P> {
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String model_path, P params) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void train(List<I> trn_data, List<I> dev_data, P params) {
        throw new UnsupportedOperationException();
    }
}
