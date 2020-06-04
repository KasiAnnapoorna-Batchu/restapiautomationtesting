
package com.atmecs.api.testscripts.post.oauth2;

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

public class TC_01_Create_New_Typi_Code {

	static PropertiesParsers cf = new PropertiesParsers();
	static Util util = new Util();

	@Test(enabled = true)
	public static void TC_01_Create_New_TypiCode() throws Exception {

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
			JSONObject typiCodePayLoad = util.getJSONObjectFromFilePath(FilePathConstants.TYPICODE_PAYLOAD);
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
				Reporter.log("POST NOT WORKED");
			}

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
