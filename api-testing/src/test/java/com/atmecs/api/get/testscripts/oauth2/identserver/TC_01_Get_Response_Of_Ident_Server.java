package com.atmecs.api.get.testscripts.oauth2.identserver;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * 
 * @author Kasi.Batchu This class is written to get response from ident server
 *         using oauth2 authentication.
 */
public class TC_01_Get_Response_Of_Ident_Server {
	static final Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	// Returns a new token every time when it is been called.
	private String getClientCredentials() throws FileNotFoundException, Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		// Load specified property file.
		prop.loadProperty(tdf);

		// Load config
		prop.loadConfig();

		// Get key Values from properties file.
		String tokenUrl = prop.setConfig("tokenurl");
		String clientId = prop.setKey("clientIdVal");// clientId
		String clientSecret = prop.setKey("secretIdVal");// client secret
		String auth = clientId + ":" + clientSecret;
		String authentication = Base64.getEncoder().encodeToString(auth.getBytes());
		String urlencoded = prop.setKey("urlencoded");
		String jsonaccepts = prop.setKey("jsonaccepts");
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

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             Get Response Using Oauth2 Authentication.
	 */
	@Test(enabled = false)
	public void getResponseUsingOAuth2() throws FileNotFoundException, Exception {

		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);

			// Load config
			prop.loadConfig();

			String token = getClientCredentials();
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");

