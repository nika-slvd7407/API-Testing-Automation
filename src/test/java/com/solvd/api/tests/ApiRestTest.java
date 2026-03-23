package com.solvd.api.tests;

import com.solvd.domain.UserRequest;
import com.solvd.domain.UserResponse;
import com.solvd.service.TemplateService;
import com.solvd.testutil.EmailService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiRestTest extends BaseTest {

    @Test
    public void tc01_create_user_success() throws Exception {
        UserResponse response = createUser();
        UserResponse responceTemplate = TemplateService.getUserResponseTemplate();

        Assert.assertEquals(response.getName(), responceTemplate.getName());
        Assert.assertEquals(response.getGender(), responceTemplate.getGender());
        Assert.assertEquals(response.getStatus(), responceTemplate.getStatus());
        Assert.assertTrue(response.getEmail().contains("testuser"));
    }

    @Test
    public void tc02_get_user_success() throws Exception {
        UserResponse createdUser = createUser();
        int id =createdUser.getId();
        UserResponse response =  get(createdUser.getId());
        UserResponse responseTemplate = TemplateService.getUserResponseTemplate();

        Assert.assertEquals(response.getId(), id);
        Assert.assertEquals(response.getName(), responseTemplate.getName());
        Assert.assertEquals(response.getGender(), responseTemplate.getGender());
        Assert.assertEquals(response.getStatus(), responseTemplate.getStatus());
        Assert.assertTrue(response.getEmail().contains("testuser"));
    }

    @Test
    public void tc03_update_user_put() throws Exception {
        UserResponse created = createUser();
        int id = created.getId();

        UserRequest updateRequest = TemplateService.getUserRequestTemplate();
        updateRequest.setName("Updated Name");
        String email = EmailService.getRandomEmail();
        updateRequest.setEmail(email);

        UserResponse response = put(id, updateRequest);

        Assert.assertEquals(response.getId(), id);
        Assert.assertEquals(response.getName(), updateRequest.getName());
        Assert.assertEquals(response.getEmail(), updateRequest.getEmail());
    }

    @Test
    public void tc04_update_user_patch() throws Exception {
        UserResponse created = createUser();
        int id = created.getId();

        UserRequest patchRequest = new UserRequest();
        patchRequest.setStatus("inactive");

        UserResponse response = patch(id, patchRequest);

        Assert.assertEquals(response.getId(), id);
        Assert.assertEquals(response.getStatus(), patchRequest.getStatus());
    }

    @Test
    public void tc05_delete_user() throws Exception {
        UserResponse created = createUser();
        int id = created.getId();

        int statusCode = delete(id);
        Assert.assertEquals(statusCode, 204);

        UserResponse response = get(id);
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test
    public void tc06_get_users_list() throws Exception {
        UserResponse[] users = getUsersList(1, 10);
        Assert.assertTrue(users.length > 0);
    }

    @Test
    public void tc07_create_user_invalid_data() throws Exception {
        UserRequest invalid = TemplateService.getUserRequestTemplate();
        invalid.setEmail("invalid-email");

        UserResponse response = post(invalid);
        Assert.assertEquals(response.getStatusCode(), 422);
    }

    @Test
    public void tc08_unauthorized_request() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();
        request.setEmail(EmailService.getRandomEmail());

        int statusCode = postWithoutToken(request);
        Assert.assertEquals(statusCode, 401);
    }

    @Test
    public void tc09_method_not_allowed() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();

        int statusCode = putWithoutId(request);
        Assert.assertEquals(statusCode, 404);
    }

    @Test
    public void tc10_rate_limit_headers() throws Exception {
        CloseableHttpResponse response = getRawUsers();
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        Assert.assertNotNull(response.getFirstHeader("X-RateLimit-Limit"));
        Assert.assertNotNull(response.getFirstHeader("X-RateLimit-Remaining"));
        response.close();
    }



    private UserResponse createUser() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();
        request.setEmail("testuser" + Math.random() + "@mail.com");
        log.info(request.toString());
        return  post(request);
    }


}