package com.solvd.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.domain.ErrorResponse;
import com.solvd.domain.UserRequest;
import com.solvd.domain.UserResponse;
import com.solvd.testutil.RestService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    private static final String BASE_URL = RestService.getBaseUrl();
    private final ObjectMapper mapper = new ObjectMapper();

    protected UserResponse post(UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = createRequest(new HttpPost(BASE_URL));
        String json = mapper.writeValueAsString(requestBody);
        request.setEntity(new StringEntity(json));
         log.info(request.toString());
        CloseableHttpResponse response = client.execute(request);
        log.info("response: {}", response.toString());
        UserResponse userResponse = parseResponse(response, UserResponse.class);

        response.close();
        client.close();
        return userResponse;
    }

    protected UserResponse get(int id) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = createRequest(new HttpGet(BASE_URL + "/" + id));
        CloseableHttpResponse response = client.execute(request);

        log.info("response: {}", response.toString());
        UserResponse userResponse = parseResponse(response, UserResponse.class);

        response.close();
        client.close();
        return userResponse;
    }

    protected <T extends HttpRequestBase> T createRequest(T request) {
        request.setHeader("Authorization", "Bearer " + RestService.getToken());
        request.setHeader("Content-Type", "application/json");
        return request;
    }

    protected <T extends UserResponse> T parseResponse(CloseableHttpResponse response, Class<T> clazz) throws IOException {
        T obj = mapper.readValue(response.getEntity().getContent(), clazz);
        obj.setStatusCode(response.getStatusLine().getStatusCode());
        return obj;
    }

    protected UserResponse put(int id, UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut request = createRequest(new HttpPut(BASE_URL + "/" + id));
        request.setEntity(new StringEntity(mapper.writeValueAsString(requestBody)));
        CloseableHttpResponse response = client.execute(request);
        UserResponse userResponse = parseResponse(response, UserResponse.class);
        response.close();
        client.close();
        return userResponse;
    }

    protected UserResponse patch(int id, UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPatch request = createRequest(new HttpPatch(BASE_URL + "/" + id));
        request.setEntity(new StringEntity(mapper.writeValueAsString(requestBody)));
        CloseableHttpResponse response = client.execute(request);
        UserResponse userResponse = parseResponse(response, UserResponse.class);
        response.close();
        client.close();
        return userResponse;
    }

    protected int delete(int id) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete request = createRequest(new HttpDelete(BASE_URL + "/" + id));
        CloseableHttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        client.close();
        return statusCode;
    }

    protected UserResponse[] getUsersList(int page, int perPage) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = createRequest(new HttpGet(BASE_URL + "?page=" + page + "&per_page=" + perPage));
        CloseableHttpResponse response = client.execute(request);
        UserResponse[] users = mapper.readValue(response.getEntity().getContent(), UserResponse[].class);
        response.close();
        client.close();
        return users;
    }

    protected int postWithoutToken(UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(mapper.writeValueAsString(requestBody)));
        CloseableHttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        client.close();
        return statusCode;
    }

    protected int putWithoutId(UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut(BASE_URL);
        request.setHeader("Authorization", "Bearer " + RestService.getToken());
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(mapper.writeValueAsString(requestBody)));
        CloseableHttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        client.close();
        return statusCode;
    }

    protected CloseableHttpResponse getRawUsers() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = createRequest(new HttpGet(BASE_URL));
        return client.execute(request);
    }

    protected ErrorResponse postForErrors(UserRequest requestBody) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = createRequest(new HttpPost(BASE_URL));
        request.setEntity(new StringEntity(mapper.writeValueAsString(requestBody)));

        CloseableHttpResponse response = client.execute(request);

        ErrorResponse result = new ErrorResponse();
        result.setStatusCode(response.getStatusLine().getStatusCode());
        result.setErrors(mapper.readValue(
                response.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(List.class, Map.class)));

        response.close();
        client.close();
        return result;
    }

}