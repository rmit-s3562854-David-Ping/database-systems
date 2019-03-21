package org.rmit.student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Derby {

    private static final String PARKING_BAY_TABLE = "parking_bay";
    private static final String PARKING_EVENT_TABLE = "parking_event";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DATABASE_NAME = "jdbc:derby:ParkingRecords;create=true;";
    private static String SQL_TABLE_FILE = "tables.sql";

    private static Connection connection = null;
    private static Statement statement = null;

    public static void bulkLoad() {
        connect();
        cleanup();

        long startTime = System.currentTimeMillis();

        // create the tables
        insertData(SQL_TABLE_FILE);
        // run the bulk load statement
        try {
            statement = connection.createStatement();
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'PARKING_BAY', 'parking-bay.csv', null, null, null, 0)");
            statement.execute("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, 'PARKING_EVENT', 'parking-event.csv', null, null, null, 0)");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time (seconds): " + (timeInMilliseconds / 1000));
    }

    private static void connect() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cleanup() {
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE " + PARKING_EVENT_TABLE);
            statement.execute("DROP TABLE " + PARKING_BAY_TABLE);
            statement.close();
        } catch (Exception e) {
//            e.printStackTrace();
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
            System.exit(1);
        }
    }
}