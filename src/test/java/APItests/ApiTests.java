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
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.GET, "/users?page=1");

        JsonPath jsonPath = response.jsonPath();
        String email = jsonPath.get("data[0].email");

        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);
        Assert.assertTrue("george.bluth@reqres.in".equalsIgnoreCase(email));
    }

    @Description("Get single user and check his or her name")
    @Test
    public void getSingleUser() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.GET, "/users/12");

        String first_name = response.jsonPath().get("data.first_name");

        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);
        Assert.assertTrue("Rachel".equalsIgnoreCase(first_name));
    }

    @Description("No results of search")
    @Test
    public void checkNotFoundResult() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.GET, "/users/1232");
        Assert.assertEquals(response.getStatusCode(), FAIL_404);
    }

    @Description("Get colours list endpoint")
    @Test
    public void checkColoursList() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.GET, "/unknown");

        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);
        Assert.assertEquals(response.jsonPath().get("data[1].name"), "fuchsia rose");
    }

    @Description("Get single color")
    @Test
    public void checkSingleColor() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.GET, "/unknown/2");

        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);
        Assert.assertTrue(response.jsonPath().get("data.color").equals("#C74375"));
    }

    @Description("No results of color search")
    @Test
    public void checkNotFoundResultColor() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.GET, "/unknown/222");
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
        RequestSpecification specification = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "Mishanja");
        jsonBody.put("job", "Boss");

        specification.header(new Header("Content-Type", "application/json"));
        specification.body(jsonBody.toString());

        Response response = specification.request(Method.PUT, "/users/459");
        String result = response.jsonPath().get("name");

        Assert.assertTrue(result.contains("Mishanja"));
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200, response.body().asString());
    }

    @Description("Delete user information")
    @Test
    public void deleteUser() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.request(Method.DELETE, "/users/2");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_204, response.body().asString());
    }

    @Description("Registration user account")
    @Test
    public void registrationUser() {
        RequestSpecification specification = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "pistol");

        specification.header(new Header("Content-Type", "application/json"));
        specification.body(jsonBody.toString());

        Response response = specification.request(Method.POST, "/register");
        Assert.assertEquals(response.getStatusCode(), SUCCESS_200, response.body().asString());
    }

    @Description("Unsuccessful registration check")
    @Test
    public void unsuccessfulRegistrationCheck() {
        RequestSpecification specification = getRequestSpecification();

        JSONObject objectBody = new JSONObject();
        objectBody.put("email", "sydney@fife");

        specification.header(new Header("Content-Type", "application/json"));
        specification.body(objectBody.toString());

        Response response = specification.request(Method.POST, "/register");
        Assert.assertEquals(response.getStatusCode(), FAIL_400, response.asString());
    }

    @Description("Login test")
    @Test
    public void loginCheck() {
        RequestSpecification specification = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "cityslicka");

        specification.header(new Header("Content-Type", "application/json"));
        specification.body(jsonBody.toString());

        Response response = specification.request(Method.POST, "/login");
        Assert.assertTrue(response.getStatusCode() == SUCCESS_200, "Login failed " + response.body().asString());
        Assert.assertTrue(response.jsonPath().get("token").equals("QpwL5tke4Pnpja7X4"), response.jsonPath().get("token"));
    }

    @Description("Failed login test")
    @Test
    public void failedLoginCheck() {
        RequestSpecification specification = getRequestSpecification();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", "eve.holt@reqres.in");

        specification.header(new Header("Content-Type", "application/json"));
        specification.body(jsonBody.toString());

        Response response = specification.request(Method.POST, "/login");
        Assert.assertTrue(response.getStatusCode() == FAIL_400, "Login failed " + response.body().asString());
    }

    @Description("Delay 3 seconds check ")
    @Test
    public void checkDelayResults() {
        RequestSpecification specification = getRequestSpecification();
        Response response = specification.queryParam("delay", "3").request(Method.GET, "/users");

        int total_pages = response.jsonPath().get("total_pages");

        Assert.assertEquals(response.getStatusCode(), SUCCESS_200);
        Assert.assertTrue(total_pages == 2, response.body().asString());
        Assert.assertTrue(response.time() > 3000, String.valueOf(response.time()));
    }

    private RequestSpecification getRequestSpecification() {
        RequestSpecification specification = RestAssured.given();
        specification.baseUri(apiAddress);
        return specification;
    }
}
