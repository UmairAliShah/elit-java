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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class ObjectIntPair<T extends Comparable<T>> implements Serializable, Comparable<ObjectIntPair<T>> {

    public T o;
    public int i;

    public ObjectIntPair(T o, int i) {
        set(o, i);
    }

    public void set(T o, int i) {
        this.o = o;
        this.i = i;
    }

    @Override
    public int compareTo(@NotNull ObjectIntPair<T> p) {
        int diff = this.o.compareTo(p.o);
        return diff != 0 ? diff : this.i - p.i;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj instanceof ObjectIntPair) {
            ObjectIntPair p = (ObjectIntPair)obj;
            return this.o.equals(p.o) && this.i == p.i;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(o, i);
    }
}