# MS3Interview

#Summary
The purpose of this repository is to consume ms3interview.csv,parse the data and insert valid records into sqlite database.
Invalid records will be written to ms3interview-bad.csv

#Steps to run application
1. Download zip file
2. Extract zip file
3. Download sqlite-jdbc-3.30.1.jar
4. Add the jar file in your class path
5. Run main.java with a java compiler
6. Database will be stored in the DB folder
7. ms3interview-bad.csv will be generated in the same folder as main.java

#Design Approach
My first objective was to read ms3interview.csv, which I achieved using BufferedReader Class.
Next, I needed to parse the data. Initally, planned to parse using only commas, however, data in column E had commas imbedded into the data.
Fortunately, the data was surrounded in double quotes. I was able find a regex online that will parsed data by commas not surrounded in double quotes.

My second objective was to write invalid records to ms3interview-bad.csv, which I achieved using BufferedWriter Class.
First, I created the file ms3interview-bad.csv, next I looked if a line in ms3interview.csv contained ",," if it did it was considered a failed record.
Finally, I pushed all the failed records into ms3interview.csv

My third objective was to write valid records to a sqlite database. First, using the jdbc driver I was able to create a database and connect to it if was not alread created.
Next, I drop the table in that database if it already existed. I do this to prevent multiple entries of identical records if the application is ran multiple times.
Then, I create a table with the headers from ms3interview.csv. Finally, I push the valid records into the sqlite database.
Note: The program was crashing due to the ms3interview.csv having blank lines. In order to remedy this, I check to if each line was empty before push the records into the database.

My Final objective was to write statistics to a log file. Using BufferedWriter class once again, I was able to write the total, successful and failed records into the log file.

#Assumptions
1. I needed to create ms3interview-bad.csv file
2. I dropped the table in the databases to prevent multiple entries of identical records if the application is ran multiple times.
