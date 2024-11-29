package com.example.newsrecommendationsystem.Database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Database {

    private static final String URI = "mongodb://localhost:27017"; // MongoDB connection URI
    private static final String DATABASE_NAME = "CwOOD"; // Database name
    private static MongoClient mongoClient = null;

    static {
        try {
            // Initialize the MongoDB client
            mongoClient = MongoClients.create(URI);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    /**
     * Returns the MongoDatabase instance.
     *
     * @return the MongoDatabase object
     */
    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    /**
     * Closes the MongoDB client.
     */
    public static void closeClient() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
