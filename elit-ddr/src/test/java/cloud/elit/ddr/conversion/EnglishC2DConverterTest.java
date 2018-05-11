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

package cloud.elit.ddr.conversion;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.junit.Assert;
import org.junit.Test;
import cloud.elit.ddr.constituency.CTReader;
import cloud.elit.ddr.constituency.CTTree;
import cloud.elit.ddr.util.IOUtils;
import cloud.elit.sdk.structure.Document;
import cloud.elit.sdk.structure.Sentence;

public class EnglishC2DConverterTest {
    final String ROOT = "src/test/resources/conversion/english/";
    EnglishC2DConverter ddg = new EnglishC2DConverter();

    @Test
    public void test() {
        // subject
        test("nsbj");
        test("csbj");
        test("expl");

        // object
        test("obj");
        test("dat");
        test("comp");

        // auxiliary
        test("aux");
        test("cop");
        test("lv");
        test("modal");
        test("raise");

        // coordination
        test("conj");
        test("cc");

        // noun/quantifier phrase
        test("det");
        test("num");
        test("poss");
        test("attr");
        test("appo");
        test("acl");
        test("relcl");

        // adverbial
        test("neg");
        test("adv");
        test("advcl");
        test("advnp");
        test("ppmod");

        // particle
        test("prt");
        test("case");
        test("mark");

        // miscellaneous
        test("com");
        test("voc");
        test("disc");
        test("meta");
        test("prn");
        test("ref");
    }

    void test(String filename) {
        CTReader reader = new CTReader(IOUtils.createFileInputStream(ROOT + filename + ".parse"));
        Document doc = new Document();
        CTTree ctree;

        while ((ctree = reader.next()) != null) {
            Sentence dtree = ddg.toDependencyGraph(ctree);
            dtree.setNamedEntities(null);
            doc.add(dtree);
        }

        String actual = doc.toTSV();

        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(ROOT + filename
                    + ".tsv")));
            char[] buff = new char[actual.length()];
            fin.read(buff);
            fin.close();
            Assert.assertEquals(new String(buff), actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void print(String filename) {
        CTReader reader = new CTReader(IOUtils.createFileInputStream(ROOT + filename + ".parse"));
        Document doc = new Document();
        CTTree ctree;

        for (int i = 0; (ctree = reader.next()) != null; i++) {
            Sentence s = ddg.toDependencyGraph(ctree);
            s.setID(i);
            doc.add(s);
        }

        try {
            PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream(ROOT + filename
                    + ".json")));
            fout.println(doc.toString());
            fout.close();

            fout = new PrintStream(new BufferedOutputStream(new FileOutputStream(ROOT + filename + ".tsv")));
            fout.println(doc.toTSV());
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}