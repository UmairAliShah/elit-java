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
package cloud.elit.ddr.lemmatize.english;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import cloud.elit.ddr.lemmatize.util.AbstractAffixMatcher;
import cloud.elit.ddr.lemmatize.util.AbstractAffixReplacer;
import cloud.elit.ddr.util.Splitter;
import cloud.elit.ddr.util.StringConst;
import cloud.elit.ddr.util.XMLUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class EnglishAffixMatcherFactory {
    final String ELEM_AFFIX = "affix";
    final String ELEM_RULE = "rule";
    final String ATTR_TYPE = "type";
    final String ATTR_FORM = "form";
    final String ATTR_POS = "pos";
    final String ATTR_ORG_POS = "org_pos";
    final String ATTR_BASE_POS = "base_pos";
    final String ATTR_AFFIX_FORM = "affix_form";
    final String ATTR_REPLACEMENTS = "replacements";
    final String ATTR_DOUBLE_CONSONANTS = "doubleConsonants";
    final String VAL_SUFFIX = "suffix";

    public List<AbstractAffixMatcher> createAffixMatchers(Element eAffixes) {
        List<AbstractAffixMatcher> affixes = new ArrayList<>();
        NodeList list = eAffixes.getElementsByTagName(ELEM_AFFIX);
        int i, size = list.getLength();
        Element eAffix;

        for (i = 0; i < size; i++) {
            eAffix = (Element) list.item(i);
            affixes.add(createAffixMatcher(eAffix));
        }

        return affixes;
    }

    public AbstractAffixMatcher createAffixMatcher(Element eAffix) {
        String type = XMLUtils.getTrimmedAttribute(eAffix, ATTR_TYPE);
        String form = XMLUtils.getTrimmedAttribute(eAffix, ATTR_FORM);
        String pos = XMLUtils.getTrimmedAttribute(eAffix, ATTR_POS);
        String orgPOS = XMLUtils.getTrimmedAttribute(eAffix, ATTR_ORG_POS);
        Pattern oPOS = orgPOS.equals(StringConst.EMPTY) ? null : Pattern.compile("^(" + orgPOS + ")$");

        boolean bSuffix = type.equals(VAL_SUFFIX);
        AbstractAffixMatcher matcher;

        if (bSuffix) matcher = new EnglishSuffixMatcher(form, pos, oPOS);
        else throw new IllegalArgumentException("Invalid affix type: " + type);

        NodeList list = eAffix.getElementsByTagName(ELEM_RULE);
        AbstractAffixReplacer replacer;
        int i, size = list.getLength();

        for (i = 0; i < size; i++) {
            replacer = getAffixReplacer((Element) list.item(i));
            matcher.addReplacer(replacer);
        }

        return matcher;
    }

    /**
     * Called by {@link #createAffixMatcher(Element)}.
     */
    private AbstractAffixReplacer getAffixReplacer(Element eRule) {
        String basePOS = XMLUtils.getTrimmedAttribute(eRule, ATTR_BASE_POS);
        String affixForm = XMLUtils.getTrimmedAttribute(eRule, ATTR_AFFIX_FORM);
        String[] replacements = Splitter.splitCommas(XMLUtils.getTrimmedAttribute(eRule, ATTR_REPLACEMENTS), true);

        String dc = XMLUtils.getTrimmedAttribute(eRule, ATTR_DOUBLE_CONSONANTS);
        boolean doubleConsonants = !dc.equals(StringConst.EMPTY) && Boolean.parseBoolean(dc);

        return new EnglishSuffixReplacer(basePOS, affixForm, replacements, doubleConsonants);
    }
}
