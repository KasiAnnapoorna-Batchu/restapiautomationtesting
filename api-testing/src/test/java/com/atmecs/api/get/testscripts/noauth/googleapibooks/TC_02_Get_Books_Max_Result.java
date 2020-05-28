package com.atmecs.api.get.testscripts.noauth.googleapibooks;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;

public class TC_02_Get_Books_Max_Result {
	PropertiesParsers cf = new PropertiesParsers();

	@Test
	public void getBooksMaxResult() throws FileNotFoundException, Exception {
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

}
