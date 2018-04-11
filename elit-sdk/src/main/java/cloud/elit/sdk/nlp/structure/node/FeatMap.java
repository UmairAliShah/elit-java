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

package cloud.elit.sdk.nlp.structure.node;

import cloud.elit.sdk.nlp.util.CharTokenizer;
import java.util.HashMap;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeatMap extends HashMap<String,String> {
    /** The delimiter between feature values ({@code ','}). */
    public static final char DELIM_VALUES = ',';
    /** The delimiter between features ({@code '|'}). */
    public static final char DELIM_FEAT = '|';
    /** The delimiter between keys and values ({@code '='}). */
    public static final char DELIM_KEY_VALUE = '=';

    private static CharTokenizer tok_feat = new CharTokenizer(DELIM_FEAT);

    public FeatMap() {
        super();
    }
    
    public FeatMap(String feats) {
        super();
        add(feats);
    }
    
    /**
     * Adds the specific features to this map.
     * @param feats {@code "_"} or {@code feat(|feat)*}.<br>
     * {@code "_"}: indicates no feature.<br>
     * {@code feat ::= key=value} (e.g., {@code pos=VBD}).
     */
    public void add(String feats) {
        if (feats == null) return;
        String key, value;
        int    idx;
        
        for (String feat : tok_feat.tokenize(feats)) {
            idx = feat.indexOf(DELIM_KEY_VALUE);
            
            if (idx > 0) {
                key   = feat.substring(0, idx);
                value = feat.substring(idx+1);
                put(key, value);
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty())    return "_";
        StringBuilder build = new StringBuilder();
        
        for (Entry<String, String> entry : entrySet()) {
            build.append(DELIM_FEAT);
            build.append(entry.getKey());
            build.append(DELIM_KEY_VALUE);
            build.append(entry.getValue());
        }
        
        return build.toString().substring(1);
    }
}
