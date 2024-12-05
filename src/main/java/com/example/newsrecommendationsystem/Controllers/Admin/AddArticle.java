package com.example.newsrecommendationsystem.Controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    private final ArticleService articleService = new ArticleService();

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
            showAlert("Error", "All fields must be filled out.\nPlease fill in title, content, category, and source.", Alert.AlertType.ERROR);
            return; // Exit the method without adding the article
        }

        // Attempt to add the article
        boolean success = articleService.addArticle(articleTitle, articleContent, articleCategory, articleSource);

        if (success) {
            showAlert("Success", "Article added successfully.", Alert.AlertType.INFORMATION);
            // Clear the fields after insertion
            clearFields();
        } else {
            showAlert("Error", "An error occurred while adding the article.\nPlease try again later.", Alert.AlertType.ERROR);
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear all input fields
    private void clearFields() {
        title.clear();
        content.clear();
        category.clear();
        source.clear();
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
            showAlert("Error", "An error occurred while navigating to the Admin View.\nPlease try again later.", Alert.AlertType.ERROR);
        }
    }
}
