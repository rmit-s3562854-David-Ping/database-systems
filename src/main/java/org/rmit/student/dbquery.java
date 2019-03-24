package org.rmit.student;

import java.io.*;
import java.nio.ByteBuffer;

public class dbquery {

    private static String text;
    private static int pageSize;
    private static File file;

    private static final int BOOLEAN_BYTES = 1;
    private static final int INTEGER_BYTES = 4;
    private static final int LONG_BYTES = 8;
    private static final int SHORT_STRING_BYTES = 10;
    private static final int MEDIUM_STRING_BYTES = 30;
    private static final int LONG_STRING_BYTES = 50;

    private static final int DA_NAME_SIZE = MEDIUM_STRING_BYTES;
    private static final int DEVICE_ID_SIZE = INTEGER_BYTES;
    private static final int ARRIVAL_TIME_SIZE = MEDIUM_STRING_BYTES;
    private static final int DEPARTURE_TIME_SIZE = MEDIUM_STRING_BYTES;
    private static final int DURATION_SECONDS_SIZE = LONG_BYTES;
    private static final int STREET_MARKER_SIZE = SHORT_STRING_BYTES;
    private static final int PARKING_SIGN_SIZE = LONG_STRING_BYTES;
    private static final int AREA_SIZE = MEDIUM_STRING_BYTES;
    private static final int STREET_ID_SIZE = INTEGER_BYTES;
    private static final int STREET_NAME_SIZE = MEDIUM_STRING_BYTES;
    private static final int BETWEEN_STREET_1_SIZE = MEDIUM_STRING_BYTES;
    private static final int BETWEEN_STREET_2_SIZE = MEDIUM_STRING_BYTES;
    private static final int SIDE_OF_STREET_SIZE = INTEGER_BYTES;
    private static final int IN_VIOLATION_SIZE = BOOLEAN_BYTES;

    public static void main(String[] args) {
        validateInputs(args);
        search();
    }

    // Validate input 2 arguments with text pagesize
    private static void validateInputs(String[] args) {
        if (args.length != 2) {
            displayUsageMessage();
        }

        String pSize = args[1];
        text = args[0];

        try {
            pageSize = Integer.parseInt(pSize);
        } catch (NumberFormatException e) {
            System.err.println("Enter numeric value for the page size");
            displayUsageMessage();
        }
        file = new File("heap." + pageSize);
        if (!file.exists() || !file.isFile()) {
            System.err.println("The file heap." + pageSize + " does not exist");
            displayUsageMessage();
        }
    }

    private static void displayUsageMessage() {
        System.err.println("usage: dbquery [<text>] [<page_size>]");
        System.exit(1);
    }

    private static void search() {
        long startTime = System.currentTimeMillis();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);

            int bytesRead = 0;
            while (bytesRead != -1) {
                byte[] page = new byte[pageSize];
                bytesRead = fileInputStream.read(page);
                if (bytesRead != -1) {
                    searchPage(page);
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
            }
        }

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to search (milliseconds): " + timeInMilliseconds);
        System.out.println("Time taken to search (seconds): " + (timeInMilliseconds / 1000));
    }

    private static void searchPage(byte[] page) {
        // Declare variables for each field in the csv
        byte[] DA_NAME = new byte[DA_NAME_SIZE];
        byte[] deviceId = new byte[DEVICE_ID_SIZE];
        byte[] arrivalTime = new byte[ARRIVAL_TIME_SIZE];
        byte[] departureTime = new byte[DEPARTURE_TIME_SIZE];
        byte[] durationSeconds = new byte[DURATION_SECONDS_SIZE];
        byte[] streetMarker = new byte[STREET_MARKER_SIZE];
        byte[] parkingSign = new byte[PARKING_SIGN_SIZE];
        byte[] area = new byte[AREA_SIZE];
        byte[] streetId = new byte[STREET_ID_SIZE];
        byte[] streetName = new byte[STREET_NAME_SIZE];
        byte[] betweenStreet1 = new byte[BETWEEN_STREET_1_SIZE];
        byte[] betweenStreet2 = new byte[BETWEEN_STREET_2_SIZE];
        byte[] sideOfStreet = new byte[SIDE_OF_STREET_SIZE];
        byte[] inViolation = new byte[IN_VIOLATION_SIZE];

        byte[][] byteArrays = {DA_NAME, deviceId, arrivalTime, departureTime, durationSeconds, streetMarker, parkingSign, area, streetId, streetName, betweenStreet1, betweenStreet2, sideOfStreet, inViolation};

        int recordSize = DA_NAME_SIZE + DEVICE_ID_SIZE + ARRIVAL_TIME_SIZE + DEPARTURE_TIME_SIZE + DURATION_SECONDS_SIZE +
                STREET_MARKER_SIZE + PARKING_SIGN_SIZE + AREA_SIZE + STREET_ID_SIZE + STREET_NAME_SIZE +
                BETWEEN_STREET_1_SIZE + BETWEEN_STREET_2_SIZE + SIDE_OF_STREET_SIZE + IN_VIOLATION_SIZE;

        ByteArrayInputStream pageInputStream = new ByteArrayInputStream(page);

        int bytesRead;
        while (true) {
            byte[] record = new byte[recordSize];
            bytesRead = 0;
            try {
                bytesRead = pageInputStream.read(record);
            } catch (Exception e) {
            }

            if (bytesRead != recordSize) {
                break;
            }
            ByteArrayInputStream recordInputStream = new ByteArrayInputStream(record);

            for (byte[] byteArray : byteArrays) {
                try {
                    recordInputStream.read(byteArray);
                } catch (Exception e) {
                }
            }

            String recordID = new String(DA_NAME);
            if (recordID.trim().equals(text)) {
                System.out.println("DeviceID: " + ByteBuffer.wrap(deviceId).getInt());
                System.out.println("Arrival time: " + new String(arrivalTime));
                System.out.println("Departure time: " + new String(departureTime));
                System.out.println("Duration (seconds): " + ByteBuffer.wrap(durationSeconds).getLong());
                System.out.println("Street marker: " + new String(streetMarker));
                System.out.println("Parking sign: " + new String(parkingSign));
                System.out.println("Area: " + new String(area));
                System.out.println("Street ID: " + ByteBuffer.wrap(streetId).getInt());
                System.out.println("Street Name: " + new String(streetName));
                System.out.println("Between street 1: " + new String(betweenStreet1));
                System.out.println("Between street 2: " + new String(betweenStreet2));
                System.out.println("Side of street: " + ByteBuffer.wrap(sideOfStreet).getInt());
                System.out.println("In violation: " + (Boolean) (ByteBuffer.wrap(inViolation).get() != 0) + "\n");
            }
        }
    }
}
