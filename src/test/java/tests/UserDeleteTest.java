package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerater;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Delete cases")
@Feature("Delete")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("Test for deleting a non-deletable user")
    @DisplayName("Test negative delete user")
    public void testDeleteBlockedUserTest() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLogin("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Map<String, String> editData = new HashMap<>();

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2", this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("Test for successful user deletion")
    @DisplayName("Test positive delete user")
    public void testDeletePositiveTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerater.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData, "https://playground.learnqa.ru/api/user/");

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLogin("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Map<String, String> editData = new HashMap<>();

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetInDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Test
    @Description("Test for deleting someone else's user")
    @DisplayName("Test negative delete user")
    public void testDeleteAnotherUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerater.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData, "https://playground.learnqa.ru/api/user/");

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLogin("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Map<String, String> editData = new HashMap<>();

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }
}
