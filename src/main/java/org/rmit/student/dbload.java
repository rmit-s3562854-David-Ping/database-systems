package org.rmit.student;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class dbload {

    private static int pageSize;
    private static File file;
    private static final String DELIMITER = ",";

    private static final int BOOLEAN_BYTES = 1;
    private static final int INTEGER_BYTES = 4;
    private static final int LONG_BYTES = 8;
    private static final int SHORT_STRING_BYTES = 10;
    private static final int MEDIUM_STRING_BYTES = 30;
    private static final int LONG_STRING_BYTES = 50;

    public static void main(String[] args) {
        // Declare variables for each field in the csv
        String DA_NAME;
        String deviceId;
        String arrivalTime;
        String departureTime;
        String durationSeconds;
        String streetMarker;
        String parkingSign;
        String area;
        String streetId;
        String streetName;
        String betweenStreet1;
        String betweenStreet2;
        String sideOfStreet;
        String inViolation;

        validateInputs(args);

        long startTime = System.currentTimeMillis();
        int totalRecords = 0;
        int totalPages = 0;

        ByteArrayOutputStream pageOutputStream = new ByteArrayOutputStream();
        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            fileOutputStream = new FileOutputStream("heap." + pageSize);
            String row;
            bufferedReader.readLine();
            while ((row = bufferedReader.readLine()) != null) {
                String[] columns = row.split(DELIMITER);
                if (columns[0] != null && columns[1] != null) {
                    DA_NAME = columns[0] + columns[1];
                    deviceId = columns[0];
                    arrivalTime = columns[1];
                    departureTime = columns[2];
                    durationSeconds = columns[3];
                    streetMarker = columns[4];
                    parkingSign = columns[5];
                    area = columns[6];
                    streetId = columns[7];
                    streetName = columns[8];
                    betweenStreet1 = columns[9];
                    betweenStreet2 = columns[10];
                    sideOfStreet = columns[11];
                    inViolation = columns[12];

                    byte[] bDA_NAME = Arrays.copyOf(DA_NAME.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bDeviceId = ByteBuffer.allocate(INTEGER_BYTES).putInt(Integer.parseInt(deviceId)).array();
                    byte[] bArrivalTime = Arrays.copyOf(arrivalTime.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bDepartureTime = Arrays.copyOf(departureTime.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bDurationSeconds = ByteBuffer.allocate(LONG_BYTES).putLong(Long.parseLong(durationSeconds)).array();
                    byte[] bStreetMarker = Arrays.copyOf(streetMarker.getBytes(), SHORT_STRING_BYTES);
                    byte[] bParkingSign = Arrays.copyOf(parkingSign.getBytes(), LONG_STRING_BYTES);
                    byte[] bArea = Arrays.copyOf(area.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bStreetId = ByteBuffer.allocate(INTEGER_BYTES).putInt(Integer.parseInt(streetId)).array();
                    byte[] bStreetName = Arrays.copyOf(streetName.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bBetweenStreet1 = Arrays.copyOf(betweenStreet1.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bBetweenStreet2 = Arrays.copyOf(betweenStreet2.getBytes(), MEDIUM_STRING_BYTES);
                    byte[] bSideOfStreet = ByteBuffer.allocate(INTEGER_BYTES).putInt(Integer.parseInt(sideOfStreet)).array();
                    byte[] bInViolation = ByteBuffer.allocate(BOOLEAN_BYTES).put((byte) (Boolean.parseBoolean(inViolation) ? 1 : 0)).array();

                    ByteArrayOutputStream recordOutputStream = new ByteArrayOutputStream();

                    recordOutputStream.write(bDA_NAME);
                    recordOutputStream.write(bDeviceId);
                    recordOutputStream.write(bArrivalTime);
                    recordOutputStream.write(bDepartureTime);
                    recordOutputStream.write(bDurationSeconds);
                    recordOutputStream.write(bStreetMarker);
                    recordOutputStream.write(bParkingSign);
                    recordOutputStream.write(bArea);
                    recordOutputStream.write(bStreetId);
                    recordOutputStream.write(bStreetName);
                    recordOutputStream.write(bBetweenStreet1);
                    recordOutputStream.write(bBetweenStreet2);
                    recordOutputStream.write(bSideOfStreet);
                    recordOutputStream.write(bInViolation);

                    byte[] record = recordOutputStream.toByteArray(); // record  length 291

                    // If the record byte array length and the page byte array length is greater than the pagesize,
                    // add the page byte array to the heap file and clear the page byte array.
                    if (record.length + pageOutputStream.size() > pageSize) {
                        byte[] page = Arrays.copyOf(pageOutputStream.toByteArray(), pageSize);
                        System.out.println(page.length);
                        fileOutputStream.write(page);
                        totalPages++;
                        pageOutputStream.reset();
                    }
                    // Place the record byte array into the page byte array
                    pageOutputStream.write(record);
                    totalRecords++;

                }
            }

            byte[] page = pageOutputStream.toByteArray();
            System.out.println(page.length);
            fileOutputStream.write(page);
            totalPages++;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e) {
            }
        }

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to load data (seconds): " + (timeInMilliseconds / 1000));
        System.out.println("Records added: " + totalRecords);
        System.out.println("Pages added: " + totalPages);


//        int numOfBytes = 20;
//        String name;
//        byte[] bName = Arrays.copyOf(name.getBytes(), numOfBytes);
//        for (byte b: bName) {
//            System.out.println(b);
//        }
//        System.out.println();
//        System.out.println(bName.length);
//        byte[] bytes = ByteBuffer.allocate(4).putInt(1695609641).array();
//
//        for (byte b : bytes) {
//            System.out.format("0x%x ", b);
//        }
    }


    // Validate input 3 arguments with -p pagesize and datafile
    public static void validateInputs(String[] args) {
        String pSize = null;
        String filePath = null;
        if (args.length != 3) {
            System.exit(1);
        }

        if (args[0].equals("-p")) {
            pSize = args[1];
            filePath = args[2];
        } else if (args[1].equals("-p")) {
            pSize = args[2];
            filePath = args[0];
        } else {
            System.err.println("Invalid arguments");
            displayUsageMessage();
        }

        try {
            pageSize = Integer.parseInt(pSize);
        } catch (NumberFormatException e) {
            System.err.println("Enter numeric value for the page size");
            displayUsageMessage();
        }
        file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("File path entered does not exist or is not a file");
            displayUsageMessage();
        }
    }

    public static void displayUsageMessage() {
        System.err.println("usage: dbload [-p <page_size>] [<data_file.csv>]");
        System.exit(1);
    }
}
