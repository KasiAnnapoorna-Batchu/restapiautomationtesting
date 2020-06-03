package com.atmecs.api.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;

import com.jayway.jsonpath.JsonPath;

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

	public static JSONObject getJSONObjectFromFilePath(String filePath)
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
		if (actualcontType.contains("charset=UTF-8") || actualcontType.contains("charset=utf-8"));
		{
			Reporter.log("Found charset= UTF-8 in content type");
			actualcontType = actualcontType.replaceAll("\\ charset=UTF-8", "");
			actualcontType = actualcontType.replaceAll("\\ charset=utf-8", "");
		}
		Reporter.log("Actaul Content Type : " + actualcontType);
		Reporter.log("Expcted Content Type :"+expContectType);
		Assert.assertEquals(actualcontType /* actual value */, expContectType /* expected value */);

		// Reader header of a give name. In this line we will get
		// Header named Content-Encoding
		Reporter.log("Actual Content Encoding : " + actualContEncoding);
		Reporter.log("Expected Content Encoding Val" + expContEncoding);
		Assert.assertEquals(actualContEncoding /* actual value */, expContEncoding /* expected value */);
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
