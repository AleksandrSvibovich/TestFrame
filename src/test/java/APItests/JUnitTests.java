package APItests;

import APIHelper.Constants;
import DTO.User;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JUnitTests {
    User dino = new User(123321, 0, "Krokodilus1",
            "Cayenne", "Dandy", "kroko@dandy.mi",
            "123", "88006667767");

    User vegetable = new User(1233213, 0, "Chesnochek",
            "Chester", "Chek", "chesnk@oche.mi",
            "123321", "88006667761");

    static String baseURL = "https://petstore.swagger.io/";
    static RequestSpecification specification;

    @BeforeAll
    static void setup() {
        specification = RestAssured.given();
        specification.baseUri(baseURL);
        specification.header(new Header("Content-Type", "application/json"));
    }

    @Order(1)
    @Tag("Pet")
    @DisplayName("Create new user for pet store")
    @Description("Create new user for pet store")
    @Test
    public void createUserTest() {
        JSONObject objectBody = new JSONObject();
        objectBody.put("email", dino.getEmail());
        objectBody.put("firstName", dino.getFirstName());
        objectBody.put("id", dino.getId());
        objectBody.put("lastName", dino.getLastName());
        objectBody.put("password", dino.getPassword());
        objectBody.put("phone", dino.getPhone());
        objectBody.put("userStatus", dino.getUserStatus());
        objectBody.put("username", dino.getUsername());

        specification.body(objectBody.toString());

        Response response = specification.request(Method.POST, "v2/user");

        Assertions.assertEquals(response.getStatusCode(), Constants.SUCCESS_200);
        Assertions.assertEquals(dino.getId(), Integer.parseInt(response.jsonPath().get("message")));
        Assertions.assertEquals(response.jsonPath().get("type"), "unknown");
    }

    @Order(2)
    @Description("Get just created user by user name")
    @DisplayName("Get just created user by user name")
    @Test
    public void getUserDetails() {
        Response response = specification.request(Method.GET, "v2/user/" + dino.getUsername());
        Assertions.assertEquals((int) Constants.SUCCESS_200, response.getStatusCode());
        Assertions.assertEquals(response.jsonPath().get("username"), dino.getUsername());
    }

    @Order(3)
    @Description("Put changes in already existing user")
    @DisplayName("Put changes in already existing user")
    @Test
    public void putChangesInUserDetails() {
        JSONObject body = new JSONObject();
        body.put("email", vegetable.getEmail());
        body.put("firstName", vegetable.getFirstName());
        body.put("id", vegetable.getId());
        body.put("lastName", vegetable.getLastName());
        body.put("password", vegetable.getPassword());
        body.put("phone", vegetable.getPhone());
        body.put("userStatus", vegetable.getUserStatus());
        body.put("username", vegetable.getUsername());

        specification.body(body.toString());

        Response response = specification.request(Method.PUT, "v2/user/" + dino.getUsername());
        Assertions.assertEquals((int) Constants.SUCCESS_200, response.getStatusCode());
        Assertions.assertEquals(Integer.parseInt(response.jsonPath().get("message")), vegetable.getId());

    }

    @Order(4)
    @Description("log in test with existing credentials")
    @DisplayName("log in test with existing credentials")
    @Test
    public void logInTest() {
        specification.queryParam("username", vegetable.getUsername());
        specification.queryParam("password", vegetable.getPassword());

        Response response = specification.request(Method.GET, "v2/user/login");
        String responseMessage = response.jsonPath().get("message");

        Assertions.assertEquals(response.getStatusCode(), Constants.SUCCESS_200);
        Assertions.assertTrue(responseMessage.contains("logged in user session"), "login failed" + response.toString());
    }

    @Order(5)
    @Description("logout test")
    @DisplayName("logout test")
    @Test
    public void logoutTest() {
        Response response = specification.request(Method.GET, "v2/user/logout");
        String responseMessage = response.jsonPath().get("message");

        Assertions.assertEquals(response.getStatusCode(), Constants.SUCCESS_200);
        Assertions.assertTrue(responseMessage.equalsIgnoreCase("ok"), "logout failed - " + response.toString());
    }

    @Order(1)
    @Description("Create user with list")
    @DisplayName("Create user with list")
    @Test
    public void createUserWithList() {
        JSONObject body = new JSONObject();
        body.put("email", vegetable.getEmail());
        body.put("firstName", vegetable.getFirstName());
        body.put("id", vegetable.getId());
        body.put("lastName", vegetable.getLastName());
        body.put("password", vegetable.getPassword());
        body.put("phone", vegetable.getPhone());
        body.put("userStatus", vegetable.getUserStatus());
        body.put("username", vegetable.getUsername());

        Object[] bodyArr = new Object[1];
        bodyArr[0] = body;
        specification.body(bodyArr);

        Response response = specification.request(Method.POST, "v2/user/createWithList");

        Assertions.assertEquals(response.getStatusCode(), Constants.SUCCESS_200, response.asString());

        String messageResponse = response.jsonPath().get("message");

        Assertions.assertTrue(messageResponse.equalsIgnoreCase("ok"),
                "user is not created - " + response.toString() + "body: " + body.toString());
    }

    @Order(1)
    @Description("Create user with array")
    @DisplayName("Create user with array")
    @Test
    public void createUserWithArray() {
        JSONObject body = new JSONObject();
        body.put("email", vegetable.getEmail());
        body.put("firstName", vegetable.getFirstName());
        body.put("id", vegetable.getId());
        body.put("lastName", vegetable.getLastName());
        body.put("password", vegetable.getPassword());
        body.put("phone", vegetable.getPhone());
        body.put("userStatus", vegetable.getUserStatus());
        body.put("username", vegetable.getUsername());

        Object[] bodyArr = new Object[1];
        bodyArr[0] = body;
        specification.body(bodyArr);

        Response response = specification.request(Method.POST, "v2/user/createWithArray");

        Assertions.assertEquals(response.getStatusCode(), Constants.SUCCESS_200, response.asString());
        String messageResponse = response.jsonPath().get("message");

        Assertions.assertTrue(messageResponse.equalsIgnoreCase("ok"), "user is not created - " + response.toString());
    }

    @Order(6)
    @Description("Delete user by user name")
    @DisplayName("Delete user by user name")
    @Test
    public void deleteUserByUserName() {
        Response response = specification.request(Method.DELETE, "v2/user/" + dino.getUsername());
        Response response2 = specification.request(Method.DELETE, "v2/user/" + vegetable.getUsername());
        Assertions.assertEquals(response.getStatusCode(), Constants.SUCCESS_200, response.toString());
        Assertions.assertEquals(response2.getStatusCode(), Constants.SUCCESS_200, response.toString());

    }
}
