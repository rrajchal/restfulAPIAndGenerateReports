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

	private boolean allInProduction(List<EnergyRequest> list) {
		for (EnergyRequest req: list) {
			if (req.getStatus() == false) {
				return false;
			}
		}
		return true;
	}


	// This function needs to verify. Not clear yet: Array with highest number(multiple highest)??
	public int[] getActiveMonths(List<EnergyRequest> list) {
		if (list == null || list.size() == 0)
			return new int[0];
		List <Integer> activeMonths = list.stream().map(EnergyRequest::getActiveMonth).collect(Collectors.toList());
		List<Integer> modes = getModes(activeMonths);
		return modes.stream().mapToInt(x -> x).toArray();
	}
	
	// https://stackoverflow.com/questions/4191687/how-to-calculate-mean-median-mode-and-range-from-a-set-of-numbers
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

	private String getLicenses(List<EnergyRequest> list) {
		if (list == null || list.size() == 0)
			return "";
		List <String> licenses = list.stream().map(EnergyRequest::getLicense).distinct().collect(Collectors.toList());
		return String.join(", ", licenses);
	}


	private Double getTotalHours(List<EnergyRequest> list) {
		if (list == null || list.size() == 0)
			return 0.0;
		List <Double> hours = list.stream().map(EnergyRequest::getLaborHours).collect(Collectors.toList());
		return hours.stream().collect(Collectors.summingDouble(Double::doubleValue));
	}

	private String getLicense(String str) {
		int begin = str.indexOf("name:");
		int end = str.indexOf(",");
		if (begin == 0 || end == 0 || begin == end)
			return "";
		String license = str.substring(begin+5, end);
		//System.out.println(license);
		return license;
	}

	private boolean getStatus(String str) {
		return str.equals("Production") ? true : false;
	}

	private Double getHours(String str) {
		String regex = "\\d+.\\d+";
		//System.out.println(str.matches(regex) ? Double.valueOf(str) : 0);
		return str.matches(regex) ? Double.valueOf(str) : 0;
		
	}

	private int getActiveMonth(String str) {
		int monthIndex = str.indexOf("-");
		String monthStr = str.substring(monthIndex+1, monthIndex+3);
		String regex = "\\d+";
		//System.out.println(monthStr.matches(regex) ? Integer.valueOf(monthStr) : 0);
		return monthStr.matches(regex) ? Integer.valueOf(monthStr) : 0;
	}
	
	//https://www.baeldung.com/java-csv
	private void writeToCsv(List<EnergyRequest> specificRecordsSorted) throws IOException {
		String fileName = getFileName(sortBy);
		File file = new File(fileName);
		List <String[]> dataLines = lines(specificRecordsSorted);
		
		try (PrintWriter pw = new PrintWriter(file)) {
	        dataLines.stream().map(this::convertToCSV).forEach(pw::println);
	    }
	}
	
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

	public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}
	
	public String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	
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
	
	@SuppressWarnings("unused")
	private void displayData(List<EnergyRequest> records) {
		for (EnergyRequest r : records) {
			System.out.println(r.getActiveMonth() + " " + r.getOrganization() + " " + r.getLaborHours() + " " + r.getStatus() + " " + r.getLicense());
		}
		
	}
	
	@SuppressWarnings("unused")
	private void displayRefinedData(List<EnergyRequest> specificRecords) {
		for (EnergyRequest r : specificRecords) {
			System.out.println(r.getOrganization() + " " + r.getReleaseCount() + " " + r.getTotalLaborHours() + " " + r.getStatus() + " " + r.getLicense()
			+ " " + Arrays.toString(r.getMostActiveMonths()));
		}
	}

}
