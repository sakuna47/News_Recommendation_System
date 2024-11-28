package com.example.newsrecommendationsystem;

import com.mongodb.client.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewController {

    @FXML
    private Button back2AdminLogin;// The Back button defined in the FXML file

    @FXML
    private Button adminViewArticles;


    @FXML
    private Button removeUsers; // Button to remove a selected user

    @FXML
    private TextArea users; // TextArea to display user information

    @FXML
    private Button addArticles; // Button to add articles

    // MongoDB connection details
    private static final String DATABASE_NAME = "CwOOD";
    private static final String COLLECTION_NAME = "Users";
    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // Replace with your MongoDB URI if different

    // Handle the Back button click to navigate back to the Admin Login screen
    @FXML
    public void handleBackClick() {
        try {
            // Load the AdminLogin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/AdminLogin.fxml"));
            Stage stage = (Stage) back2AdminLogin.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Set the Admin Login scene
            stage.show(); // Display the stage
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while navigating back to the Admin Login screen.");
            alert.showAndWait();
        }
    }

    // Load all users from MongoDB into the TextArea
    @FXML
    public void initialize() {
        // Fetch users from MongoDB when the AdminView is initialized
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Retrieve all user documents from the collection
            List<Document> userDocuments = collection.find().into(new ArrayList<>());
            StringBuilder usersText = new StringBuilder();

            // Check if there are no users
            if (userDocuments.isEmpty()) {
                usersText.append("No users found in the database.\n");
            } else {
                // Process each user document and append information to the TextArea content
                for (Document doc : userDocuments) {
                    String id = doc.getObjectId("_id").toString(); // Get the unique ObjectId
                    String name = doc.getString("name");
                    String email = doc.getString("email");

                    // Build the text to show in the TextArea
                    usersText.append("ID: ").append(id).append("\n");
                    usersText.append("Name: ").append(name).append("\n");
                    usersText.append("Email: ").append(email).append("\n");
                    usersText.append("-----------------------------\n");
                }
            }

            // Update the TextArea with the user data
            Platform.runLater(() -> {
                users.setText(usersText.toString());
            });

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading users from the database.");
            alert.showAndWait();
        }
    }

    // Handle the Remove User button click to delete a selected user
    @FXML
    public void handleRemoveUserClick() {
        // Get the selected text from the TextArea
        String selectedText = users.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            // Show an alert if no user is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
            return;
        }

        // Extract the user ID (assuming it's in the format "ID: <ObjectId>")
        String[] lines = selectedText.split("\n");
        String userId = null;
        for (String line : lines) {
            if (line.startsWith("ID: ")) {
                userId = line.substring(4).trim(); // Get the ObjectId part
                break;
            }
        }

        if (userId == null) {
            // Show an alert if no ID is found
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No valid user ID found in the selection.");
            alert.showAndWait();
            return;
        }

        // Connect to MongoDB and delete the user with the specified ID
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Delete the user document by ID
            Document query = new Document("_id", new ObjectId(userId));
            long deletedCount = collection.deleteOne(query).getDeletedCount();

            if (deletedCount > 0) {
                // Show a success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User successfully deleted.");
                alert.showAndWait();

                // Refresh the TextArea to show updated user list
                loadUsersFromDatabase();
            } else {
                // Show an alert if no user was deleted
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("No user found with the specified ID.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while deleting the user.");
            alert.showAndWait();
        }
    }
    @FXML
    public void handleViewArticlesClick() {
        try {
            // Load the AdminViewArticles.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/AdminViewArticles.fxml"));
            Stage stage = (Stage)adminViewArticles .getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Set the Admin View Articles scene
            stage.show(); // Display the new scene
        } catch (IOException e) {
            e.printStackTrace();
            // Show an error alert if loading AdminViewArticles.fxml fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the View Articles screen.");
            alert.showAndWait();
        }
    }

    // Handle the Add Articles button click to load AddArticle.fxml
    @FXML
    public void handleAddArticleClick() {
        try {
            // Load the AddArticle.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/AddArticle.fxml"));
            Stage stage = (Stage) addArticles.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Set the AddArticle scene
            stage.show(); // Display the stage
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the Add Article screen.");
            alert.showAndWait();
        }
    }
}
