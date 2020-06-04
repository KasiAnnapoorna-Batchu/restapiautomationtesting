package com.atmecs.api.testscripts.get.oauth2.googleapitasks;

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

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class TC_01_Get_Tasks_Lists {

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void getListofTasks() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String accessToken = util.getTaskUrl();
		// Get key Values from properties file.
		String tasksListBaseURL = prop.setConfig("TasksListBaseURL");

		URL obj = new URL(tasksListBaseURL);
		Reporter.log("Base Url :" + tasksListBaseURL);
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
		Reporter.log(response.toString());

	}

}
