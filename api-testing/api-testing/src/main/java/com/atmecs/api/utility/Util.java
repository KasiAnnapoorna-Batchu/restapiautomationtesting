package com.atmecs.api.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;

import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * 
 * @author Kasi.Batchu
 *
 *         This class is created to code some reusable/common functions that are
 *         frequently been used.
 */

public class Util {

	/**
	 * Compiles JsonPath with expression that is passed.
	 */
	PropertiesParsers prop = new PropertiesParsers();
	

	public JsonPath jsonCompile(String expression) {
		// Compiles expression that is being passed.
		JsonPath jsonValue = JsonPath.compile(expression);
		return jsonValue;

	}

	// This is utility Method which returns JSONObject from String
	// representation of JSON.
	public static JSONObject getJSONObjectfromString(String json) throws ParseException {
		return (JSONObject) new JSONParser().parse(json);
	}

	public JSONObject getJSONObjectFromFilePath(String filePath)
			throws FileNotFoundException, IOException, ParseException {
		return (JSONObject) new JSONParser().parse(new FileReader(filePath));
	}

	public String validateContentType(String contType) {

		// String contentType = response.header("Content-Type");
		if (contType.contains("charset=UTF-8"))
			;
		{
			Reporter.log("Found charset= UTF-8 in content type");
			contType = contType.replaceAll("\\ charset=UTF-8", "");
		}
		return contType;
	}

	public void validateResponseHeaders(String actualcontType, String actualContEncoding, String expContectType,
			String expContEncoding) {
		if (actualcontType.contains("charset=UTF-8") || actualcontType.contains("charset=utf-8"))
			;
		{
			Reporter.log("Found charset= UTF-8 in content type");
			actualcontType = actualcontType.replaceAll("\\ charset=UTF-8", "");
			actualcontType = actualcontType.replaceAll("\\ charset=utf-8", "");
		}
		Reporter.log("Actaul Content Type : " + actualcontType);
		Reporter.log("Expcted Content Type :" + expContectType);
		Assert.assertEquals(actualcontType /* actual value */, expContectType /* expected value */);

		// Reader header of a give name. In this line we will get
		// Header named Content-Encoding
		Reporter.log("Actual Content Encoding : " + actualContEncoding);
		Reporter.log("Expected Content Encoding Val" + expContEncoding);
		Assert.assertEquals(actualContEncoding /* actual value */, expContEncoding /* expected value */);
	}

	public String frameURLAndGetAccessToken(String tokenUrl, String clientId, String clientSecret,
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

	public String getTaskUrl() throws Exception {
		try {
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			prop.loadProperty(tdf);
			// Load config
			prop.loadConfig();

			// Get key Values from properties file.
			String tasksListBaseURL = prop.setConfig("TasksListBaseURL");
			RequestSpecification httpRequest = RestAssured.given();

			String taskRefreshTokenVal = prop.setKey("taskRefreshTokenVal");
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
			String accessToken = frameURLAndGetAccessToken(tokenUrl, clientId, clientSecret, taskRefreshTokenVal,
					grantType, tasksResponseTypeVal, tasksAccessTypeVal, redirectUriVal, tasksScopeVal1, tasksScopeVal2,
					"", "");
			return accessToken;
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	public String getTokenByClientCredentialsAuth() throws FileNotFoundException, Exception {
		Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");
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
	 * Mob Num Length validations
	 */
	public static boolean getlength(long mob) {
		int length = String.valueOf(mob).length();
		int mobNolength = length - 1;
		if (mobNolength == 10) {
			System.out.println("Number length is as expected . It is equal to 10. ");
			System.out.println("");
			return true;
		}
		System.out.println("Number length is not as expected. It is not equal to 10");
		System.out.println("");
		return false;

	}

	/**
	 * Mob No Character contains validation.
	 */
	public static boolean NoContainsChar(long num) {
		int length = String.valueOf(num).length();
		String str = String.valueOf(num);

		int count = 0;
		for (int i = 0; i < length - 1; i++) {
			boolean flag = Character.isDigit(str.charAt(i));
			if (flag) {
			} else {
				System.out.println("'" + str.charAt(i) + "' is a letter which is not expected");
				count = count + 1;
			}
		}

		if (count == 0) {
			System.out.println("Doesn't contain any Charcters");
			return true;
		}
		return false;

	}

	/**
	 * Mob No Spec Character contains validation.
	 */
	public static boolean isMobileNoContainsSpecChar(long num) {
		int length = String.valueOf(num).length();
		String str = String.valueOf(num);
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");

		Matcher matcher = pattern.matcher(str);

		if (!matcher.matches()) {
			System.out.println("string '" + str + "' contains special character");
			return false;
		} else {
			System.out.println("string '" + str + "' doesn't contains special character");
			return true;
		}

	}

	/**
	 * Password Length validations.
	 */
	public static boolean isPswdLength(String pswd) {
		if ((pswd.length() >= 8) && (pswd.length() < 15)) {
			System.out.println("Password length is as Expected " + pswd.length());
			return true;
		}
		System.out.println("Password length is not as Expected. See Validations for more inforamtion");
		return false;

	}

	/**
	 * Upper Case Letter Validation.
	 * 
	 * @param pswd
	 * @return
	 */
	public static boolean isContainsAtleatOneUpperCase(String sr) {
		String upperCaseChars = "(.*[A-Z].*)";
		if (!sr.matches(upperCaseChars)) {
			System.out.println(" Should contain atleast one upper case alphabet");
			return false;
		}
		System.out.println(" Contains atleast one upper case alphabet as Expected");
		return true;
	}

	/**
	 * Lower Case Letter Validation
	 * 
	 * @param pswd
	 * @return
	 */

	public static boolean isContainsAtleatOneLowerCase(String sr) {
		String lowerCaseChars = "(.*[a-z].*)";
		if (!sr.matches(lowerCaseChars)) {
			System.out.println("Should contain atleast one lower case alphabet");
			return false;
		}
		System.out.println("Contains atleast one lower case alphabet as Expected");
		return true;
	}

	/**
	 * Number Contaions Validations.
	 * 
	 * @param pswd
	 * @return
	 */
	public static boolean isContainsAtleatOneNumber(String sr) {
		String numbers = "(.*[0-9].*)";
		if (!sr.matches(numbers)) {
			System.out.println("Should contain atleast one number.");
			return false;
		}
		System.out.println("Contains atleast one number as Expected");
		return true;
	}

	/**
	 * Special Char contain Validations.
	 * 
	 * @param pswd
	 * @return
	 */
	public static boolean isContainsAtleatOneSpecChar(String sr) {
		String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
		if (!sr.matches(specialChars)) {
			System.out.println("Password should contain atleast one special character");
			return false;
		}
		System.out.println("Password contains atleast one special character as Expected");
		return true;
	}

}
