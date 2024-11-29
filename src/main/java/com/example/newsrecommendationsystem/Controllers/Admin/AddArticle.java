package com.example.newsrecommendationsystem.Controllers.Admin;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AddArticle {

    @FXML
    private Button add;

    @FXML
    private Button back;

    @FXML
    private TextField category;

    @FXML
    private TextArea content;

    @FXML
    private TextField source;

    @FXML
    private TextField title;

    // Handle the Add button click to store the article in MongoDB
    @FXML
    public void handleAddButtonClick() {
        // Get the input from the user
        String articleTitle = title.getText();
        String articleContent = content.getText();
        String articleCategory = category.getText();
        String articleSource = source.getText();

        // Check if any field is empty
        if (articleTitle.isEmpty() || articleContent.isEmpty() || articleCategory.isEmpty() || articleSource.isEmpty()) {
            // Show an error alert if any field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled out.\nPlease fill in title, content, category, and source.");
            alert.showAndWait();
            return; // Exit the method without adding the article
        }

        try {
            // Connect to MongoDB
            MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("CwOOD"); // Connect to your database
            MongoCollection<Document> articlesCollection = database.getCollection("Articles");

            // Create a new document with the article data
            Document articleDoc = new Document("title", articleTitle)
                    .append("content", articleContent)
                    .append("category", articleCategory)
                    .append("source", articleSource);

            // Insert the document into the collection
            articlesCollection.insertOne(articleDoc);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Article added successfully.");
            alert.showAndWait();

            // Clear the fields after insertion
            title.clear();
            content.clear();
            category.clear();
            source.clear();

        } catch (Exception e) {
            // Catch any exceptions and show an error message
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the article.\nPlease try again later.");
            alert.showAndWait();
        }
    }

    // Handle the Back button click to load the AdminView.fxml
    @FXML
    public void handleBackButtonClick() {
        try {
            // Load the AdminView.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Admin/AdminView.fxml"));
            Stage stage = (Stage) back.getScene().getWindow(); // Use the back button for the current stage
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Set the Admin View scene
            stage.show(); // Display the stage
        } catch (IOException e) {
            // Catch any exceptions while loading AdminView.fxml
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while navigating to the Admin View.\nPlease try again later.");
            alert.showAndWait();
        }
    }
}
