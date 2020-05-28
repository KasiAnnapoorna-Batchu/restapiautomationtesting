package com.atmecs.utility.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.atmecs.api.utility.FilePathConstants;
import org.testng.Reporter;

/**
 * 
 * @author Kasi.Batchu
 *
 * This class is created to parse property file.
 * 
 */

public class PropertiesParsers {
	public static Properties LCF = null;
	public static Properties PROPFILE = null;

	/**
	 * Load Config file. Here it is config.properties file.
	 */
	public void loadConfig() throws Exception, FileNotFoundException {

		LCF = new Properties();
		try {

			// Read config
			FileReader cf = new FileReader(FilePathConstants.CONFIG_FILE_PATH);
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
	public String setConfig(String key) {

		// return setConfig value.
		return LCF.getProperty(key);
	}

	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 * 
	 *             Loads property file
	 */
	public void loadProperty(String filePath) throws IOException {
		PROPFILE = new Properties();
		FileReader ip = new FileReader(filePath);
		PROPFILE.load(ip);

	}

	/**
	 * 
	 * @param key
	 * @return returns key value from the specified property file.
	 * 
	 */

	public static String setKey(String key) {

		return PROPFILE.getProperty(key);

	}

}
