package com.energy.restfulExample;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

/**
 * Main entry point
 * @author Rajesh
 *
 */
public class App {
    public static void main( String[] args ) throws ParseException {
        
    	// possibility to run by command line, otherwise, get user input for sortBy variable
    	String sortBy = args.length == 1 && validEntry(args[0]) ? args[0] : getUserEntry();
        
        RestfulData data = new RestfulData("https://www.energy.gov/sites/prod/files/2020/12/f81/code-12-15-2020.json");
		data.accessData(); // this will save two files (json and csv)

		CustomeCsvGenerator geneateCustomeCsvFile = new CustomeCsvGenerator(sortBy);

		try {
			geneateCustomeCsvFile.generateCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Three files created.");
    }

	private static String getUserEntry() {
		Scanner input = new Scanner(System.in);
		System.out.println("Please select a sort by option (1 is default for invalid entry)");
		System.out.println("1. Sort by Organization Name.");
		System.out.println("2. Sort by Release Count.");
		System.out.println("3. Sort by Total Labor Hours.");
		System.out.print("Select sort by option: ");
		String userInput = input.nextLine();
		input.close();
		if (userInput.equals("1"))
			return "O";
		else if (userInput.equals("2"))
			return "R";
		else if (userInput.equals("3"))
			return "T";
		return "O";
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
