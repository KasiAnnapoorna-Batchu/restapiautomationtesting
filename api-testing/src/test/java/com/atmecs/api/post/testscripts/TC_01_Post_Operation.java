package com.atmecs.api.post.testscripts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
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

public class TC_01_Post_Operation {

	static PropertiesParsers cf = new PropertiesParsers();
	static Util util = new Util();

	@Test(enabled = false)
	public static void TC_01_Post_TypiCode() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		// Load specified property file.
		cf.loadProperty(tdf);
		// Load config
		cf.loadConfig();

		// Get key Values from properties file.
		String typiCodeBaseURI = cf.setConfig("TypiCodeBaseURI");
		String useridval = cf.setConfig("useridval");
		String jsonaccepts = cf.setConfig("jsonaccepts");
		try {
			JSONObject typiCodePayLoad = Util.getJSONObjectFromFilePath(FilePathConstants.TYPICODE_PAYLOAD);
			String typiCodePayLoadStr = typiCodePayLoad.toString();
			System.out.println(typiCodePayLoad);
			URL obj = new URL(typiCodeBaseURI);
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("userId", useridval);
			postConnection.setRequestProperty("Content-Type", jsonaccepts);
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(typiCodePayLoadStr.getBytes());
			os.flush();
			os.close();

			int responseCode = postConnection.getResponseCode();
			Reporter.log("POST Response Code :  " + responseCode);
			Reporter.log("POST Response Message : " + postConnection.getResponseMessage());
			Reporter.log("POST Worked ");
			Assert.assertEquals(responseCode, STATUS_CODE.STATUS_201.getValue(),
					"INFO: Status Code Validation Failed.");

			if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print result
				Reporter.log(response.toString());
				
			} else {
				System.out.println("POST NOT WORKED");
			}

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Test(enabled = true)
	public static void TC_01_Post_Ipsum_Register() {

		try {
			// Load Config
			cf.loadConfig();
			// Get key Values from properties file.
			String IpsumRegisterURI = cf.setConfig("IpsumRegisterURI");
			String ipsurkeyval = cf.setConfig("ipsurkeyval");
			String ipsurhostval = cf.setConfig("ipsurhostval");

			// Base URI
			RestAssured.baseURI = IpsumRegisterURI;
			RequestSpecification request = RestAssured.given();

			// Operation to perform response from request.
			Response response = request.queryParam("x-rapidapi-key", ipsurkeyval).param("x-rapidapi-host", ipsurhostval)
					.post(IpsumRegisterURI);

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
