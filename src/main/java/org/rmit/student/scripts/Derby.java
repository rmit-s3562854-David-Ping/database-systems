package org.rmit.student.scripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Derby {

    private static final String AREA_TABLE = "AREA";
    private static final String STREET_TABLE = "STREET";
    private static final String SEGMENT_TABLE = "SEGMENT";
    private static final String STREET_SIDE_SECTION_TABLE = "STREET_SIDE_SECTION";
    private static final String SIGN_TABLE = "SIGN";
    private static final String PARKING_EVENT_TABLE = "PARKING_EVENT";
    private static final String PARKING_BAY_TABLE = "PARKING_BAY";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DATABASE_NAME = "jdbc:derby:ParkingRecords;create=true;";
    private static String SQL_TABLE_FILE = "tables.sql";

    private static Connection connection = null;
    private static Statement statement = null;

    public static void main(String[] args) {
        connect();
        cleanup();
        Derby.bulkLoad();
    }

    public static void bulkLoad() {

        // create the tables
        insertData(SQL_TABLE_FILE);
        // run the bulk load statement
        long startTime = System.currentTimeMillis();
        try {
            statement = connection.createStatement();
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'AREA', 'tables/area.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'STREET', 'tables/street.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'SEGMENT', 'tables/segment.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'STREET_SIDE_SECTION', 'tables/street-side-section.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'SIGN', 'tables/parking-sign.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'PARKING_BAY', 'tables/parking-bay.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'PARKING_EVENT', 'tables/parking-event.csv', null, null, null, 0)");
            statement.close();
        } catch (Exception e) {
            System.err.println("Error occurred when importing csv files into the tables, please ensure you have generated these files using the script: ./generate-table-data.rb sample.csv");
            System.err.println("If the files already exist please check the tables.sql file exists and ensure the schema matches the columns in the generated files");
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        disconnect();

        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to load data (milliseconds): " + timeInMilliseconds);
        System.out.println("Time taken to load data (seconds): " + (timeInMilliseconds / 1000));
    }

    private static void connect() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        try {
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cleanup() {
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE " + AREA_TABLE);
            statement.execute("DROP TABLE " + STREET_TABLE);
            statement.execute("DROP TABLE " + SEGMENT_TABLE);
            statement.execute("DROP TABLE " + STREET_SIDE_SECTION_TABLE);
            statement.execute("DROP TABLE " + SIGN_TABLE);
            statement.execute("DROP TABLE " + PARKING_BAY_TABLE);
            statement.execute("DROP TABLE " + PARKING_EVENT_TABLE);
            statement.close();
        } catch (Exception e) {
            System.out.println("Tables have not been created yet, Cleaning skipped");
        }
    }

    private static void insertData(String filePath) {
        String strCurrentLine;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            statement = connection.createStatement();
            while ((strCurrentLine = bufferedReader.readLine()) != null) {
                try {
                    statement.execute(strCurrentLine);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}