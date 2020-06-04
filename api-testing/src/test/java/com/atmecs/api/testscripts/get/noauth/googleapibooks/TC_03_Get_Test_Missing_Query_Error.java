package com.atmecs.api.testscripts.get.noauth.googleapibooks;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;

/**
 * 
 * @author Kasi.Batchu. This class is written to test missing query parameter
 *         error using get operation.
 * 
 */
public class TC_03_Get_Test_Missing_Query_Error {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             Using get operation, verify whether missing query parameter error
	 *             is occurred or not when no param value is provided.
	 * 
	 */
	@Test
	public void getTestMissingQueryError() throws FileNotFoundException, Exception {
		Reporter.log("Test Missing Query Error");

		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Load config
			prop.loadConfig();
			// Get key Values from properties file.
			String booksBaseURI = prop.setConfig("BooksBaseURI");
			String errormsg = prop.setKey("errormsg");
			String errorlocation = prop.setKey("errorlocation");
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");

			// Create Request.
			// Operation to get response from request.
			Reporter.log(" Create Request to get response ");

			Response response = given().param("q", "").when().get(booksBaseURI);
			if (response != null) {

				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" + response.getStatusCode());
				Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_400.getValue());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
						"INFO: Status Code Validation Failed.");

				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				String actContentType = response.header("Content-Type");
				String actContentEncoding = response.header("Content-Encoding");

				util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);

				String errorMessage = JsonPath.read(response.asString(), errormsg);
				String paramName = JsonPath.read(response.asString(), errorlocation);
				Reporter.log("Validating missing query error");
				// Validates query parameter missing error
				Assert.assertTrue(errorMessage.equals("Missing query.") && paramName.equals("q"),
						"Search Query Parameter Missing Error should be displayed");

			} else {
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
