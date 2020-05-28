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

public class TC_03_Get_Test_Missing_Query_Error {
	PropertiesParsers cf = new PropertiesParsers();

	@Test
	public void getMissingQueryError() throws FileNotFoundException, Exception {
		try {
			cf.loadConfig();
			String booksBaseURI = cf.setConfig("BooksBaseURI");
			String errormsg = cf.setConfig("errormsg");
			String errorlocation = cf.setConfig("errorlocation");

			Response response = given().param("q", "").when().get(booksBaseURI);
			Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_400.getValue(),
					"INFO: Status Code Validation Failed.");
			String errorMessage = JsonPath.read(response.asString(), errormsg);
			String paramName = JsonPath.read(response.asString(), errorlocation);

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
