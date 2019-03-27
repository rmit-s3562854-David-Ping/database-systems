package org.rmit.student;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class dbload {

    private int pageSize;
    private File file;
    private static final String DELIMITER = ",";

    private static final int BOOLEAN_BYTES = 1;
    private static final int INTEGER_BYTES = 4;
    private static final int LONG_BYTES = 8;
    private static final int SHORT_STRING_BYTES = 10;
    private static final int MEDIUM_STRING_BYTES = 30;
    private static final int LONG_STRING_BYTES = 50;

    public static void main(String[] args) {
        dbload load = new dbload();
        load.validateInputs(args);
        load.loadData();
    }

    /**
     * Validates the command line arguments with -p pagesize and datafile
     *
     * @param args The command line arguments
     */
    private void validateInputs(String[] args) {
        String pSize = null;
        String filePath = null;
        if (args.length != 3) {
            displayUsageMessage();
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
            if (pSize != null) {
                pageSize = Integer.parseInt(pSize);
            }
        } catch (NumberFormatException e) {
            System.err.println("Enter numeric value for the page size");
            displayUsageMessage();
        }
        if (filePath != null) {
            file = new File(filePath);
        }
        if (!file.exists() || !file.isFile()) {
            System.err.println("File path entered does not exist or is not a file");
            displayUsageMessage();
        }
    }

    private void displayUsageMessage() {
        System.err.println("usage: dbload [-p <page_size>] [<data_file.csv>]");
        System.exit(1);
    }

    /**
     * This method reads through each column in the csv file and converts the values into byte arrays which are then
     * written into a heap file with pages whose size are specified in the command line. The records being written into
     * each page will have a fixed length and will be appended to a byte array of size <pagesize>, this array represents
     * the page which after being filled (no more records can be added) will be padded and written to the heap file.
     */
    private void loadData() {
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

        long startTime = System.currentTimeMillis();
        int totalRecords = 0;
        int totalPages = 0;

        ByteArrayOutputStream pageOutputStream = new ByteArrayOutputStream();
        BufferedReader bufferedReader = null;
        DataOutputStream dataOutputStream = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            dataOutputStream = new DataOutputStream(new FileOutputStream("heap." + pageSize));
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
                    byte[][] byteArrays = {bDA_NAME, bDeviceId, bArrivalTime, bDepartureTime, bDurationSeconds, bStreetMarker, bParkingSign, bArea, bStreetId, bStreetName, bBetweenStreet1, bBetweenStreet2, bSideOfStreet, bInViolation};
                    for (byte[] byteArray : byteArrays) {
                        recordOutputStream.write(byteArray);
                    }
                    byte[] record = recordOutputStream.toByteArray(); // record  length 291
                    // If the record byte array length and the page byte array length is greater than the pagesize,
                    // add the page byte array to the heap file and clear the page byte array.
                    if (record.length + pageOutputStream.size() > pageSize) {
                        byte[] page = Arrays.copyOf(pageOutputStream.toByteArray(), pageSize);
                        dataOutputStream.write(page);
                        totalPages++;
                        pageOutputStream.reset();
                    }
                    // Place the record byte array into the page byte array
                    pageOutputStream.write(record);

                    totalRecords++;
                }
            }
            byte[] page = Arrays.copyOf(pageOutputStream.toByteArray(), pageSize);
            dataOutputStream.write(page);
            totalPages++;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to load data (milliseconds): " + timeInMilliseconds);
        System.out.println("Time taken to load data (seconds): " + (timeInMilliseconds / 1000));
        System.out.println("Records added: " + totalRecords);
        System.out.println("Pages added: " + totalPages);
    }
}
