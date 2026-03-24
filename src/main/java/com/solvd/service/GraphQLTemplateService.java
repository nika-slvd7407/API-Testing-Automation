package com.solvd.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GraphQLTemplateService {

    private GraphQLTemplateService() {}

    public static String getQuery(String fileName) {
        try (InputStream is = GraphQLTemplateService.class
                .getClassLoader()
                .getResourceAsStream("templates/graphql/" + fileName)) {
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCreateUserQuery(String name, String gender, String email, String status) {
        String template = getQuery("create_user_mutation.json");
        return String.format(template, name, gender, email, status);
    }

    public static String getDeleteUserQuery(int id) {
        return String.format(getQuery("delete_user_mutation.json"), id);
    }

    public static String getUpdateUserQuery(int id, String name, String email) {
        return String.format(getQuery("update_user_mutation.json"), id, name, email);
    }

    public static String getUserQuery(int id) {
        return String.format(getQuery("user_query.json"), id);
    }

    public static String getUsersQuery() {
        return getQuery("users_query.json");
    }
}