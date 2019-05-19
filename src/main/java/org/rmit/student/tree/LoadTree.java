package org.rmit.student.tree;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Scanner;

import static org.rmit.student.tree.Constants.*;

public class LoadTree {

    private String loadOption;
    private int pageSize;
    private int branchingFactor;
    private File file;

    public static void main(String[] args) {
        LoadTree loadTree = new LoadTree();
        loadTree.validateInputs(args);
        loadTree.load();
    }

    /**
     * Validates the command line arguments with branching factor and page size
     *
     * @param args The command line arguments
     */
    private void validateInputs(String[] args) {
        if (args.length != 3) {
            displayUsageMessage();
        }

        loadOption = args[0];
        String pSize = args[1];
        String mEntries = args[2];

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

        try {
            branchingFactor = Integer.parseInt(mEntries);
        } catch (NumberFormatException e) {
            System.err.println("Enter numeric value for the max entries per node, must be greater than 3");
            displayUsageMessage();
        }

        if (branchingFactor < 3) {
            displayUsageMessage();
        }
    }

    /**
     * The records in the heap file are entered one at a time from the
     * root/top of the tree.
     *
     * Pages are read from the file one at a time and read into the b+ tree.
     *
     * The time taken will be displayed once it is finished loading.
     */
    private void load() {
        BPlusTree<String, byte[]> tree = new BPlusTree(branchingFactor);

        long startTime = System.currentTimeMillis();
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            int bytesRead = 0;
            while (bytesRead != -1) {
                byte[] page = new byte[pageSize];
                bytesRead = fileInputStream.read(page);
                if (bytesRead != -1) {
                    readPageIntoTree(tree, page);
                }
            }
        } catch (Exception e) {
        }

        if (loadOption.equals("bulk-load")) {
            tree.resetRoot();
        }

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to load file into tree (milliseconds): " + timeInMilliseconds);
        System.out.println("Time taken to load file into tree (seconds): " + (timeInMilliseconds / 1000));

        searchAfterTreeLoaded(tree);
    }


    /**
     * The Records in each page is read into a byte array and the key to each
     * record is retrieved to be used in the insertion.
     */
    private void readPageIntoTree(BPlusTree<String, byte[]> tree, byte[] page) {
        ByteArrayInputStream pageInputStream = new ByteArrayInputStream(page);

        int bytesRead;
        while (true) {
            // THE RECORD
            byte[] record = new byte[RECORD_SIZE];
            bytesRead = 0;
            try {
                bytesRead = pageInputStream.read(record);
            } catch (Exception e) {
            }

            // If bytes read does not match record size then all records are done for this page
            if (bytesRead != RECORD_SIZE) {
                break;
            }

            try(ByteArrayInputStream recordInputStream = new ByteArrayInputStream(record)) {
                // Read the bytes into the byte array from the stream
                recordInputStream.read(DA_NAME);
            } catch (Exception e) {
            }

            // THE KEY
            String recordID = new String(DA_NAME).trim();

            if(!recordID.equals("")) {

                if (loadOption.equals("load")) {
                    tree.insert(recordID, record);
                }else if(loadOption.equals("bulk-load")) {
                    tree.bulkInsert(recordID, record);
                }

//                System.out.println(recordID + " has been added");
            }
        }
    }

    /**
     * Prompts the user for a search key to search the b+ tree.
     *
     * Will display the time taken to retrieve the entry.
     */
    private void searchAfterTreeLoaded(BPlusTree<String, byte[]> tree) {
        byte[] result;
        long startTime;
        Scanner keyboard = new Scanner(System.in);
        System.out.println();


        do {
            System.out.println("Enter a search key: ");
            String searchKey = keyboard.nextLine();
            System.out.println("Now searching for: " + searchKey);
            startTime = System.currentTimeMillis();
            result = tree.search(searchKey);

            if (result == null) {
                System.err.println("Search key not found, please try again");
                continue;
            }

            ByteArrayInputStream recordInputStream = new ByteArrayInputStream(result);
            for (byte[] byteArray : byteArrays) {
                try {
                    recordInputStream.read(byteArray);
                } catch (Exception e) {
                }
            }

            System.out.println("DeviceID: " + ByteBuffer.wrap(deviceId).getInt());
            System.out.println("Arrival time: " + new Date(ByteBuffer.wrap(arrivalTime).getLong() * 1000));
            System.out.println("Departure time: " + new Date(ByteBuffer.wrap(departureTime).getLong() * 1000));
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


            long endTime = System.currentTimeMillis();
            long timeInMilliseconds = endTime - startTime;
            System.out.println("Time taken to search tree (milliseconds): " + timeInMilliseconds);
            System.out.println("Time taken to search tree (seconds): " + (timeInMilliseconds / 1000));
        } while (true);
    }

    private void displayUsageMessage() {
        System.err.println("usage: LoadTree [<load || bulk-load>] [<page_size>] [<branching_factor> >= 3]");
        System.exit(1);
    }

    // Declare variables for each field in the csv
    private byte[] DA_NAME = new byte[DA_NAME_SIZE];
    private byte[] deviceId = new byte[DEVICE_ID_SIZE];
    private byte[] arrivalTime = new byte[ARRIVAL_TIME_SIZE];
    private byte[] departureTime = new byte[DEPARTURE_TIME_SIZE];
    private byte[] durationSeconds = new byte[DURATION_SECONDS_SIZE];
    private byte[] streetMarker = new byte[STREET_MARKER_SIZE];
    private byte[] parkingSign = new byte[PARKING_SIGN_SIZE];
    private byte[] area = new byte[AREA_SIZE];
    private byte[] streetId = new byte[STREET_ID_SIZE];
    private byte[] streetName = new byte[STREET_NAME_SIZE];
    private byte[] betweenStreet1 = new byte[BETWEEN_STREET_1_SIZE];
    private byte[] betweenStreet2 = new byte[BETWEEN_STREET_2_SIZE];
    private byte[] sideOfStreet = new byte[SIDE_OF_STREET_SIZE];
    private byte[] inViolation = new byte[IN_VIOLATION_SIZE];

    private byte[][] byteArrays = {DA_NAME, deviceId, arrivalTime, departureTime, durationSeconds, streetMarker, parkingSign, area, streetId, streetName, betweenStreet1, betweenStreet2, sideOfStreet, inViolation};


//        Test data, used for debugging
//        BPlusTree<Integer, String> tree = new BPlusTree(branchingFactor);
//        tree.insert(7, "asd");
//        tree.insert(4, "asd");
//        tree.insert(3, "asd");
//        tree.insert(6, "asd");
//        tree.insert(12, "asd");
//        tree.insert(1, "asd");
//        tree.insert(2, "asd");
//        tree.insert(15, "asd");
//        tree.insert(17, "asd");
//        tree.insert(9, "asd");
}
