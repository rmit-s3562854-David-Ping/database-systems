package org.rmit.student;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Mongo {

    private static final String DATABASE_NAME = "myDB";
    private static final String COLLECTION_NAME = "parking_records";
    private static final String FILE_NAME = "parking-records.json";

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection<Document> collection;

    public static void bulkLoad() {
        connect();
        importJson();
    }

    private static void connect() {
        try {
            mongoClient = new MongoClient();
            mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);
            collection = mongoDatabase.getCollection(COLLECTION_NAME);
            collection.drop();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void importJson(){
        Runtime runtime = Runtime.getRuntime();

        long startTime = System.currentTimeMillis();
        Process process = null;
        String command = "mongoimport --db myDB --collection " + COLLECTION_NAME + " --file " + FILE_NAME + " --jsonArray";
        try {
            process = runtime.exec(command);
            process.waitFor();

            System.out.println("Imported json to mongo");
        } catch (Exception e){
            System.out.println("Error executing " + command + e.toString());
        }
//        System.out.println(process.getOutputStream().toString());

        long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime - startTime;
        System.out.println("Time (seconds): " + (timeInMilliseconds / 1000));
    }


}