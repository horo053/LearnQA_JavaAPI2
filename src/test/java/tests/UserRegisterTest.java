package tests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;

@Epic("Register user cases")
@Feature("Register user")
@Story("User registration task №231")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Owner("Kris")
    @Description("Registering a user with an existing email")
    @DisplayName("Test negative registering. Registering a user with an existing login")
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate = DataGenerater.getRegistrationData(userDate);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRegisterUser(userDate, "https://playground.learnqa.ru/api/user/");

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Owner("Kris")
    @Description("Registering user")
    @DisplayName("Test positive registering")
    public void testCreateUserSuccessfully(){
        String email = DataGenerater.getRandomEmail();

        Map<String, String> userDate = DataGenerater.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRegisterUser(userDate, "https://playground.learnqa.ru/api/user/");

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Owner("Kris")
    @Description("Registering user with non-correct email")
    @DisplayName("Test negative registering. Registering user with non-correct email")
    public void testCreateUserWithNotCorrectEmail(){
        String email = "vinkotovexample.com";

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate.put("password", "1234");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRegisterUser(userDate, "https://playground.learnqa.ru/api/user/");

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Test
    @Owner("Kris")
    @Description("Registering user with short name")
    @DisplayName("Test negative registering. Registering user with short name")
    public void testCreateUserWithShortName(){
        String email = "vinkotov@example.com";

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate.put("password", "1234");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "l");
        userDate.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRegisterUser(userDate, "https://playground.learnqa.ru/api/user/");

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    }

    @Test
    @Owner("Kris")
    @Description("Registering user with long name")
    @DisplayName("Test negative registering. Registering user with long name")
    public void testCreateUserWithLongName(){
        String email = "vinkotov@example.com";

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate.put("password", "1234");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
        userDate.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRegisterUser(userDate, "https://playground.learnqa.ru/api/user/");

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");
    }

    @Test
    @Owner("Kris")
    @Description("Registering user without param")
    @DisplayName("Test negative registering. Registering user without param")
    public void testCreateUserWithNotParam(){
        Map<String, String> userDate = new HashMap<>();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRegisterUser(userDate, "https://playground.learnqa.ru/api/user/");

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: email, password, username, firstName, lastName");
    }
}
