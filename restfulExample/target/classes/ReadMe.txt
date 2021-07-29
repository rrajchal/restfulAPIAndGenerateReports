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
3. Run the App.java program (Ctrl + F11)
   By default, it will create three files: jsonFile.json, csvFile.csv, and csvFileSortByOrgName.csv
   To sort by release count or total labor hours, assign sortBy value to R or T in App.java

Running in Command Line
=======================
1. Unzip the App.zip folder
2. Open command line.
3. Go to the folder containing App.java (make sure mvn and java are configured/installed properly with valid system PATH)
4. Write javac App.java to compile the java file
5. Write java App X to run the program, where X is an uppercase letter O, R, or T 
	O: sort by Organization name - csvFileSortByOrgName.csv
	R: sort by release count - csvFileSortByReleaseCount.csv
	T: sort by total labor hours - csvFileSortByTotalLaborHours.csv
	else: sort by Organization name. - csvFileSortByOrgName.csv
6. Optionally, you can run the App.java from the Maven project from eclipse IDE 