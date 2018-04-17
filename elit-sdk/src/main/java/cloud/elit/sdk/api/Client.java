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

    public String decode(TaskRequest request) {
        try {
            String s = mapper.writeValueAsString(request);
            return decode(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
