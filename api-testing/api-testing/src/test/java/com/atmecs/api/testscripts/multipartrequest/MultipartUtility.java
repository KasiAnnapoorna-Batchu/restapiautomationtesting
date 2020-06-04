/*package com.atmecs.api.testscripts.multipartrequest;





import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

*//**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server. 
 * @author www.codejava.net
 *
 *//*
public class MultipartUtility {
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;

	*//**
	 * This constructor initializes a new HTTP POST request with content type
	 * is set to multipart/form-data
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 *//*
	public MultipartUtility(String requestURL, String charset)
			throws IOException {
		this.charset = charset;
		
		// creates a unique boundary based on time stamp
		boundary = "===" + System.currentTimeMillis() + "===";
		String accessToken = "ya29.a0AfH6SMAoT6fQmUcgRM1MuzUbhDCp543G5DF5fM1T3K0O9xEIkw4qGQ3t74q8BweNdVxiAqGixkkffJa6kWV7tc9kTDBTt78If0lQh7tkkNJLMZVdshLlMIJCwwfidKD_8Sjf9ZOQC9VbIHrhTryZSs86MIe2jikcEXeQ";
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);
		httpConn.setRequestProperty("Authorization", "Bearer " + accessToken);
		httpConn.setRequestMethod("POST");
		// indicates POST method
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
		httpConn.setRequestProperty("Test", "Bonjour");
		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
				true);
	}

	*//**
	 * Adds a form field to the request
	 * @param name field name
	 * @param value field value
	 *//*
	public void addFormField(String name, String value) {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(
				LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
		writer.flush();
	}

	*//**
	 * Adds a upload file section to the request 
	 * @param fieldName name attribute in <input type="file" name="..." />
	 * @param uploadFile a File to be uploaded 
	 * @throws IOException
	 *//*
	  
	 // Include the section to describe the file
	 httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
	 httpRequestBodyWriter.write("Content-Disposition: form-data;"
	         + "name=\"myFile\";"
	         + "filename=\""+ logFileToUpload.getName() +"\""
	         + "\nContent-Type: text/plain\n\n");
	 httpRequestBodyWriter.flush();
	  
	 // Write the actual file contents
	 FileInputStream inputStreamToLogFile = new FileInputStream(logFileToUpload);
	  
	 int bytesRead;
	 byte[] dataBuffer = new byte[1024];
	 while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
	     outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
	 }
	  
	 outputStreamToRequestBody.flush();
	  
	 // Mark the end of the multipart http request
	 httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
	 httpRequestBodyWriter.flush();
	  
	 // Close the streams
	 outputStreamToRequestBody.close();
	 httpRequestBodyWriter.close();
	public void addFilePart(String fieldName, File uploadFile)
			throws IOException {
		String fileName = uploadFile.getName();
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append(
				"Content-Disposition: form-data; name=\"" + fieldName
						+ "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		writer.append(
				"Content-Type: "
						+ URLConnection.guessContentTypeFromName(fileName))
				.append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();

		FileInputStream inputStream = new FileInputStream(uploadFile);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		
		writer.append(LINE_FEED);
		writer.flush();		
	}

	*//**
	 * Adds a header field to the request.
	 * @param name - name of the header field
	 * @param value - value of the header field
	 *//*
	public void addHeaderField(String name, String value) {
		writer.append(name + ": " + value).append(LINE_FEED);
		writer.flush();
	}
	
	*//**
	 * Completes the request and receives response from the server.
	 * @return a list of Strings as response in case the server returned
	 * status OK, otherwise an exception is thrown.
	 * @throws IOException
	 *//*
	public List<String> finish() throws IOException {
		List<String> response = new ArrayList<String>();

		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.close();
		try {
		// checks server's status code first
		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.add(line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			throw new IOException("Server returned non-OK status: " + status);
		}
		
	} catch (IOException ex) {
		System.err.println(ex);
		ex.printStackTrace();
	}

		return response;
	}
}

public class MultiPartExample {
	
	
	 public static void main(String[] args) throws Exception {
		 PropertiesParsers prop = new PropertiesParsers();
		 
			Util util = new Util();
			
			String tdf = (FilePathConstants.TESTDATA_FILE_PATH);
			prop.loadProperty(tdf);
			// Load config
			prop.loadConfig();

			// Get key Values from properties file.
			String tasksListBaseURL = "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart";
			String accessToken = util.getTaskUrl();
			accessToken = "ya29.a0AfH6SMBrXVKBL1btfIxUWDl0i6T0tMk8_SkaSV_4uHxJjD9YSMrSUzdWLqXDwAwUChOmorWEFTeiYYv5F5R5PAv_U2h3U_e-vZ3pCkUUqWxmYAMxOKxpnNu377-lBgiXiZycuch2jVFiMDMI36H88zS8t_PMVJzrOf-j";
			System.out.println();
			String fileUrl = "C:\\Annapoorna\\7767.jpg";
			String boundaryString = "----SomeRandomText";
			File logFileToUpload = new File(fileUrl);
			try {
				if (accessToken != null && !accessToken.isEmpty()) {
					URL url = new URL("https://www.googleapis.com/upload/drive/v3/files");
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					//String auth = "Bearer " + accessToken;
				//	connection.setRequestProperty("Authorization", auth);

					String boundary = UUID.randomUUID().toString();
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Authorization", "Bearer " + accessToken);
					connection.setDoOutput(true);
					
					connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

					DataOutputStream request = new DataOutputStream(connection.getOutputStream());

					request.writeBytes("--" + boundary + "\r\n");
					request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
					request.writeBytes("fileDescription" + "\r\n");

					request.writeBytes("--" + boundary + "\r\n");
					request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + logFileToUpload.getName() + "\"\r\n\r\n");
					//request.write(FileUtils.readFileToByteArray(file));
					request.writeBytes("\r\n");

					request.writeBytes("--" + boundary + "--\r\n");
					request.flush();
					// Write the actual file contents
					FileInputStream inputStreamToLogFile = new FileInputStream(logFileToUpload);
					 
					int bytesRead;
					byte[] dataBuffer = new byte[1024];
					while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
						connection.getOutputStream().write(dataBuffer, 0, bytesRead);
					}
					int respCode = connection.getResponseCode();
					System.out.println(respCode);
					switch(respCode) {
					    case 200:
					        //all went ok - read response
					      //  ...
					        break;
					    case 301:
					    case 302:
					    case 307:
					        //handle redirect - for example, re-post to the new location
					   //     ...
					        break;
					 //   ...
					    default:
					        //do something sensible
					}
							}
			}catch (Exception e) {
				// error
				Reporter.log("Unexpected exception occur: " + e.getMessage());
				e.printStackTrace();
			}
	 
}
}
*/