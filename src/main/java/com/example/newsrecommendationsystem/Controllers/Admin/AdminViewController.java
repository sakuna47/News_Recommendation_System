package com.example.newsrecommendationsystem.Controllers.Admin;

import com.mongodb.client.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminViewController {

    @FXML
    private Button generateReport;
    @FXML
    private Button back2AdminLogin;
    @FXML
    private Button adminViewArticles;
    @FXML
    private Button removeUsers;
    @FXML
    private TextArea users;
    @FXML
    private Button addArticles;

    // MongoDB connection details
    private static final String DATABASE_NAME = "CwOOD";
    private static final String USERS_COLLECTION = "Users";
    private static final String INTERACTIONS_COLLECTION = "ArticleInteractions";
    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // Replace with your MongoDB URI if different

    // Log file path
    private static final String LOG_FILE_PATH = "admin_user_activity_log.txt";

    // Helper method to log actions
    private void logAction(String action) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.append(action).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Logging Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while saving the log report.");
            alert.showAndWait();
        }
    }

    // Handle the Back button click to navigate back to the Admin Login screen
    @FXML
    public void handleBackClick() {
        try {
            logAction("Admin navigated back to the Admin Login screen.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Admin/AdminLogin.fxml"));
            Stage stage = (Stage) back2AdminLogin.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
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
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION);

            List<Document> userDocuments = collection.find().into(new ArrayList<>());
            StringBuilder usersText = new StringBuilder();

            if (userDocuments.isEmpty()) {
                usersText.append("No users found in the database.\n");
            } else {
                for (Document doc : userDocuments) {
                    String id = doc.getObjectId("_id").toString();
                    String name = doc.getString("name");
                    String email = doc.getString("email");

                    usersText.append("ID: ").append(id).append("\n");
                    usersText.append("Name: ").append(name).append("\n");
                    usersText.append("Email: ").append(email).append("\n");
                    usersText.append("-----------------------------\n");
                }
            }

            Platform.runLater(() -> users.setText(usersText.toString()));
            logAction("Admin viewed the list of users.");
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
        String selectedText = users.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
            return;
        }

        String[] lines = selectedText.split("\n");
        String email = Arrays.stream(lines).filter(line -> line.startsWith("Email: ")).findFirst().map(line -> line.substring(7).trim()).orElse(null);

        // Extract email after "Email: "

        if (email == null || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No valid email found in the selection.");
            alert.showAndWait();
            return;
        }

        // Confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this user and their associated interactions?");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        // Show confirmation dialog and handle the response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
                    MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                    MongoCollection<Document> usersCollection = database.getCollection(USERS_COLLECTION);
                    MongoCollection<Document> interactionsCollection = database.getCollection(INTERACTIONS_COLLECTION);

                    // Delete the user from the Users collection
                    Document userQuery = new Document("email", email);
                    long deletedUserCount = usersCollection.deleteOne(userQuery).getDeletedCount();

                    if (deletedUserCount > 0) {
                        // Delete all interactions associated with the email in ArticleInteractions
                        Document interactionQuery = new Document("username", email);
                        long deletedInteractionsCount = interactionsCollection.deleteMany(interactionQuery).getDeletedCount();

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("User and their interactions successfully deleted.\n" +
                                "Deleted interactions: " + deletedInteractionsCount);
                        successAlert.showAndWait();

                        // Reload the users list in the TextArea
                        loadUsersFromDatabase();
                        logAction("Admin deleted user with email: " + email +
                                " and " + deletedInteractionsCount + " associated interactions.");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("No user found with the specified email.");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred while deleting the user.");
                    alert.showAndWait();
                }
            }
        });
    }



    @FXML
    public void handleViewArticlesClick() {
        try {
            logAction("Admin viewed the articles.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Admin/AdminViewArticles.fxml"));
            Stage stage = (Stage) adminViewArticles.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the View Articles screen.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleAddArticleClick() {
        try {
            logAction("Admin accessed the Add Article screen.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Admin/AddArticle.fxml"));
            Stage stage = (Stage) addArticles.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the Add Article screen.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleGenerateReportClick() {
        try {
            logAction("Admin generated a report.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Generated");
            alert.setHeaderText(null);
            alert.setContentText("User activity report has been generated.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while generating the report.");
            alert.showAndWait();
        }
    }
}
