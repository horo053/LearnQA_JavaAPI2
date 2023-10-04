package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserDate = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasKey(responseUserDate, "username");
        Assertions.assertJsonHasNotKey(responseUserDate, "firstName");
        Assertions.assertJsonHasNotKey(responseUserDate, "lastName");
        Assertions.assertJsonHasNotKey(responseUserDate, "email");
    }
}
