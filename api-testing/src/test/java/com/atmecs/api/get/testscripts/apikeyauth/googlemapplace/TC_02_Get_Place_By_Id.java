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
 * Perform get operation to know the place based on the
 * place id.
 */
public class TC_02_Get_Place_By_Id {
	PropertiesParsers cf = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Get Place from the place id using API key Authentiaation.
	 * 
	 */
	@Test
	public void getplacefromIDUsingAPIKeyAuth() throws FileNotFoundException, Exception {
		try {

			// Load Config
			cf.loadConfig();

			// Get key Values from properties file.
			String GoogleMapsPlacesDetailsApi = cf.setConfig("GoogleMapsPlacesDetailsApi");
			String placeIdExp = cf.setConfig("placeIdExp");
			String keyval = cf.setConfig("keyval");
			String fieldval = cf.setConfig("fieldval");
			String name = cf.setConfig("name");
			String nameexp = cf.setConfig("nameexp");
			String harbourKitchen = cf.setConfig("harbourKitchen");

			// Google Map - Places base uri
			RestAssured.baseURI = GoogleMapsPlacesDetailsApi;
			RequestSpecification request = RestAssured.given();

			// Operation to perform response from request.
			Response response = request.queryParam("key", keyval).queryParam("place_id", placeIdExp)
					.queryParam("fields", fieldval).get("/json");

			// Validate status code.
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");

			// Convert response to string.
			String jsonString = response.asString();

			// Parse json string
			DocumentContext docCtx = JsonPath.parse(jsonString);

			// Compile expression
			JsonPath nameval = util.jsonCompile(name);
			JSONArray id = docCtx.read(nameval);
			String sr = id.toString();

			// Remove [,] from string.

			sr = sr.replaceAll("\\[", "").replaceAll("\\]", "");

			// Validate the country name with expected.
			Assert.assertEquals(sr, harbourKitchen);
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
