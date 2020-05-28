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

/**
 * 
 * @author Kasi.Batchu
 * 
 * This class is written to get the books count
 * from specified api.
 *
 */

public class TC_01_Get_Books_Count {
	SoftAssert softAssert = new SoftAssert();
	PropertiesParsers cf = new PropertiesParsers();
	
	/**
	 *
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Get total no of books using books api and 
	 * perform validations to know whether the count 
	 * gotten is correct or not.
	 */

	@Test
	public void getBooksCount() throws FileNotFoundException, Exception {
		try {
			
			// Load config.
			cf.loadConfig();
			
			// Books Base URI.
			String booksBaseURI = cf.setConfig("BooksBaseURI");
			// Get key Values from properties file.
			String parampottervalue = cf.setConfig("potter");
			String totalitems = cf.setConfig("totalitems");
			String items = cf.setConfig("items");

			// Operation to perform response from request.
			Response response = given().param("q", parampottervalue).when().get(booksBaseURI);
			
			// Validate status code.
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");
			int totalItems = JsonPath.read(response.asString(), totalitems);
			// Validate whether the count is greater than zero or not.
			Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

			List<Object> allBooks = JsonPath.read(response.asString(), items);

			// Validate actual books size with expected. 
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
