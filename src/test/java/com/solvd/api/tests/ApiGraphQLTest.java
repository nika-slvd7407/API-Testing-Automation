package com.solvd.api.tests;

import com.solvd.domain.UserGraphQL;
import com.solvd.testutil.RandomUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ApiGraphQLTest extends GraphQLBaseTest {

    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][]{
                {RandomUtil.randomName(), "male", RandomUtil.randomEmail(), "active"},
                {RandomUtil.randomName(), "female", RandomUtil.randomEmail(), "active"}
        };
    }

    @Test(dataProvider = "userData")
    public void createUserAndGetUser(String name, String gender, String email, String status) throws Exception {
        UserGraphQL newUser = new UserGraphQL(name, email, gender, status);
        JsonNode createResponse = executeGraphQL(UserGraphQL.createUserMutation(newUser));
        UserGraphQL createdUser = UserGraphQL.parseUser(createResponse.path("data").path("createUser").path("user"));
        newUser.setId(createdUser.getId());

        Assert.assertEquals(createdUser.getName(), name);
        Assert.assertEquals(createdUser.getEmail(), email);
        Assert.assertEquals(createdUser.getGender(), gender);
        Assert.assertEquals(createdUser.getStatus(), status);

        JsonNode getResponse = executeGraphQL(UserGraphQL.getUserQuery(newUser.getId()));
        UserGraphQL fetchedUser = UserGraphQL.parseUser(getResponse.path("data").path("user"));

        Assert.assertEquals(fetchedUser.getId(), newUser.getId());
        Assert.assertEquals(fetchedUser.getName(), name);
        Assert.assertEquals(fetchedUser.getEmail(), email);
    }

    @Test
    public void listUsersreturnsPageInfoAndNodes() throws Exception {
        JsonNode response = executeGraphQL(UserGraphQL.getUsersQuery());
        JsonNode users = response.path("data").path("users");

        Assert.assertTrue(users.has("nodes"));
        Assert.assertTrue(users.get("nodes").isArray());
        Assert.assertTrue(users.has("pageInfo"));
        Assert.assertTrue(users.path("pageInfo").has("endCursor"));
        Assert.assertTrue(users.path("pageInfo").has("startCursor"));
        Assert.assertTrue(users.path("pageInfo").has("hasNextPage"));
        Assert.assertTrue(users.path("pageInfo").has("hasPreviousPage"));
    }

    @Test
    public void updateUser() throws Exception {
        UserGraphQL newUser = new UserGraphQL(RandomUtil.randomName(), RandomUtil.randomEmail(), "male", "active");
        JsonNode createResponse = executeGraphQL(UserGraphQL.createUserMutation(newUser));
        UserGraphQL createdUser = UserGraphQL.parseUser(createResponse.path("data").path("createUser").path("user"));
        newUser.setId(createdUser.getId());

        newUser.setName("Updated_" + newUser.getName());
        newUser.setEmail(RandomUtil.randomEmail());

        JsonNode updateResponse = executeGraphQL(UserGraphQL.updateUserMutation(newUser));
        UserGraphQL updatedUser = UserGraphQL.parseUser(updateResponse.path("data").path("updateUser").path("user"));

        Assert.assertEquals(updatedUser.getName(), newUser.getName());
        Assert.assertEquals(updatedUser.getEmail(), newUser.getEmail());
    }

    @Test
    public void deleteUser() throws Exception {
        UserGraphQL newUser = new UserGraphQL(RandomUtil.randomName(), RandomUtil.randomEmail(), "male", "active");
        JsonNode createResponse = executeGraphQL(UserGraphQL.createUserMutation(newUser));
        UserGraphQL createdUser = UserGraphQL.parseUser(createResponse.path("data").path("createUser").path("user"));
        newUser.setId(createdUser.getId());

        executeGraphQL(UserGraphQL.deleteUserMutation(newUser.getId()));

        JsonNode deletedUserResponse = executeGraphQL(UserGraphQL.getUserQuery(newUser.getId()));
        Assert.assertTrue(deletedUserResponse.path("data").path("user").isNull());
    }
}