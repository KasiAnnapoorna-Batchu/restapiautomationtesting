package com.atmecs.api.get.testscripts.noauth.googleapibooks;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.api.utility.Util;
import com.atmecs.utility.parser.PropertiesParsers;
import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;

/**
 * 
 * @author Kasi.Batchu This class is written to get books max count.
 */
public class TC_02_Get_Books_Max_Result {
	
	PropertiesParsers prop = new PropertiesParsers();
	Util util = new Util();

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             Get Books Maximum count.
	 */
	
	@Test
	public void getAndTestBooksMaxCount() throws FileNotFoundException, Exception {
		try {

			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			// Load specified property file.
			prop.loadProperty(tdf);

			// Load config
			prop.loadConfig();
			// Get key Values from properties file.
			String booksBaseURI = prop.setConfig("BooksBaseURI");
			String paramdan = prop.setKey("danbrown");
			String maxcount = prop.setKey("maxcount");
			String items = prop.setKey("items");
			String expContectType = prop.setKey("contectType");
			String expContEncoding = prop.setKey("Content-Encoding");

			// Create Request.
			// Operation to get response from request.
			Reporter.log(" Create Request to get response ");

			Response response = given().param("q", paramdan).param("maxResults", maxcount).when().get(booksBaseURI);
			if (response != null) {
				// Validate status code.
				Reporter.log("Status Code: " + response.getStatusCode());
				Assert.assertEquals(response.getStatusCode(), STATUS_CODE.STATUS_200.getValue(),
						"INFO: Status Code Validation Failed.");

				// Validate Response Headers.
				Reporter.log("Validating Content Type & Content Encoding Values");
				String actContentType = response.header("Content-Type");
				String actContentEncoding = response.header("Content-Encoding");

				util.validateResponseHeaders(actContentType, actContentEncoding, expContectType, expContEncoding);

				List<Object> allBooks = JsonPath.read(response.asString(), items);
				Reporter.log("Validating actual books size with expected");				
				Reporter.log("Actual Books size : "+allBooks.size());
								
				// Validates actual max book count with expected.
				Assert.assertEquals(allBooks.size(), 25, "25 Books should be returned");
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
