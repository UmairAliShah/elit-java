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
package cloud.elit.sdk.collection.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class ObjectDoublePair<T extends Serializable> implements Serializable {
    public T o;
    public double d;

    public ObjectDoublePair(T o, double i) {
        set(o, i);
    }

    public void set(T o, double i) {
        this.o = o;
        this.d = i;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj instanceof ObjectDoublePair) {
            ObjectDoublePair p = (ObjectDoublePair) obj;
            return this.o.equals(p.o) && this.d == p.d;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(o, d);
    }
}