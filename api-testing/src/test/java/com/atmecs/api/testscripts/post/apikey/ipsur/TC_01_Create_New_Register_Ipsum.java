package com.atmecs.api.testscripts.post.apikey.ipsur;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_01_Create_New_Register_Ipsum {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void TC_01_Post_Ipsum_Register() throws Exception {
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();

		try {
			// Load Config
			prop.loadConfig();
			// Get key Values from properties file.
			String IpsumRegisterURI = prop.setConfig("IpsumRegisterURI");
			String ipsurkeyval = prop.setKey("ipsurkeyval");
			String ipsurhostval = prop.setKey("ipsurhostval");

			// Base URI
			RestAssured.baseURI = IpsumRegisterURI;
			RequestSpecification request = RestAssured.given();

			// Operation to perform response from request.
			Response response = request.queryParam("x-rapidapi-key", ipsurkeyval).param("x-rapidapi-host", ipsurhostval).
					post(IpsumRegisterURI);

			// Convert response to String
			String jsonString = response.asString();

			// Validate status code.
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_201.getValue(),
					"INFO: Status Code Validation Failed.");
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
