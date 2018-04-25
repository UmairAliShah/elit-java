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

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AffixRuleTest {
    @Test
    void testHash() {
        AffixRule r1 = new AffixRule(AffixType.SUFFIX, "s", "t");
        AffixRule r2 = new AffixRule(AffixType.SUFFIX, "s", "t");
        AffixRule r3 = new AffixRule(AffixType.PREFIX, "s", "t");
        AffixRule r4 = new AffixRule(AffixType.PREFIX, "s", "t");

        assertTrue(r1.equals(r2));
        assertTrue(r3.equals(r4));
        assertFalse(r1.equals(r3));

        Set<AffixRule> set = new HashSet<>();

        set.add(r1);
        set.add(r2);
        set.add(r3);
        set.add(r4);

        assertEquals(2, set.size());
        assertTrue(set.contains(r1));
        assertTrue(set.contains(r3));
    }
}