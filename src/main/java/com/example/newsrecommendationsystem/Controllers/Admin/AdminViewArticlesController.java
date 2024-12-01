package com.example.newsrecommendationsystem.Controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;

import java.io.IOException;
import java.util.List;

public class AdminViewArticlesController {

    @FXML
    private TextArea articles;

    @FXML
    private Button back;

    @FXML
    private Button removeArticles;

    // Method to handle the back button click
    @FXML
    private void handleBackClick() throws IOException {
        // Load the AdminView.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Admin/AdminView.fxml"));
        AnchorPane adminView = loader.load();  // Load the view


        Stage stage = (Stage) back.getScene().getWindow();

        // Set the new scene with the loaded AdminView.fxml
        stage.setScene(new Scene(adminView));
    }

    // Method to load articles from MongoDB into the TextArea
    @FXML
    public void initialize() {
        // Connect to the MongoDB database
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("CwOOD");
            MongoCollection<Document> collection = database.getCollection("Articles");

            // Fetch all articles from the collection
            List<Document> articlesList = collection.find().into(new java.util.ArrayList<>());

            // Build a string to display the articles
            StringBuilder articlesContent = new StringBuilder();
            for (Document article : articlesList) {
                String title = article.getString("title");
                String content = article.getString("content");

                // Append the article content in a readable format
                articlesContent.append("Title: ").append(title).append("\n")
                        .append("Content: ").append(content).append("\n\n")
                        .append("----------------------------------------------------------\n\n");
            }

            // Set the text in the TextArea
            articles.setText(articlesContent.toString());
        } catch (Exception e) {
            e.printStackTrace();
            articles.setText("Error loading articles from database.");
        }
    }

    // Method to handle remove article button click
    @FXML
    private void handleRemoveArticleClick() {
        // Get the selected text from the TextArea
        String selectedText = articles.getSelectedText();

        if (selectedText.isEmpty()) {
            // If no text is selected, inform the user
            articles.appendText("\n\nPlease select an article to remove.");
            return;
        }

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("CwOOD");
            MongoCollection<Document> collection = database.getCollection("Articles");

            //  remove an article based on the selected text (either title or content)
            //  use the title to identify the article to delete
            Document query = new Document("title", selectedText);

            // Find and delete the article that matches the title (or content)
            Document deletedArticle = collection.findOneAndDelete(query);

            if (deletedArticle != null) {

                articles.appendText("\n\nArticle with title \"" + selectedText + "\" has been deleted.");
            } else {

                articles.appendText("\n\nNo article found with the selected title.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            articles.appendText("\n\nError removing article from database.");
        }
    }
}