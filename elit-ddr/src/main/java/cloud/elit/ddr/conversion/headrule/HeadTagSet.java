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
package cloud.elit.ddr.conversion.headrule;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import cloud.elit.ddr.constituency.CTNode;
import cloud.elit.ddr.util.PatternUtils;
import cloud.elit.ddr.util.StringConst;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HeadTagSet {
    /**
     * The delimiter between tags ({@code "|"}).
     */
    static final public String DELIM_TAGS = StringConst.PIPE;
    /**
     * The prefix of function tags ({@code '-'}).
     */
    static final public char PREFIX_FTAG = '-';

    static final private Pattern P_TAGS = Pattern.compile("\\|");

    /**
     * The regular expression of phrase/pos tags (e.g., {@code "^(NN.*|NP)$"}).
     */
    private final Pattern syntactic_tags;
    /**
     * The set of function tags.
     */
    private final Set<String> function_tags;

    /**
     * "NN.*|-SBJ|-TPC|NP".
     */
    public HeadTagSet(String tags) {
        StringBuilder pTags = new StringBuilder();
        function_tags = new HashSet<>();

        for (String tag : P_TAGS.split(tags)) {
            if (tag.charAt(0) == PREFIX_FTAG)
                function_tags.add(tag.substring(1));
            else {
                pTags.append(DELIM_TAGS);
                pTags.append(tag);
            }
        }

        syntactic_tags = (pTags.length() != 0) ? PatternUtils.createClosedPattern(pTags.substring(1)) : null;
    }

    /**
     * @return {@code true} if the specific node matches any of the tags.
     */
    public boolean matches(CTNode node) {
        return syntactic_tags != null && node.isSyntacticTag(syntactic_tags) || node.isFunctionTag(function_tags);
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();

        if (syntactic_tags != null) {
            String tags = syntactic_tags.pattern().substring(2);

            build.append(DELIM_TAGS);
            build.append(tags.substring(0, tags.length() - 2));
        }

        for (String fTag : function_tags) {
            build.append(DELIM_TAGS);
            build.append(PREFIX_FTAG);
            build.append(fTag);
        }

        return build.substring(DELIM_TAGS.length());
    }
}