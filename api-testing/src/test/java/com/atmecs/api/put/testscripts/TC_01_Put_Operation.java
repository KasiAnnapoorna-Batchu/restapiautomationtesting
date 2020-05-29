package com.atmecs.api.put.testscripts;

import java.io.FileNotFoundException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.atmecs.api.utility.FilePathConstants;
import com.atmecs.api.utility.STATUS_CODE;
import com.atmecs.utility.parser.PropertiesParsers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_01_Put_Operation {
	PropertiesParsers cf = new PropertiesParsers();

	@Test
	public void putUpdateRecords() throws FileNotFoundException, Exception {

		cf.loadConfig();
		String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
		// Load specified property file.
		cf.loadProperty(tdf);
		int id = 2074;

		String CherchertechBaseURI = cf.setConfig("CherchertechBaseURI");
		RestAssured.baseURI = CherchertechBaseURI;
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();

		String nameToUpdate = cf.setKey("nameToUpdate");

		String descriptionToUpdate = cf.setKey("descriptionToUpdate");
		System.out.println(nameToUpdate);
		System.out.println(descriptionToUpdate);
		requestParams.put("name", nameToUpdate);

		requestParams.put("description", descriptionToUpdate);

		request.body(requestParams.toJSONString());
		Response response = request.put("/update?id=" + id);

		System.out.println(response.getStatusLine());
		int statusCode = response.getStatusCode();
		System.out.println(statusCode);

		System.out.println(response.asString());
		Assert.assertEquals(statusCode, STATUS_CODE.STATUS_201.getValue(), "INFO: Status Code Validation Failed.");

	}

}
