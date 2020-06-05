package com.atmecs.api.utility;

import java.io.File;

/**
 * 
 * @author Kasi.Batchu
 *
 * Used to obtain config and test data file paths.
 * 
 */
public class FilePathConstants {
	/**
	 * Static Strings that stores config and test data file paths.
	 */
	public static final String USER_DIR = System.getProperty("user.dir");

	public static final String CONFIG_FILE_PATH = USER_DIR + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator + "config.properties";

	public static final String TESTDATA_FILE_PATH = USER_DIR + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator + "testdata.properties";
	public static final String TYPICODE_PAYLOAD = USER_DIR + File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "testdata"+ File.separator+ "postpayload"+ File.separator + "typicodepayload.json";
	public static final String TASK_CREATE_PAYLOAD = USER_DIR + File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "testdata"+ File.separator+ "postpayload"+ File.separator + "taskcreatepayload.json";
	public static final String TASK_UPDATE_PAYLOAD = USER_DIR + File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "testdata"+ File.separator+ "postpayload"+ File.separator + "taskupdatepayload.json";
	
	public static final String CREATE_RESOURCE_PAYLOAD = USER_DIR + File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "testdata"+ File.separator+ "postpayload"+ File.separator + "createresourcepayload.json";
	
	public static final String CREATE_DRAFT_PAYLOAD = USER_DIR + File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "testdata"+ File.separator+ "postpayload"+ File.separator + "createdraftpayload.json";
	public static final String RESPONCE_DATA = USER_DIR + File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "testdata"+ File.separator+ "postpayload"+ File.separator + "responsedata.json";
	
	

}
