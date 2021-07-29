package com.energy.restfulExample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class RestfulData {
	
	private String urlLink;
	
	RestfulData(String urlLink) {
		this.urlLink = urlLink;
	}
	
	public EnergyRequest accessData() throws ParseException {
		System.out.println("Accessing URL data for the JSON File...");
		String jsonFile = "jsonFile.json";
		File csvFile = new File("csvFile.csv");
		try {
			
			URL url = new URL(this.urlLink);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer content = new StringBuffer();
			String response;		
			File file = new File(jsonFile);
			FileWriter myWriter = new FileWriter(file);
			while ((response = in.readLine()) != null) {
				content.append(response);  // to write to csv file
				myWriter.write(response);  // write to json file
			}
			myWriter.close();
			in.close();
			connection.disconnect();
			
			System.out.println("Writing to the JSON File...");
			String jsonArrayString = content.toString(); 
			JSONObject output;

			output = new JSONObject(jsonArrayString);
			JSONArray docs = output.getJSONArray("releases");
			
			String csv = CDL.toString(docs);
			FileUtils.writeStringToFile(csvFile, csv, "ISO-8859-1");

			
		} catch (MalformedURLException e) {
			System.out.println("URL is invalid: " + this.urlLink);
		} catch (IOException e) {
			System.out.println("IOException exception");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Other exception");
			e.printStackTrace();
		}
		return null;
	}
}
