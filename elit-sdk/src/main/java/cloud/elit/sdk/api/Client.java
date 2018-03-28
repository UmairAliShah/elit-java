package cloud.elit.sdk.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Client {
    private ObjectMapper mapper;
    private HttpClient client;
    private HttpPost post;

    public Client() {
        mapper = new ObjectMapper();
        client = HttpClientBuilder.create().build();
        post = new HttpPost("https://elit.cloud/api/public/decode/");
    }

    public String decode(TaskRequest request) {

        try {
            String s = mapper.writeValueAsString(request);
            return decode(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decode(String request) {
        try {
            post.setEntity(new StringEntity(request, ContentType.create("application/json")));
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
