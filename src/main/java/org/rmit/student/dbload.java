package org.rmit.student;

import java.io.BufferedReader;
import java.io.File;

public class dbload {

    private static int pageSize;
    private static File file;

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


        // Assign a number of bytes to each field in the csv, combine all these to get the fixed record sizes

        // copy the byte arrays of all the fields into one byte array for the full record


//        int numOfBytes = 20;
//        String name;
//        byte[] bName = Arrays.copyOf(name.getBytes(), numOfBytes);
//        for (byte b: bName) {
//            System.out.println(b);
//        }
//        System.out.println();
//        System.out.println(bName.length);
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
