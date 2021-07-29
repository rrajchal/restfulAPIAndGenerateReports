package com.energy.restfulExample;

import java.io.IOException;

import org.json.simple.parser.ParseException;

public class App {
    public static void main( String[] args ) throws ParseException {
        
        
        RestfulData data = new RestfulData("https://www.energy.gov/sites/prod/files/2020/12/f81/code-12-15-2020.json");
		data.accessData(); // this will save two files (json and csv)
		
		String sortBy = args.length == 1 && validEntry(args[0]) ? args[0] : "O";

		CustomeCsvGenerator geneateCustomeCsvFile = new CustomeCsvGenerator(sortBy);

		try {
			geneateCustomeCsvFile.generateCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Three files created.");
    }

	private static boolean validEntry(String str) {
		if (!str.equals("O") || !str.equals("R") || !str.equals("T")) {
			System.out.println("The sortBy parameter is not valid. Sorting by Organization name.");
			return false;
		} else {
			return true;
		}
	}
}
