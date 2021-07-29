About the program
=================
This is the Maven project and all codes are written in Java to obtain data from Restful API and convert it to CSV and JSON files.

How to run the program.
===================
===================
Running in Eclipse IDE (preferred)
==================================
1. Unzip the App.zip folder
2. Import the project to the Maven project into eclipse (Other IDEs may require other steps)
3. Run the App.java program (Ctrl + F11 in Eclipse).
4. Enter your option to sort by: 1, 2, or 3.
       1: sort by Organization name - csvFileSortByOrgName.csv
	   2: sort by release count - csvFileSortByReleaseCount.csv
	   3: sort by total labor hours - csvFileSortByTotalLaborHours.csv
	   else: sort by Organization name. - csvFileSortByOrgName.csv
   By default, it will create three files: jsonFile.json, csvFile.csv, and the chosen sort by csv file
5. Optionally, you can run the test cases from AppTest.java. To do this, right-click on AppTest.java and run as JUnit Test
