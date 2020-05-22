package com.atmecs.practice.automation.testscripts.jsonpath;

import java.io.FileNotFoundException;

import java.util.Iterator;
import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.atmecs.practice.automation.common.ReusableFunctions;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import net.minidev.json.JSONArray;

/**
 * 
 * @author Kasi.Batchu
 * 
 *         Fetches employee details from related json file. Here it is :
 *         Employee_Details_Sample_Json.json file
 */
public class FetchEmployeeDetailsUsingJsonPath {

	ReusableFunctions cf = new ReusableFunctions();

	/**
	 * Loads config and read the specific json file.
	 * 
	 * @param JsonFileName
	 * @return
	 * @throws Exception
	 */
	public String loadConfigAndReadJson(String JsonFileName) throws Exception {
		try {
			cf.loadConfig();
			String JsonFilePath = cf.setConfig(JsonFileName);
			String wholeJsonAsString = cf.getAndReadJsonFile(JsonFilePath);
			return wholeJsonAsString;
		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Get Basic comapny Details.
	 * 
	 * @throws FileNotFoundException
	 * @throws PathNotFoundException
	 * @throws Exception
	 */
	public void getBasicCompanyDetails() throws FileNotFoundException, PathNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get Company Name
			JsonPath companyName = cf.jsonCompile("$.['Company Name']");
			String cn = docCtx.read(companyName);
			System.out.println(cn);

			// Get Company Location
			JsonPath companyLocation = cf.jsonCompile("$.['Company Location']");
			String cl = docCtx.read(companyLocation);
			System.out.println(cl);

			// Get Address
			JsonPath address = cf.jsonCompile("$.Address");
			String ad = docCtx.read(address);
			System.out.println(ad);

			// Get State
			JsonPath state = cf.jsonCompile("$.State");
			String st = docCtx.read(state);
			System.out.println(st);

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Get Company Branches Info.
	 * 
	 * @throws FileNotFoundException
	 * @throws PathNotFoundException
	 * @throws Exception
	 * 
	 */
	public void getBranchesInfo() throws FileNotFoundException, PathNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get Branch Names
			JsonPath branches = cf.jsonCompile("$.Branches");
			Map br = docCtx.read(branches);
			System.out.println("");
			Iterator<Map.Entry> itr1 = br.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pair1 = itr1.next();
				System.out.println(pair1.getKey() + " --> " + pair1.getValue());
			}

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get first Particular Employee Details.
	 * 
	 * @throws FileNotFoundException
	 * @throws PathNotFoundException
	 * @throws Exception
	 * 
	 */

	public void getOnlyParticularEmpDetails() throws FileNotFoundException, PathNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get only Particular Employee Info.

			JsonPath getOneEmpDet = cf.jsonCompile("$..['emp Ids info'].[0]");
			JSONArray empDetails = docCtx.read(getOneEmpDet);
			System.out.println("");
			System.out.println("Overall Details of Employee --> " + empDetails);
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Get Personal Info of an Employee
	 * 
	 * @throws PathNotFoundException
	 * @throws FileNotFoundException
	 * @throws Exception
	 * 
	 */
	public void getPersonalInfo() throws PathNotFoundException, FileNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get only Particular Employee personal Info.

			JsonPath getPersonalInfo = cf.jsonCompile("$..['emp Ids info'].[0].['Personal Info']");
			JSONArray persInfo = docCtx.read(getPersonalInfo);
			System.out.println("");
			System.out.println(" ************ Get Personal Info  ***********  ");
			System.out.println("");

			// Getting Fisrt Name of an Employee
			JsonPath fisrtName = cf.jsonCompile("$..['emp Ids info'].[0].['Personal Info'].['First Name']");
			JSONArray fN = docCtx.read(fisrtName);
			System.out.println("First Name  --> " + fN);

			// Getting Last Name of an Employee
			JsonPath lastName = cf.jsonCompile("$..['emp Ids info'].[0].['Personal Info'].['Last Name']");
			JSONArray lN = docCtx.read(lastName);
			System.out.println("Last Name --> " + lN);

		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get Home Contact No of an particular employee.
	 * 
	 * @throws PathNotFoundException
	 * @throws FileNotFoundException
	 * @throws Exception
	 *
	 */
	public void getHomeContactNoOfParticularEmployee() throws PathNotFoundException, FileNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get Home Contact No.

			JsonPath contactInfo = cf.jsonCompile(
					"$..['emp Ids info'][0]['Personal Info'].['Contact Nos'].[?(@.type==\"home\")].number");
			JSONArray conInf = docCtx.read(contactInfo);
			System.out.println("");
			System.out.println(" Home Contact No  --> " + conInf);
			System.out.println("");
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get Professional Info of an Employee
	 * 
	 * @throws PathNotFoundException
	 * @throws FileNotFoundException
	 * @throws Exception
	 * 
	 */

	public void getProfessionalInfo() throws PathNotFoundException, FileNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get only Particular Employee Professional Info.

			JsonPath getProfessionalInfo = cf.jsonCompile("$..['emp Ids info'].[0].['Professional Info']");
			JSONArray profInfo = docCtx.read(getProfessionalInfo);
			System.out.println("");
			System.out.println(" Professional Info  --> " + profInfo);
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get Programming Skills of an Employee.
	 * 
	 * @throws PathNotFoundException
	 * @throws FileNotFoundException
	 * @throws Exception
	 * 
	 */
	public void getProgrammingSkills() throws PathNotFoundException, FileNotFoundException, Exception {
		try {
			String companyJsonString = this.loadConfigAndReadJson("employee_details_json");
			DocumentContext docCtx = JsonPath.parse(companyJsonString);
			// Get Programming Skills of an Employee

			JsonPath programmingSkills = cf.jsonCompile(
					"$..['emp Ids info'].[0].['Professional Info'].['Technical Skill set'][0].['Programming Languages']");
			JSONArray progSkills = docCtx.read(programmingSkills);
			System.out.println("");
			System.out.println(" Programming Skills --> " + progSkills);
		} catch (FileNotFoundException fe) {
			Reporter.log("File is not found in specified path: " + fe.getMessage());
			fe.printStackTrace();
		} catch (PathNotFoundException pe) {
			Reporter.log("No results for path");
			pe.printStackTrace();

		} catch (Exception e) {
			// error
			Reporter.log("Unexpected exception occur: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
