package APItests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTests {

    String apiAddress = "https://reqres.in/";

    final static int SUCCESS_CODE = 200;
    public static final int SUCCESS_201 = 201;
    public static final int SUCCESS_204 = 204;
    final static int NOT_FOUND_CODE = 404;



    @Description("Get list of users")
    @Test
    public void getListUsers() {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.baseUri(apiAddress);
        Response response = requestSpec.request(Method.GET, "api/users?page=1");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE);

        JsonPath jsonPath = response.jsonPath();
        String email = jsonPath.get("data[0].email");
        Assert.assertTrue("george.bluth@reqres.in".equalsIgnoreCase(email));
    }

    @Description("Get single user and check his or her name")
    @Test
    public void getSingleUser() {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.baseUri(apiAddress);
        Response response = requestSpec.request(Method.GET, "api/users/12");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE);

        JsonPath jsonPath = response.jsonPath();
        String first_name = jsonPath.get("data.first_name");
        Assert.assertTrue("Rachel".equalsIgnoreCase(first_name));
    }

    @Description("No results of search")
    @Test
    public void checkNotFoundResult() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(apiAddress);
        Response response = requestSpecification.request(Method.GET, "api/users/1232");
        Assert.assertEquals(response.getStatusCode(), NOT_FOUND_CODE);
    }

    @Description("Get colours list endpoint")
    @Test
    public void checkColoursList() {
        RequestSpecification specification = RestAssured.given();
        specification.baseUri(apiAddress);

        Response response = specification.request(Method.GET, "api/unknown");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE);

        JsonPath path = response.jsonPath();
        Assert.assertEquals(path.get("data[1].name"), "fuchsia rose");
    }

    @Description("Get single color")
    @Test
    public void checkSingleColor() {
        RequestSpecification specification = RestAssured.given();
        specification.baseUri(apiAddress);

        Response response = specification.request(Method.GET, "api/unknown/2");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE);

        JsonPath path = response.jsonPath();
        Assert.assertTrue(path.get("data.color").equals("#C74375"));
    }

    @Description("No results of color search")
    @Test
    public void checkNotFoundResultColor() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(apiAddress);
        Response response = requestSpecification.request(Method.GET, "api/unknown/222");
        Assert.assertEquals(response.getStatusCode(), NOT_FOUND_CODE);
    }

    @Description("Crate a new user")
    @Test
    public void createUser() {
        RequestSpecification request = RestAssured.given();
        request.baseUri(apiAddress);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "Olegka");
        jsonBody.put("job", "Slesar\'");
        request.body(jsonBody.toString());

        Response response = request.request(Method.POST, "api/users");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_201);
    }

    @Description("Update user information")
    @Test
    public void updateUserInfo(){
        RequestSpecification request = RestAssured.given();
        request.baseUri(apiAddress);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "Mishanja");
        jsonBody.put("job", "Boss");

        request.header(new Header("Content-Type","application/json"));
        request.body(jsonBody.toString());

        Response response = request.request(Method.PUT,"api/users/459");
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE);

        JsonPath path = response.jsonPath();
        String result = path.get("updatedAt");
        Assert.assertFalse(result.isEmpty());
    }

    @Description("Delete user information")
    @Test
    public void deleteUser(){
        RequestSpecification request = RestAssured.given();
        request.baseUri(apiAddress);

        Response response = request.request(Method.DELETE,"api/users/2");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_204);
    }

    @Description("Registration user account")
    @Test
    public void registrationUser(){
        RequestSpecification request = RestAssured.given();
        request.baseUri(apiAddress);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email","eve.holt@reqres.in");
        jsonBody.put("password","pistol");

        request.header(new Header("Content-Type","application/json"));
        request.body(jsonBody.toString());

        Response response = request.request(Method.POST,"api/register");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE, response.prettyPrint());

        JsonPath path = response.jsonPath();
        System.out.println("id" + path.get("id"));
        System.out.println("token" + path.get("token"));
    }









    @Test
    public void doRegister() {
        apiAddress += "doregister";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "LukLukOsPOs9");
        requestBody.put("email", "LukLukOsPOs9@ru.mail");
        requestBody.put("password", "LukLukOsPOs9");

        RequestSpecification request = getRequestSpecification();
        request.body(requestBody.toString());

        Response response = request.post(apiAddress);
        int status = response.statusCode();
        Assert.assertEquals(status, SUCCESS_CODE);
        System.out.println("Actual status code is  " + status);
    }

    @Test
    public void getUserFull() {
        apiAddress += "getuserfull?email=oly123@gmail.com";
        Response response;
        RequestSpecification request = getRequestSpecification();
        response = request.get(apiAddress);
        Assert.assertEquals(response.getStatusCode(), SUCCESS_CODE);

        JSONObject result = new JSONObject(response.body().asString());
        Assert.assertEquals(result.getString("name"), "LukLukOs");


    }

    private RequestSpecification getRequestSpecification() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
//        request.auth().basic(login,password);
        return request;
    }

//    @Test
//    public void deleteUser() {
//        apiAddress += "deleteuser";
//        RestAssured.baseURI = apiAddress;
//
//        RequestSpecification request = getRequestSpecification();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("email", "luklukospos9@ru.mail");
//
//        request.body(jsonObject.toString());
//        Response response = request.request(Method.DELETE);
//
//        int statusCode = response.getStatusCode();
//        Assert.assertTrue(statusCode == 200);
//
//
//    }


}
