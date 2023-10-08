package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Get user cases")
@Feature("Get user")
public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("Getting a user without authentication")
    @DisplayName("Test negative get user. Getting a user without authentication")
    public void testGetUserDataNotAuth(){
        Response responseUserDate = apiCoreRequests
                .makeGetRequestNotAuth("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserDate, "username");
        Assertions.assertJsonHasNotField(responseUserDate, "firstName");
        Assertions.assertJsonHasNotField(responseUserDate, "lastName");
        Assertions.assertJsonHasNotField(responseUserDate, "email");
    }

    @Test
    @Description("Getting a user with authentication")
    @DisplayName("Test positive get user. Getting a user with authentication")
    public void testGetUserDetailsAuthAsSomeUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLogin("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserDate = apiCoreRequests
                .makeGet("https://playground.learnqa.ru/api/user/2", header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserDate, expectedFields);

        Assertions.assertJsonHasField(responseUserDate, "username");
        Assertions.assertJsonHasField(responseUserDate, "firstName");
        Assertions.assertJsonHasField(responseUserDate, "lastName");
        Assertions.assertJsonHasField(responseUserDate, "email");
    }

    @Test
    @Description("Getting another user's data")
    @DisplayName("Test negative get user. Getting another user's data")
    public void testGetUserDetailsAuthAnotherUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLogin("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserDate = apiCoreRequests
                .makeGet("https://playground.learnqa.ru/api/user/1", header, cookie);

        Assertions.assertJsonHasField(responseUserDate, "username");
        Assertions.assertJsonHasNotField(responseUserDate, "firstName");
        Assertions.assertJsonHasNotField(responseUserDate, "lastName");
        Assertions.assertJsonHasNotField(responseUserDate, "email");
    }
}
