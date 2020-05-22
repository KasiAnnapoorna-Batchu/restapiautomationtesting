package com.atmecs.practice.automation.common;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;

import com.jayway.jsonpath.JsonPath;

/**
 * CommonFunctions is a class where it contains all 
 * commonly used functions/reusable functions.
 */
public class ReusableFunctions {
	public static Properties LCF = null;
	public static Properties TDF = null;
	public static String jsonString;

	/**
	 *  Load Config file. Here it is config.properties file.
	 */
	public void loadConfig() throws Exception, FileNotFoundException {

		LCF = new Properties();
		try {
			// config
			String configPath = "//common//config//config.properties";
			// Read config
			FileReader cf = new FileReader((System.getProperty("user.dir") + configPath));
			// Load config
			LCF.load(cf);

		} catch (FileNotFoundException fileNotFoundException) {
			Reporter.log("File is not found in specified path: " + fileNotFoundException.getMessage());
			fileNotFoundException.printStackTrace();
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Returns specified key value from specified property file Here LCF is meant
	 * for property file which is config.
	 */
	public static String setConfig(String key) {

		// return setConfig value.
		return LCF.getProperty(key);
	}

	/**
	 * Creates a new File instance
	 * 
	 * @param pathname
	 *            is the absolute path of the test data file
	 * @throws Exception
	 * @returns File object of the test data file
	 */

	public File getFile(String pathname) throws Exception {
		try {
			if (pathname != null) {
				File file = new File(pathname);
				return file;
			}

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the JSON file and Reads it.
	 */

	public String getAndReadJsonFile(String JsonFilePath) throws FileNotFoundException, IOException, ParseException {

		try {
			
			//load config file by using loadConfig() method 
			
			this.loadConfig();
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
			Reporter.log("File is not found in specified path: " + pe.getMessage());
			pe.printStackTrace();
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

		return jsonString;

	}

	/**
	 * Compiles JsonPath with expression that is passed.
	 */
	public JsonPath jsonCompile(String expression) {
		// Compiles expression that is being passed.
		JsonPath jsonValue = JsonPath.compile(expression);
		return jsonValue;

	}

	}
