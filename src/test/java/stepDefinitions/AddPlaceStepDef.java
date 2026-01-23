package stepDefinitions;

import Utils.JsonPathUtils;
import contracts.APIResources;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.AddPlace_Location;
import pojo.ResponseAddPlace;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class AddPlaceStepDef {


    public static RequestSpecification reqSpecBuilder;
    public static ResponseSpecification resSpecBuilder;
    public static RequestSpecification request;
    public static Response response;

    @Given("User have valid request URI and Contract")
    public static void user_have_valid_request_uri_and_contract() throws FileNotFoundException {

        PrintStream stream = new PrintStream(new FileOutputStream("logger.txt"));

        reqSpecBuilder = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addFilter(RequestLoggingFilter.logRequestTo(stream))
                .addFilter(ResponseLoggingFilter.logResponseTo(stream))
                .addQueryParam("key", "qaclick123").build();

        resSpecBuilder = new ResponseSpecBuilder().expectStatusCode(200)
                .expectBody("scope",equalTo("APP"))
                .build();


        AddPlace addPlace = getAddPlace();

        request = given().spec(reqSpecBuilder).body(addPlace);



    }

    private static AddPlace getAddPlace() {
        AddPlace addPlace = new AddPlace();
        addPlace.setAccuracy(50);
        addPlace.setName("Google Head Office");
        addPlace.setPhone_number("9890989098");
        addPlace.setAddress("13, Silicon Valley");
        addPlace.setTypes(new String[]{"Tech", "Silicon"});
        addPlace.setWebsite("http://google.com");
        addPlace.setLanguage("English_India");
        AddPlace_Location addPlaceLocation = new AddPlace_Location();
        addPlaceLocation.setLat("-38.383494");
        addPlaceLocation.setLng("39.383494");
        addPlace.setLocation(addPlaceLocation);
        return addPlace;
    }

    @When("User hits {string} api using {string} http method")
    public static void user_hits_api_using_http_method(String resource, String method)  {

        APIResources resources = APIResources.valueOf(resource);

        if(method.equalsIgnoreCase("POST")) {
            response = request.when().post(resources.getResource())
                    .then().spec(resSpecBuilder).extract().response();
        }

    }
    @Then("User wil get status code {string}")
    public void user_wil_get_status_code(String statusCode) {

        String responseStatusCode = String.valueOf(response.statusCode());
        assertEquals(responseStatusCode,statusCode);

    }
    @Then("User should get {string} equal to {string} in response")
    public void user_should_get_equal_to_in_response(String key, String value) {

        String responseString = response.asString();


        ResponseAddPlace responseAddPlace = response.as(ResponseAddPlace.class);
        System.out.println(responseAddPlace.getPlace_id());
        System.out.println(key);

        String actualValue = JsonPathUtils.getJsonPathValue(response,key);
        System.out.println(actualValue);
        assertEquals(actualValue,value);

    }
}
