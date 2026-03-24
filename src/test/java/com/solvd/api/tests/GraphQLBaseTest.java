package com.solvd.api.tests;

import com.solvd.testutil.RestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class GraphQLBaseTest {

    private static final String GRAPHQL_URL = RestService.getGraphUrl();
    private final ObjectMapper mapper = new ObjectMapper();

    protected JsonNode executeGraphQL(String body) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(GRAPHQL_URL);
            request.setHeader("Authorization", "Bearer " + RestService.getToken());
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            request.setEntity(new StringEntity(body));

            try (CloseableHttpResponse response = client.execute(request)) {
                return mapper.readTree(response.getEntity().getContent());
            }
        }
    }
}