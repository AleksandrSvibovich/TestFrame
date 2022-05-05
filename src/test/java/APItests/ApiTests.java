package APItests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.is;

public class ApiTests {

    @Test
    public void postmanTest200(){
        RestAssured.
                when().get("https://postman-echo.com/get?test=123").
                then().assertThat().statusCode(200).
                and().body("args.test",is("123"));


    }
}
