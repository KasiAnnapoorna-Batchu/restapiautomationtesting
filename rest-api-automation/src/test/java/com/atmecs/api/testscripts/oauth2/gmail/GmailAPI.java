package com.atmecs.api.testscripts.oauth2.gmail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;

/**
 * 
 * @author Kasi.Batchu
 * This class written to test GMAAIL DRAFT API. 
 */
public class GmailAPI {

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();
	SoftAssert sa = new SoftAssert();

	/**
	 * 
	 * @throws Exception
	 * This method is written to get all gmail draft
	 * list using gmail draft list api.
	 */
	@Test	
	public void tc01getGmailDraftsList() throws Exception {
		
		// Load testdata.properties file.
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String accessToken = util.getTaskUrl();
		// Get key Values from properties file.
		String baseGmailURL = prop.setConfig("BaseGmailURL");
		String userId = prop.setKey("userId");
				
		String gmailDraftURL = baseGmailURL + userId + "/drafts";
		Reporter.log("GMAIL DRAFT API : "+gmailDraftURL);
				
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
		sa.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(), "INFO: Status Code Validation Failed.");
	
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();		
		Reporter.log(response.toString());

	}
	
	/**
	 *
	 * @throws Exception
	 * This method is written to particular mail from Drafts
	 * list in one's particular user id based on the ID of 
	 * the draft.
	 */
		
	@Test
	public void tc02getParticularGmailDraftByID() throws Exception {
		// Load testdata.properties file.
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
		Reporter.log("Gmail Draft URL : "+gmailDraftURL);
		Reporter.log("Draft ID"+draftid);
		URL obj = new URL(gmailDraftURL);		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + accessToken);
		
		int responseCode = con.getResponseCode();
		// Validate status code.
		Reporter.log("Validating Status Code");
		Reporter.log("Actual Status Code :" + responseCode);
		Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
		sa.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(), "INFO: Status Code Validation Failed.");
	
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();		
		Reporter.log(response.toString());

	}
	
	/**
	 * 
	 * @throws Exception
	 * This method is written to create a new draft
	 * into one's Draft Box.
	 */
	@Test
	public void tc03CreateNewDraft() throws Exception {
		// Load testdata.properties file.
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String accessToken = util.getTaskUrl();
		
		// Get key Values from properties file.
		String baseGmailURL = prop.setConfig("BaseGmailURL");
		Reporter.log("Gmail Draft URL : "+baseGmailURL);
		String userId = prop.setKey("userId");		
		String gmailDraftURL = baseGmailURL + userId + "/drafts";	
		// Get key Values from properties file.				
		String jsonaccepts = prop.setKey("jsonaccepts");
		String idval = prop.setConfig("idval");
		String paramName = "id";
		try {
			JSONObject payload = util.getJSONObjectFromFilePath(FilePathConstants.CREATE_DRAFT_PAYLOAD);
			String draftPayLoad = payload.toString();			
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
			sa.assertEquals(responseCode, STATUS_CODE.STATUS_200.getValue(),
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
				// Validates idval in response string
				Reporter.log("Validate Draft ID created in response string");
				sa.assertTrue(
						response.toString().contains(idval)
								&& paramName.equals("id"),
						"Doesn't required ID");				
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
