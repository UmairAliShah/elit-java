package cloud.elit.sdk.api;

import cloud.elit.sdk.nlp.util.ToolType;
import cloud.elit.sdk.nlp.util.TaskType;
import org.junit.Test;


public class DecodeWebAPITest {
    @Test
    public void test()
    {
        Client api = new Client();

        String input = "Hello World! Welcome to ELIT.";
        TaskRequest r = new TaskRequest(input, TaskType.ALL, ToolType.SPACY);
        System.out.println(api.decode(r));

        input = "Hello World! Welcome to ELIT.";
        r = new TaskRequest(input, TaskType.DEP, ToolType.NLP4J, new TaskDependency(TaskType.TOK, ToolType.ELIT), new TaskDependency(TaskType.POS, ToolType.SPACY));
        System.out.println(api.decode(r));
    }
}