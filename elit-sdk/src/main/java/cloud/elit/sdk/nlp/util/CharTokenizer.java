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
package cloud.elit.sdk.nlp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CharTokenizer
{
    private char delim;

    public CharTokenizer(char delim) {
        setDelimiter(delim);
    }

    public char getDelimiter() {
        return delim;
    }

    public void setDelimiter(char delim) {
        this.delim = delim;
    }

    public List<String> tokenize(String s) {
        return tokenize(s, false);
    }
    
    public List<String> tokenize(String s, boolean include_empty) {
        List<String> list = new ArrayList<>();
        int i, bIndex = 0, len = s.length();
        char[] cs = s.toCharArray();
        
        for (i=0; i<len; i++) {
            if (cs[i] == delim) {
                if      (bIndex < i)     list.add(s.substring(bIndex, i));
                else if (include_empty)  list.add("");
                bIndex = i + 1;
            }
        }
        
        if (list.isEmpty()) {
            list.add(s);
            return list;
        }
        
        if (bIndex < len)
            list.add(s.substring(bIndex));
        
        return list;
    }
    
    public String[] toArray(String s, boolean include_empty) {
        List<String> list = tokenize(s, include_empty);
        return list.toArray(new String[list.size()]);
    }
    
    /** Not including empty strings. */
    public String[] toArray(String s) {
        return toArray(s, false);
    }
}
