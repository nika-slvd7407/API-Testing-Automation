package com.solvd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.domain.UserRequest;
import com.solvd.domain.UserResponse;

import java.io.InputStream;

public class TemplateService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static UserRequest getUserRequestTemplate() {
        try (InputStream is = TemplateService.class
                .getClassLoader().getResourceAsStream("templates/user_request.json")) {

            return mapper.readValue(is, UserRequest.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserResponse getUserResponseTemplate() {
        try (InputStream is = TemplateService.class.getClassLoader().getResourceAsStream("templates/user_response.json")) {

            return mapper.readValue(is, UserResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}