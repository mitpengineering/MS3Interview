import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
//import classes from IO to read and write csv files
import java.util.Arrays;
//import arrays classes from util to read arrays
import java.sql.*;
//import sql classes to work with sqllite

public class Main {
    private static Connection conn = null;//Conn and stmt were made global so multiple functions can access them
    private static Statement stmt = null;
    //The following function connects to a databases and creates one if one does not exist
    //The function also drops table inorder to avoid multiple identical queries in databases if the application is ran more than once
    public static void createDatabaseAndConnection(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:DB/ms3interview.db");
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS csv;");
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    } 
    //The following function creates a table csv if it does not already exists in the databases
    //The function also creates the header from the csv file provided
    public static void createNewTable() { 
        String sql = "CREATE TABLE IF NOT EXISTS csv("  
            + " A text,"  
            + " B text,"  
            + " C text,"
            + " D text," 
            + " E text," 
            + " F text," 
            + " G text," 
            + " H text," 
            + " I text," 
            + " J text" 
            + ");";  

        try{
            stmt.execute(sql);  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }
    //The following function pushes all the valid queries from csv file into the database
    public static void insert(String[] client) {  
        String sql = "INSERT INTO csv (A,B,C,D,E,F,G,H,I,J) VALUES (?,?,?,?,?,?,?,?,?,?)";  

        try{    
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            pstmt.setString(1, client[0]);  
            pstmt.setString(2, client[1]);
            pstmt.setString(3, client[2]);
            pstmt.setString(4, client[3]);
            pstmt.setString(5, client[4]);
            pstmt.setString(6, client[5]);
            pstmt.setString(7, client[6]);
            pstmt.setString(8, client[7]);
            pstmt.setString(9, client[8]);
            pstmt.setString(10,client[9]);
            pstmt.executeUpdate();
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }  

    public static void main(String[] args) {
        createDatabaseAndConnection();
        createNewTable();
        String readFile = "ms3interview.csv"; //reads csv file from location provided and stores in string element csvFile
        String writeFile = "ms3interview-bad.csv"; //invalid queries will be written to this file
        String writeLog = "Log/ms3interview.log";//All the statistics will be written to this file
        BufferedReader br = null;
        BufferedWriter bw = null;
        BufferedWriter lw = null;
        String line = "";
        String cvsSplitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";//regex that will seperate string using commas unless they are in double quotes
        int recordsReceived = 0;
        int recordsSuccessful = 0;
        int recordsFailed = 0;
        try {

            br = new BufferedReader(new FileReader(readFile));//reads the csv file ms3interview
            FileWriter fw = new FileWriter(writeFile);
            bw = new BufferedWriter(fw);
            bw.write("A,B,C,D,E,F,G,H,I,J" + "\n");//writes the header of csv file ms3interview into ms3interview-bad.csv
            FileWriter wl = new FileWriter(writeLog);
            lw = new BufferedWriter(wl);
            while ((line = br.readLine()) != null) {
                // use regex as separator
                String[] client = line.split(cvsSplitBy);
                line = line.trim();
                if(!line.isEmpty()) { //Filters out the empty lines from the csv file ms3interview
                    recordsReceived++;
                    if(line.indexOf(",,") != -1){ //Filters out the Failed queries and writes them to ms3interview-bad.csv
                        bw.write(line + "\n");
                        recordsFailed++;
                    }else{
                        insert(client); //Inserts the valid queries into the database
                        recordsSuccessful++;
                    }
                }
            }
            //The following instruction writes all the statistics into the Log file
            lw.write("Records Received : " + recordsReceived + "\n" + "Records Successful : " + recordsSuccessful + "\n" + "Records Failed : " + recordsFailed);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    bw.close();
                    lw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

