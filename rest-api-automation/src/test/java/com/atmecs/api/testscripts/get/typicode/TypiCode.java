package com.atmecs.api.testscripts.get.typicode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class TypiCode {

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             This method is written to get count of all users present in json
	 *             placeholder typicode.
	 */
	@Test
	public void tc01GetCountofAllUsers() throws FileNotFoundException, Exception {
		int expectedAllUserCount = 100;
		Reporter.log("Get Count of all Users in json place holder typi code list");
		try {

			// Load config.
			prop.loadConfig();
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Get key Values from properties file.
			String typiCodeBaseURI = prop.setConfig("TypiCodeBaseURI");
			String useridval = prop.setKey("useridval");
			String jsonaccepts = prop.setKey("jsonaccepts");

			// Typicode URL.
			Reporter.log("TypiCode Url - " + typiCodeBaseURI);
			URL urlForGetRequest = new URL(typiCodeBaseURI);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			// Operation(get) to perform response from request.
			Reporter.log(" Operation - GET Request");
			conection.setRequestMethod("GET");
			conection.setRequestProperty("userId", useridval);
			conection.setRequestProperty("Content-Type", jsonaccepts);
			int responseCode = conection.getResponseCode();

			// Validate status code.
			Reporter.log("Validating Status Code");
			Reporter.log("Actual Status Code :" + responseCode);
			Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
			Assert.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");

			BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			// print result
			Reporter.log("JSON String Result " + response.toString());
			String allUserId = prop.setKey("allUserIdExpression");
			DocumentContext docCtx = JsonPath.parse(response.toString());
			// Compile expression
			JsonPath allUserIds = util.jsonCompile(allUserId);
			JSONArray allUserIdsList = docCtx.read(allUserIds);
			int actualAllUserIdsCount = allUserIdsList.size();
			// Validates Total Users count with expected.
			Reporter.log("Validating Total User count ");
			Reporter.log("Actual Users Count :" + actualAllUserIdsCount);
			Reporter.log("Expected Users Count :" + expectedAllUserCount);
			Assert.assertEquals(actualAllUserIdsCount, expectedAllUserCount, "100 Users should be returned");
			Reporter.log("Actual and Expected both are same");

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void tc02GetUserCountById() throws FileNotFoundException, Exception {

		int expectedUserCountWithID2 = 10;
		int expectedUserId = 1;
		Reporter.log("Get Count of all Users in json place holder typi code list");
		try {

			// Load config.
			prop.loadConfig();
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Get key Values from properties file.
			String typiCodeBaseURI = prop.setConfig("TypiCodeBaseURI");
			String useridval = prop.setKey("useridval");
			String jsonaccepts = prop.setKey("jsonaccepts");

			// Typicode URL.
			Reporter.log("TypiCode Url - " + typiCodeBaseURI);
			URL urlForGetRequest = new URL(typiCodeBaseURI);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			// Operation(get) to perform response from request.
			Reporter.log(" Operation - GET Request");
			conection.setRequestMethod("GET");
			conection.setRequestProperty("userId", useridval);
			conection.setRequestProperty("Content-Type", jsonaccepts);
			int responseCode = conection.getResponseCode();

			// Validate status code.
			Reporter.log("Validating Status Code");
			Reporter.log("Actual Status Code :" + responseCode);
			Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
			Assert.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");
			Reporter.log("Status Code Validation : Success");
			BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			// print result
			Reporter.log("JSON String Result " + response.toString());
			String allUserIDWithTwoExpr = prop.setKey("allUserIDWithTwo");
			DocumentContext docCtx = JsonPath.parse(response.toString());
			// Compile expression
			JsonPath allUserIdsWithIDTwo = util.jsonCompile(allUserIDWithTwoExpr);
			JSONArray allUserIdsWithTwoList = docCtx.read(allUserIdsWithIDTwo);

			// Validates Total Users count with expected.
			Reporter.log("Validating Total UserIDs count with value 2");
			Reporter.log("Actual Users Count :" + allUserIdsWithTwoList.size());
			Reporter.log("Expected Users Count :" + expectedUserCountWithID2);
			Assert.assertEquals(allUserIdsWithTwoList, allUserIdsWithTwoList, "10 Users should be returned");
			Reporter.log("Actual and Expected both are same");

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void tc03GetAllIdsWithParticularUserId() throws FileNotFoundException, Exception {
		try {
			Reporter.log("Get all the values for key id, where UserID is 2");
			// Load config.
			prop.loadConfig();
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Get key Values from properties file.
			String typiCodeBaseURI = prop.setConfig("TypiCodeBaseURI");
			String useridval = prop.setKey("useridval");
			String jsonaccepts = prop.setKey("jsonaccepts");

			// Typicode URL.
			Reporter.log("TypiCode Url - " + typiCodeBaseURI);
			URL urlForGetRequest = new URL(typiCodeBaseURI);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			// Operation(get) to perform response from request.
			Reporter.log(" Operation - GET Request");
			conection.setRequestMethod("GET");
			conection.setRequestProperty("userId", useridval);
			conection.setRequestProperty("Content-Type", jsonaccepts);
			int responseCode = conection.getResponseCode();
			int expectedUserId = 2;

			// Validate status code.
			Reporter.log("Validating Status Code");
			Reporter.log("Actual Status Code :" + responseCode);
			Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
			Assert.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(),
					"INFO: Status Code Validation Failed.");
			Reporter.log("Status Code Validation : Success");
			BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			// print result
			Reporter.log("JSON String Result " + response.toString());
			String allUserIDWithTwoExpr = prop.setKey("allUserIDWithTwo");
			DocumentContext docCtx = JsonPath.parse(response.toString());
			String userIDListWithTwo = prop.setKey("userIDListWithTwo");
			// Compile expression
			JsonPath userIDList = util.jsonCompile(userIDListWithTwo);
			JSONArray TotaluserIDValList = docCtx.read(userIDList);
			int count = 0;
			for (int i = 0; i < TotaluserIDValList.size(); i++) {
				int actUserId = (int) TotaluserIDValList.get(i);
				System.out.println(actUserId);
				if (actUserId != expectedUserId)
					;
				{
					count = count + 1;
				}
			}
			System.out.println(count);
			Assert.assertEquals(count, 0, "All User Id Value should be equal to 2");
			String IDExpression = prop.setKey("IDExpression");
			JSONArray idList = JsonPath.read(response.toString(), IDExpression);
			String idListwithUserIDTwo = idList.toString();
			System.out.println(idListwithUserIDTwo);

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
