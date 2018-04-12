package cloud.elit.sdk.api;

import cloud.elit.sdk.nlp.structure.Document;
import cloud.elit.sdk.nlp.structure.Sentence;
import cloud.elit.sdk.nlp.structure.node.NLPNode;
import cloud.elit.sdk.nlp.structure.util.Fields;
import cloud.elit.sdk.nlp.structure.util.Tools;
import org.junit.Ignore;
import org.junit.Test;


public class DecodeWebAPITest {
    @Test
    @Ignore
    public void test() {
        Client api = new Client();

        String input = "Hello World! Welcome to ELIT.";
        TaskRequest r = new TaskRequest(input, Fields.ALL, Tools.SPACY);

        String output = api.decode(r);
        System.out.println(output);
        Document doc = new Document(output);
        System.out.println(doc);

        for (Sentence sen : doc) {
            for (NLPNode node : sen)
                System.out.println(String.format("%s(%s, %s)",
                        node.getDependencyLabel(),
                        node.getToken(),
                        node.getParent().getToken()));
            System.out.println();
        }


//        String input = "Hello World! Welcome to ELIT.";
//        TaskRequest r = new TaskRequest(input, TaskType.DEP, ToolType.NLP4J, new TaskDependency(TaskType.TOK, ToolType.ELIT), new TaskDependency(TaskType.POS, ToolType.SPACY));
//        String s = api.decode(r);
//        System.out.println(s);
//        Document doc = new Document(s);
//        System.out.println(doc);
    }
}