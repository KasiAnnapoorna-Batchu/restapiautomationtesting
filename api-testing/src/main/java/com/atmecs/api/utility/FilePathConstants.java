package com.atmecs.api.utility;

import java.io.File;

/**
 * 
 * @author Kasi.Batchu
 *
 *         Used to obtain config and test data file paths.
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

}
