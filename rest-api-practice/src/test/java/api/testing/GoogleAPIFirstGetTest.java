package api.testing;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.minidev.json.JSONArray;
import com.atmecs.utils.common.ReusableFunctions;
import com.atmecs.utils.parser.PropertiesParsers;;

public class GoogleAPIFirstGetTest {

	ReusableFunctions cf = new ReusableFunctions();
	PropertiesParsers pp = new PropertiesParsers();
	SoftAssert softAssert = new SoftAssert();


	@Test
	public void TestCase2() {
		Response response = given().param("q", "potter").when().get("https://www.googleapis.com/books/v1/volumes")
				.then().statusCode(200).extract().response();

		int totalItems = JsonPath.read(response.asString(), "$.totalItems");

		Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

		List<Object> allBooks = JsonPath.read(response.asString(), "$.items");

		Assert.assertEquals(allBooks.size(), 10, "10 Books should be returned by default");
	}

	@Test
	public void TestCase3() {
		Response response = given().param("q", "dan+brown").param("maxResults", "25").when()
				.get("https://www.googleapis.com/books/v1/volumes").then().statusCode(200).extract().response();

		int totalItems = JsonPath.read(response.asString(), "$.totalItems");

		Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

		List<Object> allBooks = JsonPath.read(response.asString(), "$.items");

		Assert.assertEquals(allBooks.size(), 25, "25 Books should be returned");
	}

	@Test
	public void TestCase4() {
		Response response = given().param("q", "").when().get("https://www.googleapis.com/books/v1/volumes").then()
				.statusCode(400).extract().response();

		String errorMessage = JsonPath.read(response.asString(), "$.error.message");
		String paramName = JsonPath.read(response.asString(), "$.error.errors[0].location");

		Assert.assertTrue(errorMessage.equals("Missing query.") && paramName.equals("q"),
				"Search Query Parameter Missing Error should be displayed");
	}

	@Test
	public void TestCase5() {
		Response response = given().param("q", "india").param("maxResults", "50").when()
				.get("https://www.googleapis.com/books/v1/volumes").then().statusCode(400).extract().response();

		String errorMessage = JsonPath.read(response.asString(), "$.error.message");
		String paramName = JsonPath.read(response.asString(), "$.error.errors[0].location");

		Assert.assertTrue(
				errorMessage.contains("Values must be within the range: [0, 40]") && paramName.equals("maxResults"),
				"MaxResults invalid range Error should be displayed");
	}

	@Test(enabled = false)
	public void getNearBySearch() throws FileNotFoundException, Exception {

		RestAssured.baseURI = "https://maps.googleapis.com/maps/api/place/nearbysearch";
		RequestSpecification request = RestAssured.given();

		Response response = request.queryParam("key", "AIzaSyB8-3rnI-csEmpIkbn0Js4CTStKX13zda8")
				.queryParam("name", "Harbour Bar & Kitchen").queryParam("radius", 500)
				.get("/json?location=-33.8670522,151.1957362");

		String jsonString = response.asString();
		System.out.println(response.getStatusCode());
		System.out.println(jsonString);
		DocumentContext docCtx = JsonPath.parse(jsonString);
		// Get Programming Skills of an Employee

		JsonPath placeId = cf.jsonCompile("$..place_id");
		JSONArray id = docCtx.read(placeId);
		String sr = id.toString();
		System.out.println(sr);
		softAssert.assertTrue(sr.contains("ChIJkeO_AzquEmsRUpGQn1ZK7Tg"), sr);
		// softAssert.assertEquals(sr, "ChIJkeO_AzquEmsRUpGQn1ZK7Tg");

		pp.loadConfig();
		String expDataFilePath = pp.setConfig("exp_data_file");
		pp.loadProperty(expDataFilePath);
		String pd = pp.setKey("place_id");
		System.out.println(pd + ":::::::::::");

	}

	@Test(enabled = false)

	public void queryParameter() throws FileNotFoundException, Exception {

		RestAssured.baseURI = "https://maps.googleapis.com/maps/api/place/details";
		RequestSpecification request = RestAssured.given();

		Response response = request.queryParam("key", "AIzaSyB8-3rnI-csEmpIkbn0Js4CTStKX13zda8")
				.queryParam("place_id", "ChIJN1t_tDeuEmsRUsoyG83frY4")
				.queryParam("fields", "name,rating,formatted_phone_number").get("/json");

		String jsonString = response.asString();
		System.out.println(response.getStatusCode());
		System.out.println(jsonString);
		DocumentContext docCtx = JsonPath.parse(jsonString);
		// Get Programming Skills of an Employee

		JsonPath name = cf.jsonCompile("$..name");
		JSONArray id = docCtx.read(name);
		String sr = id.toString();

		sr = sr.replaceAll("\\[", "").replaceAll("\\]", "");
		System.out.println(sr);
		// softAssert.assertTrue(sr.contains("ChIJkeO_AzquEmsRUpGQn1ZK7Tg"), sr);
		softAssert.assertEquals(sr, "Google Australia");

		pp.loadConfig();
		String expDataFilePath = pp.setConfig("exp_data_file");
		pp.loadProperty(expDataFilePath);
		String pd = pp.setKey("place_id");
		System.out.println(pd + ":::::::::::");

	}

	@Test(enabled = true)
	public void GoogleApiOAuthExample() {

		// Call API to get Token
		Response tokenResponse = given().contentType("application/x-www-form-urlencoded").when()
				.body("client_id=968534806332-9pdv2vamr7p15r7s5cm8gsac81v11gbf.apps.googleusercontent.com&client_secret=boZMVinb-qGps7Qc4evpuqe4")
				.post("https://www.googleapis.com/oauth2/v4/token").then().statusCode(200).extract().response();
		System.out.println(tokenResponse+":::::::::::::::");
		// Extract Token from Response
		String accessToken = JsonPath.read(tokenResponse.asString(), "$.access_token");
		System.out.println("Token = " + tokenResponse.asString());

		// Provide token as an OAuth2 parameter to rest-assured call
		Response response = given().auth().oauth2(accessToken).pathParam("shelfId", "0").when()
				.get("https://www.googleapis.com/books/v1/mylibrary/bookshelves/{shelfId}/volumes").then()
				.statusCode(200).extract().response();

		// Count of Books from the Favourites Shelf should be greater than 0
		int totalItems = JsonPath.read(response.asString(), "$.totalItems");
		Assert.assertTrue(totalItems > 0, "Total Books from Favourites shelf should be greater than 0");
	}


}
