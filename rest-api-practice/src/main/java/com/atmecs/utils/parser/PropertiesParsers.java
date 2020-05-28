package com.atmecs.utils.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.testng.Reporter;

public class PropertiesParsers {
	public static Properties LCF = null;
	public static Properties PROPFILE = null;
	
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
	
	public void loadProperty(String filePath) throws IOException{
		PROPFILE = new Properties();
		FileReader ip =new FileReader(filePath);
		System.out.println(ip+".......");
		PROPFILE.load(ip);
	
	}
	
	public static String setKey(String key) {
		
		return PROPFILE.getProperty(key);
		
	}
}
