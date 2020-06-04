package com.atmecs.api.testscripts.post.oauth2.googleapitasks;
import java.io.BufferedReader;
import java.io.IOException;
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
import io.restassured.specification.RequestSpecification;

public class TC_01_Create_New_Task_List {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void TC_01_Create_New_Task_List() throws Exception {
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();

		// Get key Values from properties file.
		String tasksListBaseURL = prop.setConfig("TasksListBaseURL");
		String accessToken = util.getTaskUrl();
		try {
			if (accessToken != null && !accessToken.isEmpty()) {
				JSONObject createTaskPayload = util.getJSONObjectFromFilePath(FilePathConstants.TASK_CREATE_PAYLOAD);
				String typiCodePayLoadStr = createTaskPayload.toString();
				System.out.println(createTaskPayload);
				URL obj = new URL(tasksListBaseURL);
				HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
				postConnection.setRequestMethod("POST");
				postConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
				postConnection.setDoOutput(true);
				OutputStream os = postConnection.getOutputStream();
				os.write(typiCodePayLoadStr.getBytes());
				os.flush();
				os.close();

				int responseCode = postConnection.getResponseCode();
				Reporter.log("POST Response Code :  " + responseCode);
				Assert.assertEquals(postConnection.getResponseCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.POST NOT WORKED ");
				Reporter.log("POST Response Message : " + postConnection.getResponseMessage());
				Reporter.log("POST Worked ");

				// Validate status code.
				BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print result
				Reporter.log(response.toString());
				System.out.println("Response :" + response.toString());

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
