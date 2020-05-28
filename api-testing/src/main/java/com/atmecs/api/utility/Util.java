package com.atmecs.api.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.jsonpath.JsonPath;

public class Util {
	
	public JsonPath jsonCompile(String expression) {
		// Compiles expression that is being passed.
		JsonPath jsonValue = JsonPath.compile(expression);
		return jsonValue;

	}
	
	/**
	 * Mob Num Length validatios
	 */
	public static boolean isMobileNoLengthValid(long mob) {
		int length = String.valueOf(mob).length();
		int mobNolength = length - 1;
		if (mobNolength == 10) {
			System.out.println("Mobile Number length is as expected . It is equal to 10. ");
			System.out.println("");
			return true;
		}
		System.out.println("Mobile Number length is not as expected. It is not equal to 10");
		System.out.println("");
		return false;

	}

	/** 
	 * Mob No Character contains validation.
	 */
	public static boolean isMobileNoContainsChar(long mob) {
		int length = String.valueOf(mob).length();
		String str = String.valueOf(mob);

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
			System.out.println("Mobile Number doesn't contain any Charcters");
			return true;
		}
		return false;

	}

	/** 
	 * Mob No Spec Character contains validation.
	 */
	public static boolean isMobileNoContainsSpecChar(long mob) {
		int length = String.valueOf(mob).length();
		String str = String.valueOf(mob);
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
	 *Password Length validations.
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
	 * @param pswd
	 * @return
	 */
	public static boolean isContainsAtleatOneUpperCase(String pswd) {
		String upperCaseChars = "(.*[A-Z].*)";
		if (!pswd.matches(upperCaseChars)) {
			System.out.println("Password should contain atleast one upper case alphabet");
			return false;
		}
		System.out.println("Password contains atleast one upper case alphabet as Expected");
		return true;
	}
	/**
	 * Lower Case Letter Validation
	 * @param pswd
	 * @return
	 */

	public static boolean isContainsAtleatOneLowerCase(String pswd) {
		String lowerCaseChars = "(.*[a-z].*)";
		if (!pswd.matches(lowerCaseChars)) {
			System.out.println("Password should contain atleast one lower case alphabet");
			return false;
		}
		System.out.println("Password contains atleast one lower case alphabet as Expected");
		return true;
	}

	/**
	 * Number Contaions Validations.
	 * @param pswd
	 * @return
	 */
	public static boolean isContainsAtleatOneNumber(String pswd) {
		String numbers = "(.*[0-9].*)";
		if (!pswd.matches(numbers)) {
			System.out.println("Password should contain atleast one number.");
			return false;
		}
		System.out.println("Password contains atleast one number as Expected");
		return true;
	}
	
	/**
	 * Special Char contain Validations.
	 * @param pswd
	 * @return
	 */
	public static boolean isContainsAtleatOneSpecChar(String pswd) {
		   String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
		   if (!pswd.matches(specialChars)) {
			   System.out.println("Password should contain atleast one special character");
				return false;
			}
			System.out.println("Password contains atleast one special character as Expected");
			return true;
	}


}
