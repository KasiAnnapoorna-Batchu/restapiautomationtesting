package com.atmecs.api.testscripts.get.apikey.googlemaps;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONArray;

/**
 * 
 * @author Kasi.Batchu Perform get operation to know the place based on the
 *         place id.
 */
public class TC_02_Get_Place_By_Id {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             Get Place from the place id using API key Authentiaation.
	 * 
	 */
	@Test
	public void TC_02_Get_Place_Based_On_Id() throws FileNotFoundException, Exception {
		try {

			// Load Config
			prop.loadConfig();
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Get key Values from properties file.
			String GoogleMapsPlacesDetailsApi = prop.setConfig("GoogleMapsPlacesDetailsApi");
			String placeIdExp = prop.setKey("placeIdExp");
			String keyval = prop.setKey("keyval");
			String fieldval = prop.setKey("fieldval");
			String name = prop.setKey("name");
			String expectedCountryName = prop.setKey("harbourKitchen");
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");

			// Google Map - Places base uri
			RestAssured.baseURI = GoogleMapsPlacesDetailsApi;
			RequestSpecification request = RestAssured.given();

			// Operation to perform response from request.
			Reporter.log(" Create Request to get response");
			Response response = request.queryParam("key", keyval).queryParam("place_id", placeIdExp)
					.queryParam("fields", fieldval).get("/json");

			if (response != null) {
				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" +response.getStatusCode());
				Reporter.log("Expected Status Code:  "+ STATUS_CODE.STATUS_200.getValue());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");
				Reporter.log("Status code validation : Passed");
				// Convert response to string.
				String jsonString = response.asString();

				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				String actContentType = response.header("Content-Type");
				String actContentEncoding = response.header("Content-Encoding");
				util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);

				// Parse json string
				DocumentContext docCtx = JsonPath.parse(jsonString);
				// Compile expression
				JsonPath nameval = util.jsonCompile(name);
				JSONArray id = docCtx.read(nameval);
				String actualCountryName = id.toString();

				// Remove [,] from string.
				actualCountryName = actualCountryName.replaceAll("\\[", "").replaceAll("\\]", "");

				// Validate the country name with expected.
				Reporter.log("Validating Actual Country Name with Expected Country Name");
				Reporter.log("Actual Country Name :" + actualCountryName);
				Reporter.log("Expected Country Name :" + expectedCountryName);
				Assert.assertEquals(actualCountryName, expectedCountryName);

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
