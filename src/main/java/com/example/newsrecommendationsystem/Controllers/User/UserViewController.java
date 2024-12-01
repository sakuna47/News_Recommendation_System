package com.example.newsrecommendationsystem.Controllers.User;

import com.example.newsrecommendationsystem.Service.ArticleRecommender;
import com.example.newsrecommendationsystem.Service.JaccardArticleCategorizer;
import com.example.newsrecommendationsystem.Service.SessionManager;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public class UserViewController {

    @FXML
    private Button Business;

    @FXML
    private Button mlModel;

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

    public UserViewController() {
    }

    @FXML
    void onback2logincick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/User/UserLogin.fxml"));
            AnchorPane loginPage = loader.load();
            Stage stage = (Stage) back2login.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCategoryClick(ActionEvent event) {
        try {
            Button clickedButton = (Button) event.getSource();
            String category = clickedButton.getText();
            JaccardArticleCategorizer categorizer = new JaccardArticleCategorizer();
            List<Document> articles = categorizer.getArticlesByCategory(category);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/User/ArticalView.fxml"));
            AnchorPane articlePage = loader.load();
            ArticalViewController controller = loader.getController();
            controller.setArticles(articles);

            Stage stage = (Stage) clickedButton.getScene().getWindow();
            stage.setScene(new Scene(articlePage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRecommendationClick(ActionEvent event) {
        try {
            // Instantiate ArticleRecommender
            ArticleRecommender recommender = new ArticleRecommender();

            // Fetch recommendations for the logged-in user
            List<Document> recommendedArticles = recommender.recommendArticles(SessionManager.getInstance().getUsername());

            // Load ArticalView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/User/ArticalView.fxml"));
            AnchorPane articlePage = loader.load();

            // Pass the recommended articles to ArticalViewController
            ArticalViewController controller = loader.getController();
            controller.setArticles(recommendedArticles);

            // Show the article view
            Stage stage = (Stage) recommendation.getScene().getWindow();
            stage.setScene(new Scene(articlePage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onDeleteAccountClick(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete your account?");
            alert.setContentText("This action is irreversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteUserFromDatabase();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Account Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your account and associated interactions have been successfully deleted.");
                successAlert.showAndWait();

                // Redirect to login page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/User/UserLogin.fxml"));
                AnchorPane loginPage = loader.load();
                Stage stage = (Stage) deleteAccount.getScene().getWindow();
                stage.setScene(new Scene(loginPage));
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUserFromDatabase() {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("CwOOD");

            // Access the "Users" collection
            MongoCollection<Document> usersCollection = database.getCollection("Users");

            // Delete the user document
            long deletedCount = usersCollection.deleteOne(new Document("email", SessionManager.getInstance().getUsername())).getDeletedCount();

            if (deletedCount > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found in the 'Users' collection.");
            }

            // List of collections to clean up user interactions
            String[] interactionCollections = {
                    "ArticleInteractions", "Ratings", "Comments", "UserPreferences", "UserHistory"
            };

            // Delete user-related data in each collection
            for (String collectionName : interactionCollections) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                long interactionDeletedCount = collection.deleteMany(new Document("username", SessionManager.getInstance().getUsername())).getDeletedCount();
                System.out.println(interactionDeletedCount + " documents deleted from " + collectionName);
            }

            // Clear the session
            SessionManager.getInstance().clearSession();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
