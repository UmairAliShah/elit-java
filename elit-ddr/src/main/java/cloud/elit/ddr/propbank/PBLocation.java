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
package cloud.elit.ddr.propbank;

import java.io.Serializable;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import cloud.elit.ddr.util.PatternConst;
import cloud.elit.ddr.util.StringConst;
import cloud.elit.ddr.util.StringUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class PBLocation implements Serializable, Comparable<PBLocation> {
    private static final long serialVersionUID = -4360802182878074026L;
    static public final String DELIM = StringConst.COLON;
    static private final Pattern SPLIT = PatternConst.COLON;

    private int terminal_id;
    private int height;
    private String type;

    public PBLocation(PBLocation loc, String type) {
        set(loc, type);
    }

    public PBLocation(int terminalID, int height) {
        set(terminalID, height, StringConst.EMPTY);
    }

    public PBLocation(int terminalID, int height, String type) {
        set(terminalID, height, type);
    }

    /**
     * @param str  "terminalId:height".
     * @param type "star ampersand comma semicolon".
     */
    public PBLocation(String str, String type) {
        String[] loc = SPLIT.split(str);

        if (!StringUtils.containsDigitOnly(loc[0]) || !StringUtils.containsDigitOnly(loc[1]))
            throw new IllegalArgumentException(str);

        setTerminalID(Integer.parseInt(loc[0]));
        setHeight(Integer.parseInt(loc[1]));
        setType(type);
    }

    public int getTerminalID() {
        return terminal_id;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    public void setTerminalID(int id) {
        terminal_id = id;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void set(int id, int height) {
        setTerminalID(terminal_id);
        setHeight(height);
    }

    public void set(int terminal_id, int height, String type) {
        setTerminalID(terminal_id);
        setHeight(height);
        setType(type);
    }

    public void set(PBLocation loc, String type) {
        set(loc.getTerminalID(), loc.getHeight(), type);
    }

    public void setLocation(PBLocation location) {
        terminal_id = location.terminal_id;
        height = location.height;
        type = location.type;
    }

    public boolean matches(int terminal_id, int height) {
        return isTerminalID(terminal_id) && isHeight(height);
    }

    public boolean isTerminalID(int id) {
        return terminal_id == id;
    }

    public boolean isHeight(int height) {
        return this.height == height;
    }

    public boolean isType(String type) {
        return this.type.equals(type);
    }

    @Override
    public String toString() {
        return type + terminal_id + DELIM + height;
    }

    @Override
    public int compareTo(@NotNull PBLocation loc) {
        return terminal_id == loc.terminal_id ? height - loc.height : terminal_id - loc.terminal_id;
    }
}