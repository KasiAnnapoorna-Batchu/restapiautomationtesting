package com.atmecs.api.testscripts.get.oauth2.googleapitasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
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

public class GmailAPI {

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void tc01getGmailDraftsList() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String accessToken = util.getTaskUrl();
		// Get key Values from properties file.
		String baseGmailURL = prop.setConfig("BaseGmailURL");
		String userId = prop.setKey("userId");
				
		String gmailDraftURL = baseGmailURL + userId + "/drafts";
				
		URL obj = new URL(gmailDraftURL);
		Reporter.log("Base Url :" + gmailDraftURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + accessToken);
		int responseCode = con.getResponseCode();
		// Validate status code.
		Reporter.log("Validating Status Code");
		Reporter.log("Actual Status Code :" + responseCode);
		Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
		Assert.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(), "INFO: Status Code Validation Failed.");
		// success
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		Reporter.log(response.toString());

	}
		
	@Test
	public void tc02getParticularGmailDraftByID() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String accessToken = util.getTaskUrl();
		// Get key Values from properties file.
		String baseGmailURL = prop.setConfig("BaseGmailURL");
		String userId = prop.setKey("userId");
		String draftid =  prop.setKey("draftid");
		String gmailDraftURL = baseGmailURL + userId + "/drafts"+"/"+draftid;
		
		URL obj = new URL(gmailDraftURL);
		Reporter.log("Base Url :" + gmailDraftURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + accessToken);
		int responseCode = con.getResponseCode();
		// Validate status code.
		Reporter.log("Validating Status Code");
		Reporter.log("Actual Status Code :" + responseCode);
		Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
		Assert.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(), "INFO: Status Code Validation Failed.");
		// success
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		Reporter.log(response.toString());

	}
	
	@Test
	public void tc03CreateNewDraft() throws Exception {
		
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String accessToken = util.getTaskUrl();
		
		// Get key Values from properties file.
		String baseGmailURL = prop.setConfig("BaseGmailURL");
		String userId = prop.setKey("userId");		
		String gmailDraftURL = baseGmailURL + userId + "/drafts";	
		// Get key Values from properties file.		
		String useridval = prop.setKey("useridval");
		String jsonaccepts = prop.setKey("jsonaccepts");
		String idval = prop.setConfig("idval");
		String paramName = "id";
		try {
			JSONObject payload = util.getJSONObjectFromFilePath(FilePathConstants.CREATE_DRAFT_PAYLOAD);
			String draftPayLoad = payload.toString();
			System.out.println(draftPayLoad);
			URL obj = new URL(gmailDraftURL);
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
			postConnection.setRequestProperty("Content-Type", jsonaccepts);
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(draftPayLoad.getBytes());
			os.flush();
			os.close();

			int responseCode = postConnection.getResponseCode();
			Reporter.log("POST Response Code :  " + responseCode);
			Reporter.log("POST Response Message : " + postConnection.getResponseMessage());
			Reporter.log("POST Worked ");
			Assert.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(),
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
				System.out.println(response.toString());
				
				// Validates invalid range error.
				Assert.assertTrue(
						response.toString().contains(idval)
								&& paramName.equals("id"),
						"MaxResults invalid range Error should be displayed");


			} else {
				Reporter.log("POST NOT WORKED");
			}

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
