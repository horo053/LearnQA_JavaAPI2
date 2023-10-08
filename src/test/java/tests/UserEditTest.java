package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerater;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;

@Epic("Edit cases")
@Feature("Edit")
@Story("User change task №233")
public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Owner("Kris")
    @Description("Test for successful user change")
    @DisplayName("Test positive edit user. Test for successful user change")
    public void testEditJustCreatedTest(){
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

        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePostRequestEditUser("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGet("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Owner("Kris")
    @Description("Test for changing the username by one character")
    @DisplayName("Test negative edit user. Test for changing the username by one character")
    public void testEditJustCreatedTestOneSymbol(){
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

        //EDIT
        String newName = "C";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePostRequestEditUser("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Too short value for field firstName\"}");
    }

    @Test
    @Owner("Kris")
    @Description("Test for changing a user without authorization")
    @DisplayName("Test negative edit user. Test for changing a user without authorization")
    public void testEditJustCreatedNotAuth(){
        //GENERATE USER
        Map<String, String> userData = DataGenerater.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData, "https://playground.learnqa.ru/api/user/");

        String userId = responseCreateAuth.getString("id");

        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePostRequestEditUser("https://playground.learnqa.ru/api/user/" + userId, editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Owner("Kris")
    @Description("Test for changing user login without @")
    @DisplayName("Test negative edit user. Test for changing user login without @")
    public void testEditJustCreatedNotRightEmail(){
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

        //EDIT
        String newEmail = "testexemple.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests
                .makePostRequestEditUser("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }

    @Test
    @Owner("Kris")
    @Description("Test to edit another user")
    @DisplayName("Test negative edit user. Test to edit another user")
    public void testEditJustCreatedWithAnotherUser(){
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

        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePostRequestEditUser("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");
    }
}
