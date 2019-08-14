package testing;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.*;
import io.restassured.specification.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AppTest {

    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;

    @DataProvider(name="data")
    public Object[][] zipCodesAndPlaces(){
        return new Object[][] {
                { "us", "90210", "Beverly Hills"},
                { "us", "12345", "Schenectady"},
                { "ca", "B2R", "Waverley"},
                { "nl", "1001", "Amsterdam"}
        };
    }

    @BeforeClass
    public void createSpecification() {
        requestSpec = new RequestSpecBuilder().setBaseUri("http://api.zippopotam.us").build();
        responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
    }

    @Test(enabled = false)
    public void requestUSZipCode_checkBody_expectBeverlyHills() {
        given().
                when().
                get("http://zippopotam.us/us/90210").
                then().
                assertThat().
                body("places[0].'place name'", equalTo("Beverly Hills"));
    }

    @Test(enabled = false)
    public void requestUSZipCode_checkStatusCode_expectHTTP200() {
        given().
                when().
                get("http://zippopotam.us/us/90210").
                then().
                assertThat().
                statusCode(200);
    }

    @Test(enabled = false)
    public void requestUSZipCode_checkContentType_expectAppJson() {//it could be ANY, TEXT, JSON, XML, HTML, URLENC, BINARY
        given().
                when().
                get("http://zippopotam.us/us/90210").
                then().
                assertThat().
                contentType(ContentType.JSON);
    }

    @Test(enabled = false)
    public void requestUSZipCode_checkContentType_expectLogRequestAndResponseDetails() {//it could be ANY, TEXT, JSON, XML, HTML, URLENC, BINARY
        given().
                log().
                all().
                when().
                get("http://zippopotam.us/us/90210").
                then().
                log().
                body();
    }
    @Test(dataProvider = "data")
    public void requestZipCodes_checkPlaceNameInResponse_expectSpecificPlace(String country, String zipCode, String city) {
        given().
                spec(requestSpec).
                pathParam("countryCode", country).pathParam("zipCode", zipCode).
                when().
                get("{countryCode}/{zipCode}").
                then().
                spec(responseSpec).
                and().
                assertThat().
                body("places[0].'place name'", equalTo(city));
    }
}
