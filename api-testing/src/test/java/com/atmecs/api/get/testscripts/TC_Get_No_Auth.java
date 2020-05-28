package com.atmecs.api.get.testscripts;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import com.atmecs.utility.parser.PropertiesParsers;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.atmecs.api.utility.STATUS_CODE;

import io.restassured.response.Response;

public class TC_Get_No_Auth {
	SoftAssert softAssert = new SoftAssert();
	PropertiesParsers cf = new PropertiesParsers();

	@Test
	public void getBookCount() throws FileNotFoundException, Exception {
		try {

			cf.loadConfig();
			String booksBaseURI = cf.setConfig("BooksBaseURI");

			String parampottervalue = cf.setConfig("potter");
			String totalitems = cf.setConfig("totalitems");
			String items = cf.setConfig("items");

			Response response = given().param("q", parampottervalue).when().get(booksBaseURI);

			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");
			int totalItems = JsonPath.read(response.asString(), totalitems);

			Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

			List<Object> allBooks = JsonPath.read(response.asString(), items);

			Assert.assertEquals(allBooks.size(), 10, "10 Books should be returned by default");

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void getMaxResults() throws FileNotFoundException, Exception {
		try {

			cf.loadConfig();
			String booksBaseURI = cf.setConfig("BooksBaseURI");

			String paramdan = cf.setConfig("danbrown");
			String maxcount = cf.setConfig("maxcount");
			String totalitems = cf.setConfig("totalitems");
			String items = cf.setConfig("items");

			Response response = given().param("q", paramdan).param("maxResults", maxcount).when().get(booksBaseURI);
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");

			int totalItems = JsonPath.read(response.asString(), totalitems);

			Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

			List<Object> allBooks = JsonPath.read(response.asString(), items);

			Assert.assertEquals(allBooks.size(), 25, "25 Books should be returned");
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void getMissingQueryError() throws FileNotFoundException, Exception {
		try {
			cf.loadConfig();
			String booksBaseURI = cf.setConfig("BooksBaseURI");
			String errormsg = cf.setConfig("errormsg");
			String errorlocation = cf.setConfig("errorlocation");

			Response response = given().param("q", "").when().get(booksBaseURI);
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
					"INFO: Status Code Validation Failed.");
			String errorMessage = JsonPath.read(response.asString(), errormsg);
			String paramName = JsonPath.read(response.asString(), errorlocation);

			Assert.assertTrue(errorMessage.equals("Missing query.") && paramName.equals("q"),
					"Search Query Parameter Missing Error should be displayed");
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void GetInvalidRangeError() throws FileNotFoundException, Exception {
		try {
			cf.loadConfig();
			String booksBaseURI = cf.setConfig("BooksBaseURI");
			String country = cf.setConfig("country");
			String invalidrange = cf.setConfig("invalidrange");
			String errormsg = cf.setConfig("errormsg");
			String errorlocation = cf.setConfig("errorlocation");
			Response response = given().param("q", country).param("maxResults", invalidrange).when().get(booksBaseURI);
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
					"INFO: Status Code Validation Failed.");

			String errorMessage = JsonPath.read(response.asString(), errormsg);
			String paramName = JsonPath.read(response.asString(), errorlocation);

			Assert.assertTrue(
					errorMessage.contains("Values must be within the range: [0, 40]") && paramName.equals("maxResults"),
					"MaxResults invalid range Error should be displayed");

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
