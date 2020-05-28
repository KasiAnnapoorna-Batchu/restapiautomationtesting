package com.atmecs.utils.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;

public class JsonParser {

	private JSONParser parser = new JSONParser();
	public static String jsonString;

	/**
	 * Reads JSON file and parses the file and converts into JSON object
	 * 
	 * @param jsonFile
	 *            is the File Object converted from JSON file
	 * @throws Exception
	 * @returns JSONObject
	 */
	public String getAndReadJsonFile(String JsonFilePath) throws FileNotFoundException, IOException, ParseException {

		try {
						
			// Validates whether the file type is JSON or not.
			if (!JsonFilePath.contains(".json")) {
				throw new Exception("File Type is not JSON");
			}
			Object obj = new JSONParser().parse(new FileReader(JsonFilePath));
			jsonString = obj.toString();
			return jsonString;

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (ParseException pe) {
			Reporter.log("File is not found in specified path: " + pe.getMessage());Reporter.log(
					"Parsing from file data to JSONObject is not take place properly: " + pe.getMessage());

			pe.printStackTrace();
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

		return jsonString;

	}
}
