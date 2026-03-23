package com.solvd.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.domain.UserRequest;
import com.solvd.domain.UserResponse;
import com.solvd.testutil.RestService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    private final ObjectMapper mapper = new ObjectMapper();

    protected UserResponse post(UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(RestService.getBaseUrl());

        request.setHeader("Authorization", "Bearer " + RestService.getToken());
        request.setHeader("Content-Type", "application/json");

        String json = mapper.writeValueAsString(requestBody);
        request.setEntity(new StringEntity(json));

        CloseableHttpResponse response = client.execute(request);
        log.info(response.toString());
        UserResponse userResponse = mapper.readValue(response.getEntity().getContent(), UserResponse.class);

        response.close();
        client.close();

        return userResponse;
    }
}