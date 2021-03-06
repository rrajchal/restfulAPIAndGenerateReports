package com.energy.restfulExample;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVReader;
/**
 * This creates a csv file with 5 fields: Organization name, Release count, Total labor hours, All in production, and Most active months
 * The data will be sorted based on user's selection or Organization name. 
 * @author Rajesh
 *
 */
public class CustomeCsvGenerator {
	
	private String sortBy;
	CustomeCsvGenerator(String sortBy) {
		this.sortBy = sortBy;
	}
	
	public void generateCsv() throws IOException {
		Logger log = Logger.getLogger(CustomeCsvGenerator.class.getName());
		log.info("Creating custome csv File.");
		List<EnergyRequest> records = new ArrayList<>();
		
		try (CSVReader csvReader = new CSVReader(new FileReader("csvFile.csv"));) {
		    String[] fields = null;
		    int line = 0;
		    while ((fields = csvReader.readNext()) != null) {
		    	if (line == 0) {
		    		line++;
		    	} else {
			    	EnergyRequest req = new EnergyRequest();
			    	req.setActiveMonth(getActiveMonth(fields[0]));
			    	req.setOrganization(fields[3]);
			    	req.setLaborHours(getHours(fields[7]));
			    	req.setStatus(getStatus(fields[0]));
			    	req.setLicense(getLicense(fields[1]));
			    	records.add(req);
			    	line++;
		    	}
		    	
		    }
		}
		//displayData(records);
		
		List<EnergyRequest> specificRecords = getRequiredData(records);
		
		//displayRefinedData(specificRecords);
		
		List<EnergyRequest> specificRecordsSorted = sortData(specificRecords, sortBy);
		writeToCsv(specificRecordsSorted);

	}

	/**
	 * This sorts the data
	 * @param records
	 * @param sortBy
	 * @return
	 */
	private List<EnergyRequest> sortData(List<EnergyRequest> records, String sortBy) {
		if (sortBy.equals("O")) {
			return records.stream().sorted(Comparator.comparing(EnergyRequest::getOrganization)).collect(Collectors.toList());
		} else if (sortBy.equals("R")) {
			return records.stream().sorted(Comparator.comparing(EnergyRequest::getReleaseCount)).collect(Collectors.toList());
		} else if (sortBy.equals("T")) {
			return records.stream().sorted(Comparator.comparing(EnergyRequest::getTotalLaborHours)).collect(Collectors.toList());
		}
		return records.stream().sorted(Comparator.comparing(EnergyRequest::getOrganization)).collect(Collectors.toList());
	}

	/**
	 * This calculates field values and return the List of EnergyRequest class which is ready to write to CSV
	 * @param records
	 * @return List<EnergyRequest> 
	 */
	private List<EnergyRequest> getRequiredData(List<EnergyRequest> records) {
		if (records == null || records.size() == 0)
			return null;
		List<EnergyRequest> energyRequestList = new ArrayList<>();
		EnergyRequest req = new EnergyRequest();
		Map <String, List<EnergyRequest>> groupByOrg = records.stream().collect(Collectors.groupingBy(EnergyRequest::getOrganization));
		
		for (Map.Entry<String, List<EnergyRequest>> value : groupByOrg.entrySet()) {
			req = new EnergyRequest();
			req.setOrganization(value.getKey());
			req.setReleaseCount(value.getValue().size());
			req.setStatus(allInProduction(value.getValue()));
			req.setTotalLaborHours(getTotalHours(value.getValue()));
			req.setLicense(getLicenses(value.getValue()));
			req.setMostActiveMonths(getActiveMonths(value.getValue()));
			energyRequestList.add(req);
		}
		return energyRequestList;
	}

	/**
	 * returns a value for the All_In_Production field
	 * @param list
	 * @return
	 */
	private boolean allInProduction(List<EnergyRequest> list) {
		for (EnergyRequest req: list) {
			if (req.getStatus() == false) {
				return false;
			}
		}
		return true;
	}


	/**
	 * This finds the mode values for the month of date created.
	 * This function needs to verify. Not clear yet: Array with highest number(multiple highest)??
	 */
	public int[] getActiveMonths(List<EnergyRequest> list) {
		if (list == null || list.size() == 0)
			return new int[0];
		List <Integer> activeMonths = list.stream().map(EnergyRequest::getActiveMonth).collect(Collectors.toList());
		List<Integer> modes = getModes(activeMonths);
		return modes.stream().mapToInt(x -> x).toArray();
	}
	
	/**
	 * Calculates mode from List of Integers
	 * This function is modified from https://stackoverflow.com/questions/4191687/how-to-calculate-mean-median-mode-and-range-from-a-set-of-numbers
	 * @param numbers
	 * @return
	 */
	public List<Integer> getModes(List<Integer> numbers) {
	    final Map<Integer, Long> countFrequencies = numbers.stream()
	            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

	    final long maxFrequency = countFrequencies.values().stream()
	            .mapToLong(count -> count)
	            .max().orElse(-1);

	    return countFrequencies.entrySet().stream()
	            .filter(tuple -> tuple.getValue() == maxFrequency)
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
	}

