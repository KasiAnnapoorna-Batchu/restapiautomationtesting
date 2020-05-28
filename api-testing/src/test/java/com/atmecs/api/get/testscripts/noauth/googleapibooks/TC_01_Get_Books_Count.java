package com.atmecs.api.get.testscripts.noauth.googleapibooks;

import static io.restassured.RestAssured.given;
import com.atmecs.utility.parser.PropertiesParsers;

import java.io.FileNotFoundException;
import java.util.List;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.atmecs.api.utility.STATUS_CODE;

import io.restassured.response.Response;

public class TC_01_Get_Books_Count {
	SoftAssert softAssert = new SoftAssert();
	PropertiesParsers cf = new PropertiesParsers();

	@Test
	public void getBooksCount() throws FileNotFoundException, Exception {
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

}
