package org.rmit.student;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            invalidArgs();
        }

        int option = Integer.parseInt(args[0]);
        switch (option) {
            case 1:
                Derby.bulkLoad();
                break;
            default:
                invalidArgs();
        }
    }

    private static void invalidArgs() {
        System.out.println("Enter one the following as an argument");
        System.out.println("1. Load Derby and insert data using bulk loading, output time taken");
        System.exit(1);
    }
}
