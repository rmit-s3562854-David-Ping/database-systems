import java.io.*;
import java.nio.ByteBuffer;
import java.util.Date;

import static org.rmit.student.tree.Constants.*;

public class dbquery {

    private String text;
    private int pageSize;
    private File file;

    public static void main(String[] args) {
        dbquery query = new dbquery();
        query.validateInputs(args);
        query.search();
    }

    /**
     * Validates the command line arguments with text and page size
     *
     * @param args The command line arguments
     */
    private void validateInputs(String[] args) {
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

    private void displayUsageMessage() {
        System.err.println("usage: dbquery [<text>] [<page_size>]");
        System.exit(1);
    }

    /**
     * This method reads an array of bytes from the heap file corresponding to the page size, hence it reads one page
     * at a time. This will continue until all pages are searched.
     */
    private void search() {
        long startTime = System.currentTimeMillis();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int bytesRead = 0;
            while (bytesRead != -1) {
                byte[] page = new byte[pageSize];
                bytesRead = fileInputStream.read(page);
                if (bytesRead != -1) {
                    searchPage(page);
                }
            }
        } catch (Exception e) {
        }

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to search (milliseconds): " + timeInMilliseconds);
        System.out.println("Time taken to search (seconds): " + (timeInMilliseconds / 1000));
    }

    /**
     * This method reads records from the page and then fields from the records, if the first field DA_NAME is equal
     * to the search text then it print out the full contents of that record. This will go on until all records in the
     * page are searched.
     */
    private void searchPage(byte[] page) {
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

        ByteArrayInputStream pageInputStream = new ByteArrayInputStream(page);

        int bytesRead;
        while (true) {
            byte[] record = new byte[RECORD_SIZE];
            bytesRead = 0;
            try {
                bytesRead = pageInputStream.read(record);
            } catch (Exception e) {
            }

            if (bytesRead != RECORD_SIZE) {
                break;
            }
            ByteArrayInputStream recordInputStream = new ByteArrayInputStream(record);

            for (byte[] byteArray : byteArrays) {
                try {
                    recordInputStream.read(byteArray);
                } catch (Exception e) {
                }
            }

            // After converting to string, need to trim to remove empty spaces that comes after (from the padding)
            String recordID = new String(DA_NAME).trim();
            if(recordID.equals(text)) {
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
            }
        }
    }
}
