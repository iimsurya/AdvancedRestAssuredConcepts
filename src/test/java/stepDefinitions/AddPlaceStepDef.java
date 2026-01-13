package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class AddPlaceStepDef {


    public static RequestSpecification reqSpecBuilder;
    public static ResponseSpecification resSpecBuilder;
    public static Response response;

    @Given("User have valid request URI and Contract")
    public static void user_have_valid_request_uri_and_contract() throws FileNotFoundException {

        PrintStream stream = new PrintStream(new FileOutputStream("logger.txt"));

        reqSpecBuilder = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addFilter(RequestLoggingFilter.logRequestTo(stream))
                .addQueryParam("key", "qaclick123").build();

        resSpecBuilder = new ResponseSpecBuilder().expectStatusCode(200)
                .expectBody("scope",equalTo("APP"))
                .build();
    }

    @When("User hits add place api using POST http method")
    public static void user_hits_add_place_api_using_post_http_method() throws IOException {

         response = given().spec(reqSpecBuilder)
                .body(new String(Files.readAllBytes(Paths.get("/Users/suryav/IdeaProjects/AutomationPhase2-RestAssured/src/test/java/Data/addPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().spec(resSpecBuilder).extract().response();

    }
    @Then("User wil get status code {string}")
    public void user_wil_get_status_code(String statusCode) {

        String responseStatusCode = String.valueOf(response.statusCode());
        assertEquals(responseStatusCode,statusCode);

    }
    @Then("User should get {string} equal to {string} in respose")
    public void user_should_get_equal_to_in_respose(String key, String value) {

        String responseString = response.asString();

        System.out.println(key);
        JsonPath js = new JsonPath(responseString);
        String actualValue = js.get(key);
        System.out.println(actualValue);
        assertEquals(actualValue,value);

    }
}
