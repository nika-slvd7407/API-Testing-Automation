package com.solvd.api.tests;

import com.solvd.domain.UserRequest;
import com.solvd.domain.UserResponse;
import com.solvd.service.TemplateService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class ApiRestTest extends BaseTest {

    @Test
    public void tc01_create_user_success() throws Exception {
        UserRequest request = TemplateService.getUserRequestTemplate();
        request.setName("test user");
        request.setEmail("testuser" + Math.random() + "@mail.com");

        log.info(request.toString());

        UserResponse responce = post(request);

        log.info(responce.toString());

        Assert.assertEquals(responce.getName(), request.getName());
        Assert.assertEquals(responce.getGender(), request.getGender());
        Assert.assertEquals(responce.getStatus(), request.getStatus());
        Assert.assertTrue(responce.getEmail().contains("testuser"));
    }
}