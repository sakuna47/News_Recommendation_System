package com.example.newsrecommendationsystem.Controllers.Admin;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.util.List;

public class ArticleService {

    private static final String DATABASE_URL = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "CwOOD";
    private static final String COLLECTION_NAME = "Articles";

    // Fetch all articles from the database
    public List<Document> getAllArticles() {
        try (var mongoClient = MongoClients.create(DATABASE_URL)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            return collection.find().into(new java.util.ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Add a new article to the database
    public boolean addArticle(String title, String content, String category, String source) {
        try (var mongoClient = MongoClients.create(DATABASE_URL)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Create a new article document
            Document articleDoc = new Document("title", title)
                    .append("content", content)
                    .append("category", category)
                    .append("source", source);

            // Insert the new article into the collection
            collection.insertOne(articleDoc);

            return true;  // Successfully added the article
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Error occurred while adding the article
        }
    }

    // Delete an article by its title
    public boolean deleteArticleByTitle(String title) {
        try (var mongoClient = MongoClients.create(DATABASE_URL)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("title", title);
            Document deletedArticle = collection.findOneAndDelete(query);

            return deletedArticle != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
