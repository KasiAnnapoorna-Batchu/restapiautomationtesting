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
 * @author Kasi.Batchu
 * 
 *         Perform NearBy Search on google maps with the location provided using
 *         places api .
 *
 */
public class TC_01_Get_NearBy_Search {

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             Perform NearBy Search Operation using API Key Authentication
	 * 
	 */
	@Test
	public void TC_01_GetNearByPlace() throws FileNotFoundException, Exception {
		try {

			// Load Config
			prop.loadConfig();
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Get key Values from properties file.
			String GoogleMapsPlacesAPI = prop.setConfig("GoogleMapsPlacesAPI");
			String keyval = prop.setKey("keyval");
			String harbourKitchen = prop.setKey("harbourKitchen");
			String radiusval = prop.setKey("radiusval");
			String placeIdExp = prop.setKey("placeIdExp");
			String placeidexpression = prop.setKey("placeidexpression");
			String locationval = prop.setKey("locationval");
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");

			// Base URI
			RestAssured.baseURI = GoogleMapsPlacesAPI;
			RequestSpecification request = RestAssured.given();

			// Operation to perform response from request.
			Reporter.log(" Create Request to get response");
			Response response = request.queryParam("key", keyval).queryParam("name", harbourKitchen)
					.queryParam("radius", radiusval).get("/json?location=" + locationval);

			if (response != null) {
				// Convert response to String
				String jsonString = response.asString();

				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				String actContentType = response.header("Content-Type");
				String actContentEncoding = response.header("Content-Encoding");
				util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);

				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" + response.getStatusCode());
				Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");

				// Parse Json String.
				DocumentContext docCtx = JsonPath.parse(jsonString);
				// Compile expression
				JsonPath placeId = util.jsonCompile(placeidexpression);
				JSONArray id = docCtx.read(placeId);
				String actualPlaceID = id.toString();

				Reporter.log("Actual Place Id = " + actualPlaceID);
				Reporter.log("Expected Placce Id= " + placeIdExp);
				// Validate the place id.
				Assert.assertTrue(actualPlaceID.contains(placeIdExp), actualPlaceID);
				Reporter.log("Near By Place Found Based on the location provided");
				Reporter.log("Get - Worked");
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
