package com.atmecs.practice.automation.testscripts.jsonpath;

import org.testng.annotations.Test;


/**
 * 
 * @author Kasi.Batchu
 * Get Atemces Employee Details.
 */
public class GetAtmecsEmployeeDetails {
	
	FetchEmployeeDetailsUsingJsonPath fetchEmpJson = new FetchEmployeeDetailsUsingJsonPath();
	
	@Test(priority=1)
	public void companyMajorDetails () throws Exception {
		fetchEmpJson.getBasicCompanyDetails();
		
	}
	
	@Test(priority=2)
	public void branchesInfo () throws Exception {
		fetchEmpJson.getBranchesInfo();
		
	}
	
	@Test(priority=3)
	public void empInfo () throws Exception {
		fetchEmpJson.getOnlyParticularEmpDetails();
		
	}
	
	@Test(priority=4)
	public void empPersonalInfo () throws Exception {
		fetchEmpJson.getPersonalInfo();
		
	}
	
	@Test(priority=5)
	public void empProfessionalInfo () throws Exception {
		fetchEmpJson.getProfessionalInfo();
		
	}
	
	@Test(priority=6)
	public void empProgrammingSkills () throws Exception {
		fetchEmpJson.getProgrammingSkills();
		
	}
	
	@Test(priority=7)
	public void empHomeNo () throws Exception {
		fetchEmpJson.getHomeContactNoOfParticularEmployee();
		
	}
	
}
