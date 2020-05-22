package com.atmecs.practice.automation.testscripts.dd;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/*TDD Example : Password Validations
We(Tester) define some criteria of password validations
before the actual requirement defined by client or before 
development. On our own imagination, will write some
validations.
For example : Here as part of validations, we checked
password should contains one special characters.
But if their is any change in client requirement 
like password should contains one special characters
then certain re-factoring of code is required
*/

public class SampleTDDExample {
	SoftAssert softAssert = new SoftAssert();
	String password = "a9$P8895";

	/**
	 * Check and validates the password length.
	 */
	public void chkPswdlength() {
		Validator vald = new Validator();
		softAssert.assertEquals(true, vald.isPswdLength(password));
	}

	/**
	 * Check and validates the Upper Case 
	 * lettes in Password String
	 */
	public void chkUpperCase() {
		Validator vald = new Validator();
		softAssert.assertEquals(true, vald.isContainsAtleatOneUpperCase(password));
	}

	/**
	 * Check and validates the Lower Case 
	 * lettes in Password String
	 */
	public void chkLowerCase() {
		Validator vald = new Validator();
		softAssert.assertEquals(true, vald.isContainsAtleatOneLowerCase(password));
	}

	/**
	 * Check and validates the Numbers
	 * in Password String
	 */
	public void chkNumber() {
		Validator vald = new Validator();
		softAssert.assertEquals(true, vald.isContainsAtleatOneNumber(password));
	}
	
	/**
	 * Check and validates the Special Case
	 * Characters in Password Strig
	 */
	public void chkSpecChar() {
		Validator vald = new Validator();
		softAssert.assertEquals(true, vald.isContainsAtleatOneSpecChar(password));
	}

	/**
	 * Performs overall password validations.
	 */
	@Test
	public void pswdValidations() {
		this.chkPswdlength();
		this.chkLowerCase();
		this.chkUpperCase();
		this.chkNumber();
		this.chkSpecChar();
	}

}
