package com.atmecs.api.get.testscripts.noauth.googleapibooks;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;
/**
 * 
 * @author Kasi.Batchu
 * This class is written to test missing query parameter
 * error using get operation.
 */
public class TC_03_Get_Test_Missing_Query_Error {
	PropertiesParsers cf = new PropertiesParsers();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Using get operation, verify whether missing query 
	 * parameter error is occurred or not when no param 
	 * value is provided. 
	 */
	@Test
	public void getMissingQueryError() throws FileNotFoundException, Exception {
		try {
			// Load config
			cf.loadConfig();
			// Get key Values from properties file.
			String booksBaseURI = cf.setConfig("BooksBaseURI");
			String errormsg = cf.setConfig("errormsg");
			String errorlocation = cf.setConfig("errorlocation");
			// Operation to perform response from request.
			Response response = given().param("q", "").when().get(booksBaseURI);
			// Validate status code.
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
					"INFO: Status Code Validation Failed.");
			
			String errorMessage = JsonPath.read(response.asString(), errormsg);
			String paramName = JsonPath.read(response.asString(), errorlocation);
			
			// Validates query parameter missing error
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

}
