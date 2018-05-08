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
package cloud.elit.ddr.bin;

import cloud.elit.ddr.constituency.CTNode;
import cloud.elit.ddr.util.*;
import cloud.elit.ddr.constituency.CTReader;
import cloud.elit.ddr.constituency.CTTree;
import cloud.elit.ddr.conversion.C2DConverter;
import cloud.elit.ddr.conversion.EnglishC2DConverter;
import cloud.elit.sdk.collection.tuple.ObjectIntIntTuple;
import cloud.elit.sdk.structure.Chunk;
import cloud.elit.sdk.structure.Document;
import cloud.elit.sdk.structure.Sentence;
import cloud.elit.sdk.structure.node.NLPNode;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DDGConvert {
    @Option(name = "-d", usage = "input path (required)", required = true, metaVar = "<filepath>")
    private String input_path;
    @Option(name = "-pe", usage = "parse file extension (default: parse)", metaVar = "<string>")
    private String parse_ext = "parse";
    @Option(name = "-oe", usage = "output file extension (default: tsv)", metaVar = "<string>")
    private String output_ext = "tsv";
    @Option(name = "-n", usage = "if set, normalize empty category indices", metaVar = "<boolean>")
    private boolean normalize = false;
    @Option(name = "-r", usage = "if set, traverse parse files recursively", metaVar = "<boolean>")
    private boolean recursive = false;

    public DDGConvert() {

    }

    public DDGConvert(String[] args) {
        BinUtils.initArgs(args, this);
        Language language = Language.ENGLISH;

        List<String> parseFiles = FileUtils.getFileList(input_path, parse_ext, recursive);
        C2DConverter converter = new EnglishC2DConverter();

        for (String parseFile : parseFiles) {
            int n = convert(converter, language, parseFile, parse_ext, output_ext, normalize);
            System.out.printf("%s: %d trees\n", parseFile, n);
        }
    }

    Int2ObjectMap<List<ObjectIntIntTuple<String>>> getNER(String parseFile) {
        final String nameFile = parseFile.substring(0, parseFile.length() - 5) + "name";
        Int2ObjectMap<List<ObjectIntIntTuple<String>>> map = new Int2ObjectOpenHashMap<>();
        File file = new File(nameFile);
        if (!file.exists()) return null;
        BufferedReader fin = IOUtils.createBufferedReader(file);
        String line;

        try {
            while ((line = fin.readLine()) != null) {
                List<ObjectIntIntTuple<String>> list = new ArrayList<>();
                String[] t = Splitter.splitSpace(line.trim());
                int tree_id = Integer.parseInt(t[1]);
                map.put(tree_id, list);

                for (int i = 2; i < t.length; i++) {
                    String[] s = Splitter.splitHyphens(t[i]);
                    int idx = s[0].indexOf(':');
                    list.add(new ObjectIntIntTuple<>(s[1], Integer.parseInt(s[0].substring(0, idx)), Integer.parseInt(s[0].substring(idx + 1))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    int convert(C2DConverter converter, Language language, String parseFile, String parseExt, String outputExt, boolean normalize) {
        CTReader reader = new CTReader(IOUtils.createFileInputStream(parseFile), language);
        Int2ObjectMap<List<ObjectIntIntTuple<String>>> ner_map = getNER(parseFile);
        Document doc = new Document();
        Sentence dTree;
        CTTree cTree;

        for (int n = 0; (cTree = reader.next()) != null; n++) {
            for (CTNode nn : cTree.getTokens()) {
                if (nn.isSyntacticTag("EMO")) nn.setSyntacticTag(PTBLib.P_NFP);
            }

            if (normalize) cTree.normalizeIndices();
            dTree = converter.toDependencyGraph(cTree);

            if (dTree == null)
                System.err.println("No token in the tree " + (n + 1) + "\n" + cTree.toStringLine());
            else {
                doc.add(dTree);

                if (ner_map == null)
                    dTree.setNamedEntities(null);
                else if (ner_map.containsKey(n)) {
                    List<Chunk> chunks = new ArrayList<>();

                    for (ObjectIntIntTuple<String> t : ner_map.get(n)) {
                        List<NLPNode> nodes = new ArrayList<>();

                        for (int tok_id = cTree.getTerminal(t.i1).getTokenID(); tok_id < cTree.getTerminal(t.i2).getTokenID() + 1; tok_id++)
                            nodes.add(dTree.get(tok_id));

                        chunks.add(new Chunk(nodes, t.o));
                    }

                    dTree.setNamedEntities(chunks);
                }
            }
        }

        reader.close();

        PrintStream fout = IOUtils.createBufferedPrintStream(parseFile + "." + outputExt);
        fout.println(outputExt.equalsIgnoreCase("tsv") ? doc.toTSV() : doc.toString());
        fout.close();
        return doc.size();
    }

    void convertEnglish() {
        final String[] dirs = {"bionlp", "bolt", "google", "mipacq", "ontonotes", "sharp", "thyme"};
        final String input_path = "/Users/jdchoi/Documents/Data/english/";
        C2DConverter converter = new EnglishC2DConverter();

        for (String dir : dirs) {
            List<String> parseFiles = FileUtils.getFileList(input_path + dir, "parse", true);
            boolean norm = dir.equals("bionlp") || dir.equals("bolt");

            for (String parseFile : parseFiles) {
                int n = convert(converter, Language.ENGLISH, parseFile, "parse", "tsv", norm);
                System.out.printf("%s: %d trees\n", parseFile, n);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new DDGConvert(args);
//            new DDGConvert().convertEnglish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}