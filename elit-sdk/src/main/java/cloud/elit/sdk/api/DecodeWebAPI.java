package cloud.elit.sdk.api;


import cloud.elit.sdk.nlp.structure.Document;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class DecodeWebAPI {
    private ObjectMapper mapper;
    private HttpClient client;
    private HttpPost post;

    public DecodeWebAPI() {
        mapper = new ObjectMapper();
        client = HttpClientBuilder.create().build();
        post = new HttpPost("https://elit.cloud/api/public/decode/");
    }

    public String decodeJSON(TaskRequest r) {
        try {
            String s = mapper.writeValueAsString(r);
            post.setEntity(new StringEntity(s, ContentType.create("application/json")));
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