	/**
	 * This collect list of permission licenses and return a string for the list
	 * @param list
	 * @return
	 */
	private String getLicenses(List<EnergyRequest> list) {
		if (list == null || list.size() == 0)
			return "";
		List <String> licenses = list.stream().map(EnergyRequest::getLicense).distinct().collect(Collectors.toList());
		return String.join(", ", licenses);
	}

	/**
	 * This is a field for the csv report which is the added labor hours
	 * @param list
	 * @return
	 */
	private Double getTotalHours(List<EnergyRequest> list) {
		if (list == null || list.size() == 0)
			return 0.0;
		List <Double> hours = list.stream().map(EnergyRequest::getLaborHours).collect(Collectors.toList());
		return hours.stream().collect(Collectors.summingDouble(Double::doubleValue));
	}

	/**
	 * This function parse the permission field from the raw csv field and parse the License
	 * @param str
	 * @return
	 */
	private String getLicense(String str) {
		int begin = str.indexOf("name:");
		int end = str.indexOf(",");
		if (begin == 0 || end == 0 || begin == end)
			return "";
		String license = str.substring(begin+5, end);
		//System.out.println(license);
		return license;
	}

	/**
	 * If the release is the Production it returns true
	 * @param str
	 * @return
	 */
	private boolean getStatus(String str) {
		return str.equals("Production") ? true : false;
	}

	/**
	 * This make sure the hours is decimal value
	 * @param str
	 * @return
	 */
	private Double getHours(String str) {
		String regex = "\\d+.\\d+";
		//System.out.println(str.matches(regex) ? Double.valueOf(str) : 0);
		return str.matches(regex) ? Double.valueOf(str) : 0;
		
	}

	/**
	 * This extract month from date (created date)
	 * @param str
	 * @return
	 */
	private int getActiveMonth(String str) {
		int monthIndex = str.indexOf("-");
		String monthStr = str.substring(monthIndex+1, monthIndex+3);
		String regex = "\\d+";
		//System.out.println(monthStr.matches(regex) ? Integer.valueOf(monthStr) : 0);
		return monthStr.matches(regex) ? Integer.valueOf(monthStr) : 0;
	}
	
	/**
	 * Returns a file name based on user selection
	 * @param sortBy
	 * @return
	 */
	private String getFileName(String sortBy) {
		String fileName = "csvFile";
		if (sortBy.equals("O")) {
			return fileName + "SortByOrgName.csv";
		} else if (sortBy.equals("R")) {
			return fileName + "SortByReleaseCount.csv";
		} else if (sortBy.equals("T")) {
			return fileName + "SortByTotalLaborHours.csv";
		}
		return "csvFile.csv";
	}
	
	/**
	 * This function write the List of class to csv file. 
	 * This code is modified from https://www.baeldung.com/java-csv
	 * @param specificRecordsSorted
	 * @throws IOException
	 */
	private void writeToCsv(List<EnergyRequest> specificRecordsSorted) throws IOException {
		String fileName = getFileName(sortBy);
		File file = new File(fileName);
		List <String[]> dataLines = lines(specificRecordsSorted);
		
		try (PrintWriter pw = new PrintWriter(file)) {
	        dataLines.stream().map(this::convertToCSV).forEach(pw::println);
	    }
	}
	
	/**
	 * Required function to write to CSV
	 * @param data
	 * @return
	 */
	public String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	
	/**
	 * Required function to write to CSV
	 * @param data
	 * @return
	 */
	public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}
	
	/**
	 * Add lines with data to write to csv file
	 * @param records
	 * @return
	 */
	private List <String[]> lines(List<EnergyRequest> records) {
		List<String[]> lines = new ArrayList<>();
		
		lines.add(new String[] {"Organization", "Release_Count", "Total_Labor_Hours", "All_In_Production", "Licenses", "Most_Active_Months"});
			for (EnergyRequest r : records) {
				String[] dataLine = {r.getOrganization(), String.valueOf(r.getReleaseCount()),
				String.valueOf(r.getTotalLaborHours()), r.getStatus() ? "true" : "false", r.getLicense(), 
				Arrays.toString(r.getMostActiveMonths())};
				lines.add(dataLine);
			}
		return lines;
	}
	
	/**
	 * this is for testing data
	 * @param records
	 */
	@SuppressWarnings("unused")
	private void displayData(List<EnergyRequest> records) {
		for (EnergyRequest r : records) {
			System.out.println(r.getActiveMonth() + " " + r.getOrganization() + " " + r.getLaborHours() + " " + r.getStatus() + " " + r.getLicense());
		}
		
	}
	
	/**
	 * this is for testing data
	 * @param specificRecords
	 */
	@SuppressWarnings("unused")
	private void displayRefinedData(List<EnergyRequest> specificRecords) {
		for (EnergyRequest r : specificRecords) {
			System.out.println(r.getOrganization() + " " + r.getReleaseCount() + " " + r.getTotalLaborHours() + " " + r.getStatus() + " " + r.getLicense()
			+ " " + Arrays.toString(r.getMostActiveMonths()));
		}
	}

}
