package org.rmit.student.scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DerbyQuery {

    private static final String QUERY_1 = "SELECT * FROM PARKING_EVENT WHERE duration > 80000";
    private static final String QUERY_2 = "SELECT * FROM PARKING_EVENT WHERE arrival_time > 1519063163";
    private static final String QUERY_3 = "SELECT * FROM PARKING_EVENT WHERE duration > 60000 AND in_violation = false";
    private static final String QUERY_4 = "SELECT * FROM PARKING_EVENT WHERE duration < 1000 AND in_violation = true";

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DATABASE_NAME = "jdbc:derby:ParkingRecords2;";

    private static Connection connection = null;
    private static Statement statement = null;

    public static void main(String[] args) {
        connect();
        DerbyQuery.runQuery(QUERY_1);
        DerbyQuery.runQuery(QUERY_1);
        DerbyQuery.runQuery(QUERY_2);
        DerbyQuery.runQuery(QUERY_3);
        DerbyQuery.runQuery(QUERY_4);
        disconnect();
    }

    public static void runQuery(String query) {
        long timeInMilliseconds;
        // run the bulk load statement
        try {
            long startTime = System.currentTimeMillis();
            statement = connection.createStatement();
            statement.execute(query);
            long endTime = System.currentTimeMillis();
            timeInMilliseconds = endTime - startTime;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Query Ran: " + query);
        System.out.println("Time taken to run query (milliseconds): " + timeInMilliseconds);
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
}