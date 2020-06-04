package com.atmecs.api.testscripts.get.oauth2.googleapitasks;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import io.restassured.http.Method;
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
public class TC_01_Get_Ident_Server_List {
	
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	// Returns a new token every time when it is been called.
		/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             Get Response Using Oauth2 Authentication.
	 */
	@Test(enabled = false)
	public void TC_01_Get_Ident_ServerList() throws FileNotFoundException, Exception {

		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);

			// Load config
			prop.loadConfig();

			String token = util.getTokenByClientCredentialsAuth();
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

	/*@Test(enabled = false)
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

	@Test(enabled = true)
	public void getListofTasks() throws Exception {

		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		prop.loadProperty(tdf);
		// Load config
		prop.loadConfig();

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
		String grantType = "refresh_token";
		String accessToken = util.frameURLAndGetAccessToken(tokenUrl, clientId, clientSecret, refresh_token, grantType,
				tasksResponseTypeVal, tasksAccessTypeVal, redirectUriVal, tasksScopeVal1, tasksScopeVal2, "", "");

		// Get key Values from properties file.
		String typiCodeBaseURI = prop.setConfig("TypiCodeBaseURI");

		URL obj = new URL(typiCodeBaseURI);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + accessToken);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} else

		{
			System.out.println("GET Response Code :: " + responseCode);
			System.out.println("GET request not worked");

		}
	}
*/
}
