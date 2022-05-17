package APItests;

import APIadditionalServices.Constants;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTestsPetStore {

    String baseURL = "https://petstore.swagger.io/";

    @Description("Create new user test")
    @Test
    public void createUserTest() {
        RequestSpecification specification = RestAssured.given();

        JSONObject objectBody = new JSONObject();
        objectBody.put("email", "test@pochta.com");
        objectBody.put("firstName", "Alejandro");
        objectBody.put("id", 1232132);
        objectBody.put("lastName", "Paskal");
        objectBody.put("password", "123qwe");
        objectBody.put("phone", "88005553525");
        objectBody.put("userStatus", 0);
        objectBody.put("username", "Krokodilus");

        specification.body(objectBody.toString());
        specification.baseUri(baseURL);
        specification.header(new Header("Content-Type", "application/json"));

        Response response = specification.request(Method.POST, "v2/user");

        Assert.assertEquals(response.getStatusCode(), Constants.SUCCESS_200);
        Assert.assertTrue(response.jsonPath().get("message").equals("1232132"), response.jsonPath().get("message"));
        Assert.assertTrue(response.jsonPath().get("type").equals("unknown"));
    }

    @Description("Get just created user by user name")
    @Test
    public void getUserDetails(){
        RequestSpecification request = RestAssured.given();
        request.baseUri(baseURL);
        request.header(new Header("Content-Type", "application/json"));

        Response response = request.request(Method.GET,"v2/user/Krokodilus");
        Assert.assertTrue(response.getStatusCode()==Constants.SUCCESS_200);
        Assert.assertTrue(response.jsonPath().get("username").equals("Krokodilus"));
    }

    @Description("Put changes in already existing user")
    @Test
    public void putChangesInUserDetails(){
        RequestSpecification request = RestAssured.given();
        request.baseUri(baseURL);
        request.header(new Header("Content-Type", "application/json"));

        JSONObject objectBody = new JSONObject();
        objectBody.put("email", "test@pochta.com");
        objectBody.put("firstName", "Alejandro");
        objectBody.put("id", 1232132);
        objectBody.put("lastName", "Paskal");
        objectBody.put("password", "123qwe");
        objectBody.put("phone", "88005553525");
        objectBody.put("userStatus", 0);
        objectBody.put("username", "Krokodilus");

        request.body(objectBody.toString());

        Response response = request.request(Method.PUT,"v2/user/Krokodilus");
        Assert.assertTrue(response.getStatusCode()==Constants.SUCCESS_200);
        Assert.assertTrue(response.jsonPath().get("message").equals("1232132"));
    }
}
