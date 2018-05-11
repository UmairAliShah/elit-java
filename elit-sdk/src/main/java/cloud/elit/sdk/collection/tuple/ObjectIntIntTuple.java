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
import org.jetbrains.annotations.NotNull;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class ObjectIntIntTuple<T extends Comparable<T>> implements Serializable, Comparable<ObjectIntIntTuple<T>> {
    private static final long serialVersionUID = 6433419518317733243L;
    public T o;
    public int i1;
    public int i2;

    public ObjectIntIntTuple(T o, int i1, int i2) {
        set(o, i1, i2);
    }

    public void set(T o, int i1, int i2) {
        this.o = o;
        this.i1 = i1;
        this.i2 = i2;
    }

    @Override
    public int compareTo(@NotNull ObjectIntIntTuple<T> p) {
        int diff = this.o.compareTo(p.o);
        if (diff != 0) return diff;

        diff = this.i1 - p.i1;
        if (diff != 0) return diff;

        return this.i2 - p.i2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof ObjectIntIntTuple) {
            ObjectIntIntTuple<T> p = (ObjectIntIntTuple<T>) obj;
            return this.o.equals(p.o) && this.i1 == p.i1 && this.i2 == p.i2;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(o, i1, i2);
    }
}