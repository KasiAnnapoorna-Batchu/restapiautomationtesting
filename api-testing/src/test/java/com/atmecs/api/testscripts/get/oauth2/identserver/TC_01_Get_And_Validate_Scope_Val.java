package com.atmecs.api.testscripts.get.oauth2.identserver;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

import io.restassured.response.Response;

public class TC_01_Get_And_Validate_Scope_Val {

	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	@Test
	public void TC_01_Get_And_Validate_Scope_Val() {
		
			try {
				String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
				// Load specified property file.
				prop.loadProperty(tdf);
				// Load config
				prop.loadConfig();

				String token = util.getTokenByClientCredentialsAuth();
				String expContectType = prop.setKey("contectType");
				String expContEncoding = prop.setKey("Content-Encoding");
				String types = prop.setKey("types");
				boolean clientIDVal = false;

				String BaseidentityServerURi = prop.setConfig("BaseidentityServerURi");
				Response response = given().auth().oauth2(token).when().get(BaseidentityServerURi);
				if (response != null) {
					// Validate status code.
					Reporter.log("Validating Status Code");
					Reporter.log("Actual Status Code :" + response.getStatusCode());
					Reporter.log("Expected Status Code:  " + STATUS_CODE.STATUS_200.getValue());
					Reporter.log("Status Code: " + response.getStatusCode());
					Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
							"INFO: Status Code Validation Failed.");

					String actContentType = response.header("Content-Type");
					String actContentEncoding = response.header("Content-Encoding");
					// Validate Response Headers.
					Reporter.log("Validating Content Type & Content Encoding Values");
					util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);
					String exprScopeForVal = prop.setKey("exprScopeForVal");
					JSONArray scopeIDvalue = JsonPath.read(response.asString(), exprScopeForVal);

					String actualScopeIdVal = scopeIDvalue.toString();
					actualScopeIdVal = actualScopeIdVal.replaceAll("\\[", "").replaceAll("\\]", "");
					String expScopeVal = prop.setKey("expScopeVal");
					Reporter.log("Calidating Actual Scope Val with Expected");
					Reporter.log("Actual Scope Val :"+actualScopeIdVal);
					Reporter.log("Expected Scpoe Val : "+expScopeVal);					
					Assert.assertTrue(actualScopeIdVal.contains(expScopeVal),
							"Scope with expected value not exists" + expScopeVal);
					Reporter.log("Scope Value Validation : Passed");
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
}
