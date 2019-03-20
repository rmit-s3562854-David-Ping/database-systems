package org.rmit.student;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }

        if (Integer.parseInt(args[0]) == 1) {
            Derby.load();
        } else {
            usage();
        }
    }

    private static void usage() {
        System.out.println("Usage: enter one the following as an argument");
        System.out.println("1. Load Derby and insert data, output time taken");

        System.exit(1);
    }
}
