package com.atmecs.api.testscripts.delete.oauth2.googleapitasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;

public class TC_01_DeleteGoogleAPITask {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void TC_01_deleteTaskById() throws Exception {
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();

		// Get key Values from properties file.
		String tasksListBaseURL = prop.setConfig("TasksListBaseURL");

		String accessToken = util.getTaskUrl();
		try {
			if (accessToken != null && !accessToken.isEmpty()) {
				String urlParamidval = prop.setKey("urlParamidval");
				URL obj = new URL(tasksListBaseURL + "/" + urlParamidval);

				HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("DELETE");
				connection.setRequestProperty("Authorization", "Bearer " + accessToken);
				int responseCode = connection.getResponseCode();
				System.out.println("Response code: " + connection.getResponseCode());
				System.out.println("DELETE Response Code :  " + responseCode);
				System.out.println("DELETE Response Message : " + connection.getResponseMessage());
				Reporter.log("DELETE Worked ");
				Assert.assertEquals(connection.getResponseCode(), STATUS_CODE.STATUS_204.getValue(),
						"INFO: Status Code Validation Failed.DELETE NOT WORKED ");
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line, responseText = "";
				while ((line = br.readLine()) != null) {
					System.out.println("LINE: " + line);
					responseText += line;
				}
				br.close();
				connection.disconnect();

			} else {
				Reporter.log("Access Token is either null or empty");
			}

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
