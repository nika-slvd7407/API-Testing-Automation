package com.solvd.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.solvd.service.GraphQLTemplateService;

public class UserGraphQL {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;

    public UserGraphQL(int id, String name, String email, String gender, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }

    public UserGraphQL(String name, String email, String gender, String status) {
        this(0, name, email, gender, status);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setGender(String gender) { this.gender = gender; }
    public void setStatus(String status) { this.status = status; }

    public static String createUserMutation(UserGraphQL user) {
        return GraphQLTemplateService.getCreateUserQuery(
                user.getName(), user.getGender(), user.getEmail(), user.getStatus());
    }

    public static String deleteUserMutation(int userId) {
        return GraphQLTemplateService.getDeleteUserQuery(userId);
    }

    public static String updateUserMutation(UserGraphQL user) {
        return GraphQLTemplateService.getUpdateUserQuery(user.getId(), user.getName(), user.getEmail());
    }

    public static String getUserQuery(int userId) {
        return GraphQLTemplateService.getUserQuery(userId);
    }

    public static String getUsersQuery() {
        return GraphQLTemplateService.getUsersQuery();
    }

    public static UserGraphQL parseUser(JsonNode userNode) {
        if (userNode == null || userNode.isNull()) return null;
        return new UserGraphQL(
                userNode.path("id").asInt(),
                userNode.path("name").asText(),
                userNode.path("email").asText(),
                userNode.path("gender").asText(),
                userNode.path("status").asText()
        );
    }
}