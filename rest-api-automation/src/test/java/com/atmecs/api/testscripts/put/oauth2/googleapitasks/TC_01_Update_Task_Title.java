package com.atmecs.api.testscripts.put.oauth2.googleapitasks;

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
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import com.atmecs.api.utility.STATUS_CODE;

public class TC_01_Update_Task_Title {
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void updateTasktitle() throws Exception {
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();

		// Get key Values from properties file.
		String taskUpdateURL = prop.setConfig("TasksListUpdateURL");
		RequestSpecification httpRequest = RestAssured.given();

		String refresh_token = "1//0gr__DItZBOkjCgYIARAAGBASNwF-L9IrE6azvf-NPXqK_engsUezRGVA0Z32MqgsXnpkC_m_bi3xcrbA393xznjCueVOmf15zFc";
		String tokenUrl = prop.setKey("tokenUrl");
		String clientId = prop.setKey("client_id");
		String clientSecret = prop.setKey("client_secret");
		String tasksResponseTypeVal = prop.setKey("tasksResponseTypeVal");
		String tasksAccessTypeVal = prop.setKey("tasksAccessTypeVal");
		String tasksScopeVal1 = prop.setKey("tasksScopeVal1");
		String tasksScopeVal2 = prop.setKey("tasksScopeVal2");
		String redirectUriVal = prop.setKey("redirectUriVal");
		String taskListID = prop.setKey("taskListID");
		String expectedTokenVal = prop.setKey("expectedTokenVal");

		String grantType = "refresh_token";
		String accessToken = util.frameURLAndGetAccessToken(tokenUrl, clientId, clientSecret, refresh_token, grantType,
				tasksResponseTypeVal, tasksAccessTypeVal, redirectUriVal, tasksScopeVal1, tasksScopeVal2, "", "");

		try {
			JSONObject updateTaskPayLoad = util.getJSONObjectFromFilePath(FilePathConstants.TASK_UPDATE_PAYLOAD);
			String typiCodePayLoadStr = updateTaskPayLoad.toString();
			System.out.println(updateTaskPayLoad);
			URL obj = new URL(taskUpdateURL);
			HttpURLConnection putConnection = (HttpURLConnection) obj.openConnection();
			putConnection.setRequestMethod("PUT");
			putConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
			putConnection.setDoOutput(true);
			OutputStream os = putConnection.getOutputStream();
			os.write(typiCodePayLoadStr.getBytes());
			os.flush();
			os.close();

			int responseCode = putConnection.getResponseCode();
			System.out.println("PUT Response Code :  " + responseCode);
			System.out.println("PUT Response Message : " + putConnection.getResponseMessage());
			Reporter.log("PUT Worked ");
			if (putConnection.getResponseCode() == STATUS_CODE.STATUS_200.getValue()) {
				// Validate status code.
				BufferedReader in = new BufferedReader(new InputStreamReader(putConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print result
				Reporter.log(response.toString());
				System.out.println("Response :" + response.toString());
				String actualTitleVal = JsonPath.read(response.toString(), "$.title");
				System.out.println("Token = " + actualTitleVal);
				Assert.assertEquals(actualTitleVal, expectedTokenVal);

			} else {
				System.out.println("PUT NOT WORKED");
			}

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
