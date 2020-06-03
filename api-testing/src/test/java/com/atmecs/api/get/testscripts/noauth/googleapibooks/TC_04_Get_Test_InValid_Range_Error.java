package com.atmecs.api.get.testscripts.noauth.googleapibooks;

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
 * @author Kasi.Batchu
 * This class is written to test whether invalid range error
 * using get operation.
 *
 */
public class TC_04_Get_Test_InValid_Range_Error {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Get and validate whether invalid range 
	 * error when the range is given above the max 
	 * range.
	 * 
	 */
		
	@Test
	public void GetInvalidRangeError() throws FileNotFoundException, Exception {
		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Load config
			prop.loadConfig();
			// Get key Values from properties file.
			String booksBaseURI = prop.setConfig("BooksBaseURI");
			String country = prop.setKey("country");
			String invalidrange = prop.setKey("invalidrange");
			String errormsg = prop.setKey("errormsg");
			String errorlocation = prop.setKey("errorlocation");
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");
			
			// Create Request.
			// Operation to get response from request.
			Reporter.log(" Create Request to get response ");
			Response response = given().param("q", country).param("maxResults", invalidrange).when().get(booksBaseURI);
						
			// Validate status code.
			Reporter.log("Status Code: "+ response.getStatusCode());
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
					"INFO: Status Code Validation Failed.");

			// Validate Response Headers.
			Reporter.log("Validating Content Type & Content Encoding Values");
			String actContentType = response.header("Content-Type");
			String actContentEncoding = response.header("Content-Encoding");

			util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);
			
			String errorMessage = JsonPath.read(response.asString(), errormsg);
			String paramName = JsonPath.read(response.asString(), errorlocation);
			
			Reporter.log("Validating Invalid range error");
			// Validates invalid range error.
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
