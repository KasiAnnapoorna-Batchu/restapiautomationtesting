/*package com.atmecs.api.testscripts.multipartrequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

*//**
 * This program demonstrates a usage of the MultipartUtility class.
 * @author www.codejava.net
 *
 *//*
public class MultipartFileUploader {

	public static void main(String[] args) {
		String charset = "UTF-8";
		File uploadFile1 = new File("C:/Annapoorna/7767.jpg");
	//	File uploadFile2 = new File("e:/Test/PIC2.JPG");
		String requestURL = "https://www.googleapis.com/upload/drive/v3/files";

		try {
			System.out.println("lolooooooooooooo");
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);
			
			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");
			System.out.println("111111");
			multipart.addFormField("description", "Cool Pictures");
			multipart.addFormField("keywords", "Java,upload,Spring");
			System.out.println("00000000000000000");
			multipart.addFilePart("fileUpload", uploadFile1);
		//	multipart.addFilePart("fileUpload", uploadFile2);
			System.out.println("33333333333333333");
			List<String> response = multipart.finish();
			System.out.println("444444444444");
			System.out.println("SERVER REPLIED:");
			
			for (String line : response) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			System.err.println(ex);
			ex.printStackTrace();
		}
	}
}*/