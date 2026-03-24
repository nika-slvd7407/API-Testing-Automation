package com.solvd.api.tests;

import com.solvd.domain.ErrorResponse;
import com.solvd.domain.UserRequest;
import com.solvd.domain.UserResponse;
import com.solvd.service.TemplateService;
import com.solvd.testutil.EmailService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiRestTest extends BaseTest {

    @Test
    public void createUserWhenValidRequestReturnsCreatedUser() throws Exception {
        UserResponse response = createUser();
        UserResponse responseTemplate = TemplateService.getUserResponseTemplate();

        Assert.assertEquals(response.getName(), responseTemplate.getName(), "name mismatch");
        Assert.assertEquals(response.getGender(), responseTemplate.getGender(), "gender mismatch");
        Assert.assertEquals(response.getStatus(), responseTemplate.getStatus(), "status mismatch");
        Assert.assertTrue(response.getEmail().contains("testuser"), "email does not contain testuser");
    }

    @Test
    public void getUserWhenUserExistsReturnsUserDetails() throws Exception {
        UserResponse createdUser = createUser();
        int id = createdUser.getId();
        UserResponse response = get(createdUser.getId());
        UserResponse responseTemplate = TemplateService.getUserResponseTemplate();

        Assert.assertEquals(response.getId(), id, "id mismatch");
        Assert.assertEquals(response.getName(), responseTemplate.getName(), "name mismatch");
        Assert.assertEquals(response.getGender(), responseTemplate.getGender(), "gender mismatch");
        Assert.assertEquals(response.getStatus(), responseTemplate.getStatus(), "status mismatch");
        Assert.assertTrue(response.getEmail().contains("testuser"), "email does not contain testuser");
    }

    @Test
    public void updateUserWithPutWhenValidDataUpdatesUser() throws Exception {
        UserResponse created = createUser();
        int id = created.getId();

        UserRequest updateRequest = TemplateService.getUserRequestTemplate();
        updateRequest.setName("Updated Name");
        String email = EmailService.getRandomEmail();
        updateRequest.setEmail(email);

        UserResponse response = put(id, updateRequest);

        Assert.assertEquals(response.getId(), id, "id mismatch");
        Assert.assertEquals(response.getName(), updateRequest.getName(), "name not updated");
        Assert.assertEquals(response.getEmail(), updateRequest.getEmail(), "email not updated");
    }

    @Test
    public void updateUserWithPatchWhenPartialUpdateUpdatesSpecifiedFields() throws Exception {
        UserResponse created = createUser();
        int id = created.getId();

        UserRequest patchRequest = new UserRequest();
        patchRequest.setStatus("inactive");

        UserResponse response = patch(id, patchRequest);

        Assert.assertEquals(response.getId(), id, "id mismatch");
        Assert.assertEquals(response.getStatus(), patchRequest.getStatus(), "status not updated");
    }

    @Test
    public void deleteUserWhenUserExistsReturnsNoContentAndUserNotFoundAfter() throws Exception {
        UserResponse created = createUser();
        int id = created.getId();

        int statusCode = delete(id);
        Assert.assertEquals(statusCode, 204, "expected 204 status");

        UserResponse response = get(id);
        Assert.assertEquals(response.getStatusCode(), 404, "user still exists after delete");
    }

    @Test
    public void getUsersListWhenPageRequestedReturnsNonEmptyList() throws Exception {
        UserResponse[] users = getUsersList(1, 10);
        Assert.assertTrue(users.length > 0, "users list is empty");
    }

    @Test
    public void createUserWhenInvalidEmailReturnsValidationError() throws Exception {
        UserRequest invalid = TemplateService.getUserRequestTemplate();
        invalid.setEmail("");

        ErrorResponse response = postForErrors(invalid);

        Assert.assertEquals(response.getStatusCode(), 422, "expected 422 status");
        Assert.assertFalse(response.getErrors().isEmpty(), "error list is empty");

        boolean emailErrorFound = response.getErrors().stream()
                .anyMatch(error -> "email".equals(error.get("field")) &&
                        error.get("message").toString().toLowerCase().contains("can't be blank"));

        Assert.assertTrue(emailErrorFound, "email validation error not found");
    }

    @Test
    public void createUserWhenUnauthorizedReturnsUnauthorizedError() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();
        request.setEmail(EmailService.getRandomEmail());

        int statusCode = postWithoutToken(request);
        Assert.assertEquals(statusCode, 401, "expected unauthorized error");
    }

    @Test
    public void updateUserWithoutIdWhenPutRequestReturnsNotFound() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();

        int statusCode = putWithoutId(request);
        Assert.assertEquals(statusCode, 404, "expected not found error");
    }

    @Test
    public void getUsersWhenRequestMadeReturnsRateLimitHeaders() throws Exception {
        CloseableHttpResponse response = getRawUsers();
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "unexpected status code");
        Assert.assertNotNull(response.getFirstHeader("X-RateLimit-Limit"), "missing rate limit header");
        Assert.assertNotNull(response.getFirstHeader("X-RateLimit-Remaining"), "missing remaining header");
        response.close();
    }

    private UserResponse createUser() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();
        request.setEmail("testuser" + Math.random() + "@mail.com");
        log.info(request.toString());
        return post(request);
    }
}
