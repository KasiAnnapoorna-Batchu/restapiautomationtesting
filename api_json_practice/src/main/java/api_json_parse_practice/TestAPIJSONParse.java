package api_json_parse_practice;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

public class TestAPIJSONParse {
	@Test(enabled=true)
	public void getJsonKeysAndValues() throws FileNotFoundException, IOException, ParseException {

		try {
			// Read config from user directory.
			FileReader reader = new FileReader(
					(System.getProperty("user.dir") + "//src//main//java//config.properties"));
			Properties p = new Properties();
			p.load(reader);

			String jsonFile = p.getProperty("json_sample_path");
			// Read json File from the path specified.
			Object obj = new JSONParser().parse(new FileReader(jsonFile));

			// typecasting obj to JSONObject
			JSONObject jo = (JSONObject) obj;

			// Get company name Details.
			System.out.println("");
			System.out.println("========= **** Company Details **** ==========");
			String companyName = (String) jo.get("Company Name");
			System.out.println("Company --> " + companyName);
			String companyLocation = (String) jo.get("Company Location");
			System.out.println("Company Location --> " + companyLocation);
			String address = (String) jo.get("Address");
			System.out.println("Address --> " + address);
			String state = (String) jo.get("State");
			System.out.println("State --> " + state);
			String pincode = (String) jo.get("Pincode");
			System.out.println("Pincode --> " + pincode);
			String contactNo = (String) jo.get("Contact no");
			System.out.println("Contact No --> " + contactNo);
			System.out.println("");
			
			// Get Branches info
			System.out.println("****** Branches info *****");
			Map branches = ((Map) jo.get("Branches"));
			
			// iterating branches Map
			Iterator<Map.Entry> itr1 = branches.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pair1 = itr1.next();
				System.out.println(pair1.getKey() + " --> " + pair1.getValue());
			}

			// Get employee Details
			Map employeeDetails = ((Map) jo.get("ATMECS Employee Details"));
			JSONArray emp = (JSONArray) employeeDetails.get("emp Ids info");

			for (int i = 0; i < emp.size(); i++) {
				// getting personal info of an employee 1.
				System.out.println("");

				JSONObject personalInfo1 = (JSONObject) emp.get(i);
				Map perInfo = ((Map) personalInfo1.get("Personal Info"));
				String fN = ((String) perInfo.get("First Name"));
				System.out.println("****** Personal info of an employee - " + fN + "*****");
				Long id = (Long) personalInfo1.get("ID");
				System.out.println("ID --> " + id);
				System.out.println("First Name --> " + fN);
				String lN = ((String) perInfo.get("Last Name"));
				System.out.println("First Name --> " + lN);
				String dB = ((String) perInfo.get("Date-of-Birth"));
				System.out.println("Date of Bitrh --> " + dB);
				String gender = ((String) perInfo.get("Gender"));
				System.out.println("Gender --> " + gender);
				String fatherName = ((String) perInfo.get("Father's Name"));
				System.out.println("Father's Name --> " + fatherName);
				System.out.println("");

				// Get address info out from the personal info for individual employee.
				// getting Address info
				System.out.println("****** Address info *****");
				Map add = ((Map) perInfo.get("Address"));

				// iterating address Map
				Iterator<Map.Entry> addr1 = add.entrySet().iterator();
				while (addr1.hasNext()) {
					Map.Entry pair1 = addr1.next();
					System.out.println(pair1.getKey() + " --> " + pair1.getValue());
				}
				System.out.println("");

				// getting Contact info from personal info.
				JSONArray contactInfo = (JSONArray) perInfo.get("Contact Nos");
				System.out.println(" ****** Overall Contact Info ******");
				// System.out.println("");
				System.out.println("Looping over contact Info to get contact nos");

				Iterator itr5 = contactInfo.iterator();
				while (itr5.hasNext()) {
					Iterator<Map.Entry> itrfive = ((Map) itr5.next()).entrySet().iterator();
					while (itrfive.hasNext()) {
						Map.Entry pair5 = itrfive.next();
						System.out.println(pair5.getKey() + " --> " + pair5.getValue());
					}
				}

				// Get Professional info
				JSONObject professionalInfo = (JSONObject) emp.get(i);
				Map profInfo = ((Map) professionalInfo.get("Professional Info"));
				System.out.println("");
				System.out.println("****** Professional info of an employee - " + fN + " *****");
				String companyLoc = ((String) profInfo.get("Company Location"));
				System.out.println("Company Location --> " + companyLocation);
				String dj = ((String) profInfo.get("Date of Joining"));
				System.out.println("Date of Joining --> " + dj);
				Long tpf = (Long) personalInfo1.get("ID");
				System.out.println("Total Professional Experience --> " + tpf);
				String rm = ((String) profInfo.get("Reporting Manager"));
				System.out.println("Reporting Manager --> " + rm);
				String desg = ((String) profInfo.get("Designation"));
				System.out.println("Designation --> " + desg);
				String role = ((String) profInfo.get("Role"));
				System.out.println("Designation --> " + role);
				System.out.println();
				
				System.out.println("**** Techinical skills info **** ");
				JSONArray technicalskill = (JSONArray) profInfo.get("Technical Skill set");
				Iterator skills = technicalskill.iterator();
				while (skills.hasNext()) {
					Iterator<Map.Entry> technicalSkillset = ((Map) skills.next()).entrySet().iterator();
					while (technicalSkillset.hasNext()) {
						Map.Entry skillpair = technicalSkillset.next();
						System.out.println(skillpair.getKey() + " --> " + skillpair.getValue());

					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
