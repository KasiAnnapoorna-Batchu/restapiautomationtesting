
package com.atmecs.api.testscripts.post.noauth.restapiexample;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class DummyRestAPI {

	static PropertiesParsers prop = new PropertiesParsers();
	static Util util = new Util();

	@Test(enabled = true)

	public String createNewResource() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		// Load specified property file.
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		// RestAPI URI.
		String RestApiExampleBaseURL = prop.setConfig("RestApiExampleBaseURL");
		// Get key Values from properties file.
		String nameVal = prop.setKey("valueOfTheName");
		String expContectType = prop.setKey("contectType");

		RestAssured.baseURI = RestApiExampleBaseURL;
		JSONObject payload = util.getJSONObjectFromFilePath(FilePathConstants.CREATE_RESOURCE_PAYLOAD);
		String resourcePayLoad = payload.toString();
		System.out.println(resourcePayLoad);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(resourcePayLoad).post("/create");
			if (response != null) {
				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" + response.getStatusCode());
				Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
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
				Assert.assertTrue(namecontains, "Name Should exists in the List");

			}
		}

		catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
		return response.asString();
	}

	@Test
	public void tc02getAllEmployeeDetails() throws Exception {
		// Load specified property file.
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String responseStringfromPost = createNewResource();
		String idgetval = prop.setKey("idgetval");
		int idval = JsonPath.read(responseStringfromPost, idgetval);
		System.out.println(idval);
		String RestApiExampleBaseURL = prop.setConfig("RestApiExampleBaseURL");
		RestAssured.baseURI = RestApiExampleBaseURL;

		Response response = null;

		try {
			System.out.println(RestApiExampleBaseURL + "/employees/" + idval);
			response = given().when().get(RestApiExampleBaseURL + "/employee/" + idval);
			// response =
			// RestAssured.given().contentType(ContentType.JSON).get("/employees/idval");
			if (response != null) {
				// Validate status code.
				Reporter.log("Validating Status Code");
				Reporter.log("Actual Status Code :" + response.getStatusCode());
				Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");

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
