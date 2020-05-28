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

public class TC_04_Get_Test_InValid_Range_Error {
	PropertiesParsers cf = new PropertiesParsers();

	@Test
	public void GetInvalidRangeError() throws FileNotFoundException, Exception {
		try {
			cf.loadConfig();
			String booksBaseURI = cf.setConfig("BooksBaseURI");
			String country = cf.setConfig("country");
			String invalidrange = cf.setConfig("invalidrange");
			String errormsg = cf.setConfig("errormsg");
			String errorlocation = cf.setConfig("errorlocation");
			Response response = given().param("q", country).param("maxResults", invalidrange).when().get(booksBaseURI);
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
					"INFO: Status Code Validation Failed.");

			String errorMessage = JsonPath.read(response.asString(), errormsg);
			String paramName = JsonPath.read(response.asString(), errorlocation);

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
