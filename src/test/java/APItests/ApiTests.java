package APItests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTests {

    String apiAddress = "https://reqres.in/api";

    final static int SUCCESS_200 = 200;
    final static int SUCCESS_201 = 201;
    final static int SUCCESS_204 = 204;
    final static int FAIL_404 = 404;
    final static int FAIL_400 = 400;


    @Description("Get list of users")
    @Test
    public void getListUsers() {
        RequestSpecification requestSpec = getRequestSpecification();
        Response response = requestSpec.request(Method.GET, "/users?page=1");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);

        JsonPath jsonPath = response.jsonPath();
        String email = jsonPath.get("data[0].email");
        Assert.assertTrue("george.bluth@reqres.in".equalsIgnoreCase(email));
    }

    @Description("Get single user and check his or her name")
    @Test
    public void getSingleUser() {
        RequestSpecification requestSpec = getRequestSpecification();
        Response response = requestSpec.request(Method.GET, "/users/12");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);

        JsonPath jsonPath = response.jsonPath();
        String first_name = jsonPath.get("data.first_name");
        Assert.assertTrue("Rachel".equalsIgnoreCase(first_name));
    }

    @Description("No results of search")
    @Test
    public void checkNotFoundResult() {
        RequestSpecification requestSpecification = getRequestSpecification();
        Response response = requestSpecification.request(Method.GET, "/users/1232");
        Assert.assertEquals(response.getStatusCode(), FAIL_404);
    }

    @Description("Get colours list endpoint")
    @Test
    public void checkColoursList() {
        RequestSpecification specification = getRequestSpecification();

        Response response = specification.request(Method.GET, "/unknown");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);

        JsonPath path = response.jsonPath();
        Assert.assertEquals(path.get("data[1].name"), "fuchsia rose");
    }

    @Description("Get single color")
    @Test
    public void checkSingleColor() {
        RequestSpecification specification = getRequestSpecification();

        Response response = specification.request(Method.GET, "/unknown/2");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);

        JsonPath path = response.jsonPath();
        Assert.assertTrue(path.get("data.color").equals("#C74375"));
    }

    @Description("No results of color search")
    @Test
    public void checkNotFoundResultColor() {
        RequestSpecification requestSpecification = getRequestSpecification();
        Response response = requestSpecification.request(Method.GET, "/unknown/222");
        Assert.assertEquals(response.getStatusCode(), FAIL_404);
    }

    @Description("Crate a new user")
    @Test
    public void createUser() {
        RequestSpecification specification = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "Olegka");
        jsonBody.put("job", "Slesar\'");
        specification.body(jsonBody.toString());

        Response response = specification.request(Method.POST, "/users");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_201);
    }

    @Description("Update user information")
    @Test
    public void updateUserInfo() {
        RequestSpecification request = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "Mishanja");
        jsonBody.put("job", "Boss");

        request.header(new Header("Content-Type", "application/json"));
        request.body(jsonBody.toString());

        Response response = request.request(Method.PUT, "/users/459");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200, response.body().asString());

        JsonPath path = response.jsonPath();
        String result = path.get("name");
        Assert.assertTrue(result.contains("Mishanja"));
    }

    @Description("Delete user information")
    @Test
    public void deleteUser() {
        RequestSpecification request = getRequestSpecification();

        Response response = request.request(Method.DELETE, "/users/2");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_204, response.body().asString());
    }

    @Description("Registration user account")
    @Test
    public void registrationUser() {
        RequestSpecification request = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "pistol");

        request.header(new Header("Content-Type", "application/json"));
        request.body(jsonBody.toString());

        Response response = request.request(Method.POST, "/register");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200, response.body().asString());

    }

    @Description("Unsuccessful registration check")
    @Test
    public void unsuccessfulRegistrationCheck() {
        RequestSpecification request = getRequestSpecification();
        request.header(new Header("Content-Type", "application/json"));


        JSONObject objectBody = new JSONObject();
        objectBody.put("email", "sydney@fife");
        request.body(objectBody.toString());

        Response response = request.request(Method.POST, "/register");
        Assert.assertEquals(response.getStatusCode(), FAIL_400, response.asString());
    }

    @Description("Login test")
    @Test
    public void loginCheck() {
        RequestSpecification specification = getRequestSpecification();
        specification.header(new Header("Content-Type", "application/json"));

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "cityslicka");

        specification.body(jsonBody.toString());

        Response response = specification.request(Method.POST, "/login");
        Assert.assertTrue(response.getStatusCode() == SUCCESS_200, "Login failed " + response.body().asString());
        Assert.assertTrue(response.jsonPath().get("token").equals("QpwL5tke4Pnpja7X4"), response.jsonPath().get("token"));
    }

    @Description("Failed login test")
    @Test
    public void failedLoginCheck() {
        RequestSpecification specification = getRequestSpecification();
        specification.header(new Header("Content-Type", "application/json"));

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", "eve.holt@reqres.in");

        specification.body(jsonBody.toString());

        Response response = specification.request(Method.POST, "/login");
        Assert.assertTrue(response.getStatusCode() == FAIL_400, "Login failed " + response.body().asString());
    }

    @Description("Delay 3 seconds check ")
    @Test
    public void checkDelayResults() {
        RequestSpecification specification = getRequestSpecification();

        Response response = specification.queryParam("delay", "3").request(Method.GET, "/users");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);

        int total_pages = response.jsonPath().get("total_pages");

        Assert.assertTrue(total_pages == 2, response.body().asString());
        Assert.assertTrue(response.time() > 3000, String.valueOf(response.time()));
    }

    private RequestSpecification getRequestSpecification() {
        RequestSpecification specification = RestAssured.given();
        specification.baseUri(apiAddress);
        return specification;
    }
}
