package com.atmecs.api.get.testscripts.oauth2.identserver;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.utility.parser.PropertiesParsers;

import io.restassured.response.Response;

public class TC_01_Get_Response_Of_Ident_Server {
	private static final Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");

	static PropertiesParsers cf = new PropertiesParsers();

	private static String getClientCredentials() throws FileNotFoundException, Exception {
		
		// Load config
		cf.loadConfig();
		// Get key Values from properties file.
		String tokenUrl = cf.setConfig("tokenurl");
		String clientId = cf.setConfig("clientIdVal");// clientId
		String clientSecret = cf.setConfig("secretIdVal");// client secret
		String auth = clientId + ":" + clientSecret;
		String authentication = Base64.getEncoder().encodeToString(auth.getBytes());
		String urlencoded = cf.setConfig("urlencoded");
		String jsonaccepts = cf.setConfig("jsonaccepts");

		String content = "grant_type=client_credentials";
		BufferedReader reader = null;
		HttpsURLConnection connection = null;
		String returnValue = "";
		
		try {
			URL url = new URL(tokenUrl);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + authentication);
			connection.setRequestProperty("Content-Type", urlencoded);
			connection.setRequestProperty("Accept", jsonaccepts);
			PrintStream os = new PrintStream(connection.getOutputStream());
			os.print(content);
			os.close();
			
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			StringWriter out = new StringWriter(
					connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			String response = out.toString();
			Matcher matcher = pat.matcher(response);
			// check whether the matcher matches as per the specified pattern.
			
			if (matcher.matches() && matcher.groupCount() > 0) {
				returnValue = matcher.group(1);
			}
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			// Disconnect.
			connection.disconnect();
		}
		return returnValue;
	}

	@Test
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 * Get Response Using Oauth2 Authentication.
	 */
	private static void getResponseUsingOAuth2() throws FileNotFoundException, Exception {
		try {
			String token = getClientCredentials();
			String BaseidentityServerURi = cf.setConfig("BaseidentityServerURi");
			String response = given().auth().oauth2(token).when().get(BaseidentityServerURi).getBody().asString();			
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
