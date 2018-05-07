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
package cloud.elit.ddr.propbank.frameset;

import cloud.elit.ddr.util.PBLib;
import cloud.elit.ddr.util.StringConst;
import cloud.elit.ddr.util.StringUtils;
import cloud.elit.ddr.util.XMLUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.*;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class PBFRoleset implements Serializable, Comparable<PBFRoleset> {
    private static final long serialVersionUID = 5776067696903134630L;

    private Map<String, PBFRole> m_roles;
    private Set<String> s_vncls;
    private String s_name;
    private String s_id;

    public PBFRoleset(Element eRoleset) {
        init(eRoleset);
    }

    private void init(Element eRoleset) {
        s_vncls = new HashSet<>();
        m_roles = new HashMap<>();

        setID(XMLUtils.getTrimmedAttribute(eRoleset, PBFXml.A_ID));
        setName(XMLUtils.getTrimmedAttribute(eRoleset, PBFXml.A_NAME));
        addVerbNetClasses(XMLUtils.getTrimmedAttribute(eRoleset, PBFXml.A_VNCLS));
        initRoles(eRoleset.getElementsByTagName(PBFXml.E_ROLE));
    }

    private void addVerbNetClasses(String classes) {
        if (!classes.equals(StringConst.EMPTY) && !classes.equals(StringConst.HYPHEN)) {
            for (String vncls : classes.split(StringConst.SPACE))
                addVerbNetClass(vncls);
        }
    }

    private void initRoles(NodeList list) {
        int i, size = list.getLength();

        for (i = 0; i < size; i++)
            initRole((Element) list.item(i));
    }

    private void initRole(Element eRole) {
        addRole(new PBFRole(eRole));
    }

    public Set<String> getVerbNetClasseSet() {
        return s_vncls;
    }

    public Collection<PBFRole> getRoles() {
        return m_roles.values();
    }

    /**
     * @param argNumber e.g., {@code "0"}, {@code "2"}.
     */
    public PBFRole getRole(String argNumber) {
        return m_roles.get(argNumber);
    }

    public String getFunctionTag(String argNumber) {
        PBFRole role = getRole(argNumber);
        return (role != null) ? role.getFunctionTag() : StringConst.EMPTY;
    }

    public String getID() {
        return s_id;
    }

    public String getName() {
        return s_name;
    }

    public void addVerbNetClass(String vncls) {
        if (vncls.length() > 1)
            s_vncls.add(PBLib.stripVerbNetClassName(vncls));
    }

    public void addRole(PBFRole role) {
        if (!isValidAnnotation(role)) {
            if (!PBLib.isLightVerbRoleset(s_id))
                System.err.println("Invalid argument: " + s_id + " - " + role.getArgKey());
        } else {
            m_roles.put(role.getArgumentNumber(), role);

//			for (String vncls : role.getVNClasseSet())
//			{
//				if (!s_vncls.contains(vncls))
//					System.err.printf("VerbNet class mismatch: %s - %s (%s ^ %s)\n", s_id, role.getArgKey(), vncls, s_vncls.toString());
//			}
        }
    }

    private boolean isValidAnnotation(PBFRole role) {
        String n = role.getArgumentNumber();
        return n.length() == 1 && (StringUtils.containsDigitOnly(n) || role.isArgumentNumber("A") || role.isArgumentNumber("M") && !role.isFunctionTag(StringConst.EMPTY));
    }

    public void setID(String id) {
        s_id = id;
    }

    public void setName(String name) {
        s_name = name;
    }

    public boolean isValidArgumentNumber(String number) {
        return m_roles.containsKey(number);
    }

    public boolean isValidArgument(String label) {
        // TODO: to be removed
        if (PBLib.isLightVerbRoleset(s_id))
            return true;

        String n = PBLib.getNumber(label);
        return n == null || m_roles.containsKey(n);
    }

    @Override
    public String toString() {
        List<PBFRole> list = new ArrayList<>(getRoles());
        StringBuilder build = new StringBuilder();
        Collections.sort(list);

        build.append(s_id);
        build.append(": ");
        build.append(s_name);

        for (String vncls : s_vncls) {
            build.append(", ");
            build.append(vncls);
        }

        build.append(StringConst.NEW_LINE);

        for (PBFRole role : list) {
            build.append(role.toString());
            build.append(StringConst.NEW_LINE);
        }

        return build.toString();
    }

    @Override
    public int compareTo(@NotNull PBFRoleset roleset) {
        return s_id.compareTo(roleset.s_id);
    }
}