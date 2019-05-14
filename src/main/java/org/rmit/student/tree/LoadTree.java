package org.rmit.student.tree;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import static org.rmit.student.tree.Constants.*;

public class LoadTree {

    private String loadOption;
    private int pageSize;
    private int branchingFactor;
    private File file;

    public static void main(String[] args) {
        LoadTree loadTree = new LoadTree();
        loadTree.validateInputs(args);
        loadTree.loadTree();
    }

    /**
     * Validates the command line arguments with text and page size
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

    // Choose between load and bulk load
    private void loadTree() {
        switch (loadOption) {
            case "load":
                load();
                break;
            case "bulk-load":
                bulkLoad();
                break;
        }
    }

    /**
     * Normal load, loads the nodes into the tree one by one
     */
    private void load() {
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

        long startTime = System.currentTimeMillis();

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            int bytesRead = 0;
            while (bytesRead != -1) {
                byte[] page = new byte[pageSize];
                bytesRead = fileInputStream.read(page);
                if (bytesRead != -1) {
                    loadSampleData(page);
                }
            }
        } catch (Exception e) {
        }
        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time taken to search (milliseconds): " + timeInMilliseconds);
        System.out.println("Time taken to search (seconds): " + (timeInMilliseconds / 1000));
    }


    private void loadSampleData(byte[] page) {
        // THE KEY
        byte[] DA_NAME = new byte[DA_NAME_SIZE];

        ByteArrayInputStream pageInputStream = new ByteArrayInputStream(page);

        int bytesRead;
        BPlusTree<String, byte[]> tree = new BPlusTree(branchingFactor);
        while (true) {
            // THE DATA
            byte[] record = new byte[RECORD_SIZE];
            bytesRead = 0;
            try {
                bytesRead = pageInputStream.read(record);
            } catch (Exception e) {
            }

            // If bytes read does not match record size then all pages/records are finished
            if (bytesRead != RECORD_SIZE) {
                break;
            }

            try(ByteArrayInputStream recordInputStream = new ByteArrayInputStream(record)) {
                // Read the bytes into the byte array from the stream
                recordInputStream.read(DA_NAME);
            } catch (Exception e) {
            }

            // THE KEY
            String recordID = new String(DA_NAME);

            if(!recordID.trim().equals("")) {
                // BASIC TREE INSERTION
                tree.insert(recordID, record);
            }
        }
    }



    private void bulkLoad() {
        // assume file is sorted or call external merge sort on it first
    }

    private void displayUsageMessage() {
        System.err.println("usage: dbquery [<load || bulk-load>] [<page_size>] [<branching_factor> >= 3]");
        System.exit(1);
    }
}
