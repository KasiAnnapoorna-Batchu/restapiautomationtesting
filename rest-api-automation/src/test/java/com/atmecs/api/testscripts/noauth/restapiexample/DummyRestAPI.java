
package com.atmecs.api.testscripts.noauth.restapiexample;

import static io.restassured.RestAssured.given;


import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * 
 * @author Kasi.Batchu
 * This rest is written to Dummy Rest API with 
 * GET and POST requests.
 *
 */
public class DummyRestAPI {

	static PropertiesParsers prop = new PropertiesParsers();
	static Util util = new Util();
	SoftAssert sa = new SoftAssert();

	/**
	 * 
	 * @return
	 * @throws Exception
	 * Create the new resource using 
	 * post request and return the response.
	 * 
	 */
	public String createNewResource() throws Exception {
		// Load testdata.properties file.
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		// Load specified property file.
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		// RestAPI URI.
		String RestApiExampleBaseURL = prop.setConfig("RestApiExampleBaseURL");
		Reporter.log("Rest Api Example Base URL: "+RestApiExampleBaseURL);
		// Get key Values from properties file.
		String nameVal = prop.setKey("valueOfTheName");
		String expContectType = prop.setKey("contectType");

		RestAssured.baseURI = RestApiExampleBaseURL;
		JSONObject payload = util.getJSONObjectFromFilePath(FilePathConstants.CREATE_RESOURCE_PAYLOAD);
		String resourcePayLoad = payload.toString();		
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(resourcePayLoad).post("/create");
			if (response != null) {
				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" + response.getStatusCode());
				Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
				sa.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");				
				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				String actContentType = response.header("Content-Type");
				util.validateContentType(actContentType, expContectType);				
				Reporter.log("Actaul Content Type : " + actContentType);
				Reporter.log("Expcted Content Type :" + expContectType);

				// By default response time in milliseconds
				long responseTime1 = response.getTime();
				Reporter.log("Response time in ms using getTime():" + responseTime1);

				// we can get response time seconds
				long responseTimeInSeconds = response.getTimeIn(TimeUnit.SECONDS);
				Reporter.log("Response time in seconds using getTimeIn():" + responseTimeInSeconds);

				// Validate new resource creation.
				boolean namecontains = response.asString().contains(nameVal);
				sa.assertTrue(namecontains, "Name Should exists in the List");

			}
		}

		catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
		return response.asString();
	}

	/**
	 * 
	 * @throws Exception
	 * This method is written to test whether the
	 * new resource is creation is successful or not.
	 * 
	 */
	@Test
	public void tc01GetAndValidateNewResourceCreation() throws Exception {
		// Load specified property file.
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String responseStringfromPost = createNewResource();
		String idgetval = prop.setKey("idgetval");
		int idval = JsonPath.read(responseStringfromPost, idgetval);
		
		String RestApiExampleBaseURL = prop.setConfig("RestApiExampleBaseURL");
		RestAssured.baseURI = RestApiExampleBaseURL;

		Response response = null;

		try {
			Reporter.log(RestApiExampleBaseURL + "/employees/" + idval);
			response = given().when().get(RestApiExampleBaseURL + "/employee/" + idval);
			// response =
			// RestAssured.given().contentType(ContentType.JSON).get("/employees/idval");
			if (response != null) {
				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" + response.getStatusCode());
				Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
				sa.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");
				Reporter.log(response.toString());
				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				// By default response time in milliseconds
				long responseTime1 = response.getTime();
				Reporter.log("Response time in ms using getTime():" + responseTime1);

				// we can get response time seconds
				long responseTimeInSeconds = response.getTimeIn(TimeUnit.SECONDS);
				Reporter.log("Response time in seconds using getTimeIn():" + responseTimeInSeconds);
			}
		}

		catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
