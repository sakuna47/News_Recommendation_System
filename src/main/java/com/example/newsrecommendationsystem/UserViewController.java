package com.example.newsrecommendationsystem;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserViewController {

    @FXML
    private Button Business;

    @FXML
    private Button LNews;

    @FXML
    private Button WNews;

    @FXML
    private Button back2login;

    @FXML
    private Button entertainment;

    @FXML
    private Button health;

    @FXML
    private Button politics;

    @FXML
    private Button recommendation;

    @FXML
    private Button sport;

    @FXML
    private Button weather;

    @FXML
    private Button deleteAccount;

    private MongoClient mongoClient;
    private MongoDatabase database;

    public UserViewController() {
        // Initialize MongoDB client
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("CwOOd");
    }

    @FXML
    void onback2logincick(ActionEvent event) {
        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLogin.fxml"));
            AnchorPane loginPage = loader.load();

            // Set the scene with the login page
            Stage stage = (Stage) back2login.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLocalNewsClick(ActionEvent event) {
        try {
            // Fetch all articles to debug and check the categories
            MongoCollection<Document> collection = database.getCollection("Articles");

            // Fetch all articles to check categories
            FindIterable<Document> allArticles = collection.find();
            allArticles.forEach(doc -> System.out.println(doc.getString("category"))); // Debugging line

            // Query for the category "Local News" (case-insensitive) and also trim whitespaces
            Document localNews = collection.find(Filters.regex("category", "^\\s*Local News\\s*$", "i")).first();

            if (localNews != null) {
                System.out.println("Found Local News article: " + localNews);  // Debugging line

                // Load ArticalView.fxml to show the article
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticalView.fxml"));
                AnchorPane articalViewPage = loader.load();

                // Pass the article content to ArticalViewController
                ArticalViewController articalController = loader.getController();
                articalController.setArticalContent(localNews.getString("content"));

                // Set the scene with ArticalView
                Stage stage = (Stage) LNews.getScene().getWindow();
                stage.setScene(new Scene(articalViewPage));
                stage.show();
            } else {
                System.out.println("No Local News article found in the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
