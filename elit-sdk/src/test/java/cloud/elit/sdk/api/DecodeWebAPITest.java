package cloud.elit.sdk.api;

import cloud.elit.sdk.nlp.structure.JSONField;
import org.junit.Ignore;
import org.junit.Test;
import cloud.elit.sdk.nlp.structure.Document;


public class DecodeWebAPITest {
    @Test
    @Ignore
    public void test()
    {
        Client api = new Client();

        String input = "Hello World! Welcome to ELIT.";
        TaskRequest r = new TaskRequest(input, JSONField.ALL, JSONField.SPACY);

        String s = api.decode(r);
        System.out.println(s);
        Document doc = new Document(s);
        System.out.println(doc);

//        String input = "Hello World! Welcome to ELIT.";
//        TaskRequest r = new TaskRequest(input, TaskType.DEP, ToolType.NLP4J, new TaskDependency(TaskType.TOK, ToolType.ELIT), new TaskDependency(TaskType.POS, ToolType.SPACY));
//        String s = api.decode(r);
//        System.out.println(s);
//        Document doc = new Document(s);
//        System.out.println(doc);
    }
}