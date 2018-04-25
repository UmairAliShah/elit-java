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

package cloud.elit.vsm.morphology;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class AffixRule implements Serializable, Comparable<AffixRule> {
    AffixType type;
    String source;
    String target;

    public AffixRule(AffixType type, String source, String target) {
        this.type = type;
        this.source = source;
        this.target = target;
    }

    public AffixType getType() {
        return type;
    }

    public void setType(AffixType type) {
        this.type = type;
    }

    public boolean isType(AffixType type) {
        return this.type == type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSource(String source) {
        return source.equals(this.source);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isTarget(String target) {
        return target.equals(this.target);
    }

    @Override
    public int compareTo(@NotNull AffixRule o) {
        int diff = this.type.compareTo(o.type);
        if (diff == 0) {
            diff = this.source.compareTo(o.source);
            if (diff == 0)
                diff = this.target.compareTo(o.target);
        }
        return diff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj instanceof AffixRule) {
            AffixRule rule = (AffixRule)obj;
            return isType(rule.type) && isSource(rule.source) && isTarget(rule.target);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, source, target);
    }

    @Override
    public String toString() {
        return "(\""+source+"\",\""+target+"\","+type.getDirection()+")";
    }
}
