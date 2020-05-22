package com.atmecs.practice.automation.testscripts.dd;

import java.io.FileNotFoundException;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.atmecs.practice.automation.common.ReusableFunctions;

/**
 * Mobile number validation: Given/Entered a few numbers on the mobile number
 * field. When we give 10 numbers and hit enter Then a mobile number validation
 * is success. Behavior is usually defined by client. For Example: Client
 * Requirements: Mobile Number Length - 10 Digits and it doesn't contain any
 * special characters.
 */
public class SampleBDDExample {
	long mobNo = 12345678910L;

	SoftAssert softAssert = new SoftAssert();

	/**
	 * Check and validate the length of the Mob Num.
	 */
	public void chkMobileNumberlength() throws Exception {

		Validator vald = new Validator();

		softAssert.assertEquals(true, vald.isMobileNoLengthValid(mobNo));
	}
	
	/**
	 * Check and validate whether Mob No contains any Characters.
	 */
	public void chkMobileNoContainsChar() throws Exception {

		Validator vald = new Validator();

		softAssert.assertEquals(true, vald.isMobileNoContainsChar(mobNo));
	}

	/**
	 * Check and validate whether Mob No contains any Special
	 * Characters.
	 */
	public void chkMobileNoContainsSpecialChar() throws Exception {

		Validator vald = new Validator();

		softAssert.assertEquals(true, vald.isMobileNoContainsSpecChar(mobNo));

	}

	/**
	 * Performs Overalll Mobile Number Validations and Executes.
	 */
	@Test
	public void performMobileNoValidations() throws Exception {
		try {
			this.chkMobileNumberlength();
			this.chkMobileNoContainsSpecialChar();
			this.chkMobileNoContainsChar();
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
