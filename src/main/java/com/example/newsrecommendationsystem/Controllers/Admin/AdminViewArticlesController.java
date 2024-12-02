package com.example.newsrecommendationsystem.Controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


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
            // No article selected
            articles.appendText("\n\nPlease select an article to remove.");
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("No Selection");
            noSelectionAlert.setHeaderText("No Article Selected");
            noSelectionAlert.setContentText("Please select an article's title to delete.");
            noSelectionAlert.showAndWait();
            return;
        }

        // Display confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this article?");
        confirmAlert.setContentText("Selected article title: " + selectedText);

        // Wait for the user's response
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed deletion
                try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase database = mongoClient.getDatabase("CwOOD");
                    MongoCollection<Document> collection = database.getCollection("Articles");

                    // Create a query to find and delete the article
                    Document query = new Document("title", selectedText);
                    Document deletedArticle = collection.findOneAndDelete(query);

                    if (deletedArticle != null) {
                        // Successfully deleted the article
                        articles.appendText("\n\nArticle with title \"" + selectedText + "\" has been deleted.");
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Article with title \"" + selectedText + "\" has been successfully deleted.");
                        successAlert.showAndWait();
                    } else {
                        // No matching article found
                        articles.appendText("\n\nNo article found with the selected title \"" + selectedText + "\".");
                        Alert notFoundAlert = new Alert(Alert.AlertType.INFORMATION);
                        notFoundAlert.setTitle("Article Not Found");
                        notFoundAlert.setHeaderText(null);
                        notFoundAlert.setContentText("No article found with the selected title.");
                        notFoundAlert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    articles.appendText("\n\nError occurred while trying to delete the article.");
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Database Error");
                    errorAlert.setContentText("An error occurred while trying to delete the article. Please try again.");
                    errorAlert.showAndWait();
                }
            } else {
                // User canceled deletion
                articles.appendText("\n\nDeletion canceled for article titled \"" + selectedText + "\".");
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Deletion Canceled");
                cancelAlert.setHeaderText(null);
                cancelAlert.setContentText("Deletion has been canceled. The article remains unchanged.");
                cancelAlert.showAndWait();
            }
        });
    }

}