			String BaseidentityServerURi = prop.setConfig("BaseidentityServerURi");
			Response response = given().auth().oauth2(token).when().get(BaseidentityServerURi);
			if (response != null) {
				// Validate status code.
				Reporter.log("Status Code: " + response.getStatusCode());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");

				String actContentType = response.header("Content-Type");
				String actContentEncoding = response.header("Content-Encoding");
				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);
			}

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(enabled = false)
	public void getAndValidateTypeClientExistence() throws FileNotFoundException, Exception {
		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Load config
			prop.loadConfig();

			String token = getClientCredentials();
			String BaseidentityServerURi = prop.setConfig("BaseidentityServerURi");
			String types = prop.setKey("types");
			boolean clientIDVal = false;

			Response response = given().auth().oauth2(token).when().get(BaseidentityServerURi);
			if (response != null) {
				// Validate status code.
				Reporter.log("Status Code: " + response.getStatusCode());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");
				JSONArray typeList = JsonPath.read(response.asString(), types);

				Reporter.log("Validate Client Id Type existence in Type List");
				for (int i = 0; i < typeList.size(); i++) {
					String type = (String) typeList.get(i);
					boolean actualTypeclientIdExists = type.equals("client_id");
					boolean expectedTypeclientIdExists = true;
					int compVal = Boolean.compare(actualTypeclientIdExists, expectedTypeclientIdExists);
					if (compVal == 0) {
						clientIDVal = true;
						Reporter.log("Client ID exists in type list as expected");
					}
				}

				Assert.assertTrue(clientIDVal, "Client ID Type should exists in the type list");
			}
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(enabled = false)
	public void getScopeValueFromList() {
		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);
			// Load config
			prop.loadConfig();
			String token = getClientCredentials();
			String BaseidentityServerURi = prop.setConfig("BaseidentityServerURi");
			// String expScope = prop.setKey("scopeValExp");

			Response response = given().auth().oauth2(token).when().get(BaseidentityServerURi);
			System.out.println(response);
			if (response != null) {
				// Validate status code.
				Reporter.log("Status Code: " + response.getStatusCode());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");
				String exprScopeForVal = prop.setKey("exprScopeForVal");
				JSONArray scopeIDvalue = JsonPath.read(response.asString(), exprScopeForVal);

				String actualScopeIdVal = scopeIDvalue.toString();
				actualScopeIdVal = actualScopeIdVal.replaceAll("\\[", "").replaceAll("\\]", "");
				String expScopeVal = prop.setKey("expScopeVal");

				Assert.assertTrue(actualScopeIdVal.contains(expScopeVal),
						"Scope with expected value not exists" + expScopeVal);
			}

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Test(enabled = false)
	// Returns a new token every time when it is been called.
	private void getAccessToken() throws FileNotFoundException, Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		// Load specified property file.
		prop.loadProperty(tdf);

		// Load config
		prop.loadConfig();

		RequestSpecification request = RestAssured.given();
		/*
		 * Response tokenResponse =
		 * request.post("https://accounts.google.com/o/oauth2/token" +
		 * "?client_id=968534806332-lsslb2earlechlpaud4rn217k26jn66c.apps.googleusercontent.com"
		 * + "&client_secret=ULkI1XOSo17NFMB3h9BRzmIX"+
		 * "&grant_type=authorization_code"+ //
		 * "&refresh_token=ya29.a0AfH6SMC1Q7TZMAEi73qNKkurHniSUtRal15_TqsQ14UfFBu4uln0JG55_ZdIyElDlMjTsm3v3oJ4tylJU2zMn2emiaperrP6tm6qgkMha66R8klfau5ujWUK14k4gz7msiTVcjBWEPdFxckAXlVKaZDHCc10WKKgmEc"+
		 * "&code=4/0QHNb1sC76qEhU3uUTcUqf33tTGd3-a-6vk1BoH89NDcRBJyyUXjCSTzWHsgfCZsJLIZx_TP_czkZdt9qB142V8"
		 * + "&response_type=code" + "&access_type=offline"+
		 * "&scope=https://www.googleapis.com/auth/tasks" + //
		 * "&auth_url=https://accounts.google.com/o/oauth2/auth"+
		 * "&redirect_uri=http://localhost:8080"); System.out.println(tokenResponse
		 * .asString()); //Refresh_token val =
		 * 1//0gr__DItZBOkjCgYIARAAGBASNwF-L9IrE6azvf-
		 * NPXqK_engsUezRGVA0Z32MqgsXnpkC_m_bi3xcrbA393xznjCueVOmf15zFc
		 * 
		 * //Extract Token from Response String accessToken =
		 * JsonPath.read(tokenResponse.asString(),"$.access_token");
		 * System.out.println("Token = " + tokenResponse.asString());
		 */

		Response tokenResponse = request.post("https://accounts.google.com/o/oauth2/token"
				+ "?client_id=968534806332-lsslb2earlechlpaud4rn217k26jn66c.apps.googleusercontent.com"
				+ "&client_secret=ULkI1XOSo17NFMB3h9BRzmIX" + "&grant_type=refresh_token"
				+ "&refresh_token=1//0gr__DItZBOkjCgYIARAAGBASNwF-L9IrE6azvf-NPXqK_engsUezRGVA0Z32MqgsXnpkC_m_bi3xcrbA393xznjCueVOmf15zFc"
				+
				// "&code=4/0QHNb1sC76qEhU3uUTcUqf33tTGd3-a-6vk1BoH89NDcRBJyyUXjCSTzWHsgfCZsJLIZx_TP_czkZdt9qB142V8"
				// +
				"&response_type=code" + "&access_type=offline" + "&scope=https://www.googleapis.com/auth/tasks"
				+ "&scope=https://www.googleapis.com/auth/tasks.readonly" +
				// "&auth_url=https://accounts.google.com/o/oauth2/auth"+
				"&redirect_uri=http://localhost:8080");

		String accessToken = JsonPath.read(tokenResponse.asString(), "$.access_token");
		System.out.println("Token = " + accessToken);

		/*
		 * // Provide token as an OAuth2 parameter to rest-assured call Response
		 * response = given().auth().oauth2(accessToken).when()
		 * .get("https://www.googleapis.com/tasks/v1/users/@me/lists");
		 * System.out.println(response.asString());
		 */

		// Operation to perform response from request.
		Response rr = request.queryParam("access_token",
				"ya29.a0AfH6SMBZcdZStEOw1iw5C-SAF9i6K3q6dxrz1PRDY8Tzf9DxpQRnFdLzde5sVRB4pplkaBlo-uCFA7Bim23vI9VIVkuoRStlRym_Mw4yLUG7jeNu_afJlrKliIMcgvYMKXdIg8zDuiPCsGGgJBUr6cXGKvdeQzWHSHBt4F9H")
				.get("https://www.googleapis.com/tasks/v1/users/@me/lists");

		// Convert response to String
		String jsonString = rr.asString();

		// Validate status code.
		Assert.assertEquals(rr.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
				"INFO: Status Code Validation Failed.");

	}

	@Test(enabled = true)
	public void postTask() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();
		String refresh_token = "1//0gr__DItZBOkjCgYIARAAGBASNwF-L9IrE6azvf-NPXqK_engsUezRGVA0Z32MqgsXnpkC_m_bi3xcrbA393xznjCueVOmf15zFc";
		String tokenUrl = prop.setKey("tokenUrl");
		String clientId = prop.setKey("client_id");
		String clientSecret = prop.setKey("client_secret");
		String tasksResponseTypeVal = prop.setKey("tasksResponseTypeVal");
		String tasksAccessTypeVal = prop.setKey("tasksAccessTypeVal");
		String tasksScopeVal1 = prop.setKey("tasksScopeVal1");
		String tasksScopeVal2 = prop.setKey("tasksScopeVal2");
		String redirectUriVal = prop.setKey("redirectUriVal");
		String grantType = "refresh_token";
		String accessToken=frameURLAndGetAccessToken(tokenUrl, clientId, clientSecret, refresh_token, grantType, tasksResponseTypeVal,
				tasksAccessTypeVal, redirectUriVal, tasksScopeVal1, tasksScopeVal2, "", "");
		
		// Get key Values from properties file.
		String typiCodeBaseURI = prop.setConfig("TypiCodeBaseURI");
		String useridval = prop.setConfig("useridval");
		String jsonaccepts = prop.setConfig("jsonaccepts");
		try {
			org.json.simple.JSONObject typiCodePayLoad = Util.getJSONObjectFromFilePath(FilePathConstants.TASK_CREATE_PAYLOAD);
			String typiCodePayLoadStr = typiCodePayLoad.toString();
		//	JSONObject typiCodePayLoad = Util.getJSONObjectFromFilePath(FilePathConstants.TYPICODE_PAYLOAD);
		//	Util.getJSONObjectFromFilePath(FilePathConstants.TASK_CREATE_PAYLOAD);
	//		String typiCodePayLoadStr = Util.getJSONObjectFromFilePath(FilePathConstants.TASK_CREATE_PAYLOAD).toString();
			System.out.println(typiCodePayLoadStr);
			URL obj = new URL("https://www.googleapis.com/tasks/v1/users/@me/lists");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("access_token", accessToken);
			//postConnection.setRequestProperty("Content-Type", jsonaccepts);
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

	// @Test(enabled = false)
	private String frameURLAndGetAccessToken(String tokenUrl, String clientId, String clientSecret,
			String refreshTokenVal, String grantTypeVal, String tasksResponseTypeVal, String tasksAccessTypeVal,
			String redirectUriVal, String tasksScopeVal1, String tasksScopeVal2, String tasksScopeVal3,
			String tasksScopeVal4) {

		String url = tokenUrl + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&refresh_token="
				+ refreshTokenVal + "&grant_type=" + grantTypeVal + "&response_type=" + tasksResponseTypeVal
				+ "&access_type=" + tasksAccessTypeVal + "&redirect_uri=" + redirectUriVal + "&scope=" + tasksScopeVal1
				+ "&scope=" + tasksScopeVal2 + "&scope=" + tasksScopeVal3 + "&scope=" + tasksScopeVal4;
		System.out.println(url);

		RequestSpecification request = RestAssured.given();
		Response tokenResponse = request.post(url);
		Assert.assertEquals(tokenResponse.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
				"INFO: Status Code Validation Failed.");
		
		String accessToken = JsonPath.read(tokenResponse.asString(), "$.access_token");
		System.out.println("Token = " + accessToken);
		return accessToken;
	}

}
