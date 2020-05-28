package com.atmecs.api.get.testscripts.apikeyauth.googlemapplace;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

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
 * Perform NearBy Search on google maps using places api.
 *
 */
public class TC_01_Get_NearBy_Search {

	PropertiesParsers cf = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Perform NearBy Search Operation using API Key Authentication
	 * 
	 */
	@Test
	public void getNearBySearchUsingAPIKeyAuth() throws FileNotFoundException, Exception {
		try {

			// Load Config
			cf.loadConfig();
			// Get key Values from properties file.
			String GoogleMapsPlacesAPI = cf.setConfig("GoogleMapsPlacesAPI");
			String keyval = cf.setConfig("keyval");
			String harbourKitchen = cf.setConfig("harbourKitchen");
			String radiusval = cf.setConfig("radiusval");
			String placeIdExp = cf.setConfig("placeIdExp");
			String placeidexpression = cf.setConfig("placeidexpression");

			// Base URI
			RestAssured.baseURI = GoogleMapsPlacesAPI;
			RequestSpecification request = RestAssured.given();

			// Operation to perform response from request.
			Response response = request.queryParam("key", keyval).queryParam("name", harbourKitchen)
					.queryParam("radius", radiusval).get("/json?location=-33.8670522,151.1957362");

			// Convert response to String
			String jsonString = response.asString();

			// Validate status code.
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");

			// Parse Json String.
			DocumentContext docCtx = JsonPath.parse(jsonString);
			// Compile expression
			JsonPath placeId = util.jsonCompile(placeidexpression);
			JSONArray id = docCtx.read(placeId);
			String sr = id.toString();

			// Validate the place id.
			Assert.assertTrue(sr.contains(placeIdExp), sr);

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
