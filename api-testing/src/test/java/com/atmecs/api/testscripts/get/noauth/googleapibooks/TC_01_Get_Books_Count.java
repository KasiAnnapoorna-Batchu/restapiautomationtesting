package com.atmecs.api.testscripts.get.noauth.googleapibooks;

import static io.restassured.RestAssured.given;

import com.atmecs.utility.parser.PropertiesParsers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;

import io.restassured.response.Response;

/**
 * 
 * @author Kasi.Batchu
 * 
 * This class is written to get the books count from specified api.
 *
 */

public class TC_01_Get_Books_Count {
	PropertiesParsers prop = new PropertiesParsers();
	 Util util = new Util();
	
	/**
	 *
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Get total no of books using books api and perform validations to
	 * know whether the count gotten is correct or not.
	 * 
	 */
	
	@Test
	public void getAndTestBooksItemsCount() throws FileNotFoundException, Exception {
		try {

			// Load config.
			prop.loadConfig();
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);

			// Books Base URI.
			String booksBaseURI = prop.setConfig("BooksBaseURI");
			// Get key Values from properties file.
			String parampottervalue = prop.setKey("potter");
			String totalitems = prop.setKey("totalitems");
			String items = prop.setKey("items");
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");

			Reporter.log(" Create Request to get response");
			// Operation to perform response from request.			
			Response response = given().param("q", parampottervalue).when().get(booksBaseURI);			
			
			if (response != null) {
				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" +response.getStatusCode());
				Reporter.log("Expected Status Code:  "+ STATUS_CODE.STATUS_200.getValue());				
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");
				
				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				String actContentType = response.header("Content-Type");
				String actContentEncoding = response.header("Content-Encoding");
				util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);

				int totalItems = JsonPath.read(response.asString(), totalitems);
				// Validate whether the count is greater than zero or not.
				Reporter.log("Validating total no of items is greater than zero or not");				
				Reporter.log("Total No of items : " + totalItems);				
				Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");
			}
		 else {
			Reporter.log("Response is either null or empty");
		}

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
