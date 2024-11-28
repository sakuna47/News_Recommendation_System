package com.example.newsrecommendationsystem;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public class ArticalViewController {

    @FXML
    private TextArea articals;

    @FXML
    private Button back2UserMenu;

    @FXML
    private Button next;

    @FXML
    private Button previous;

    @FXML
    private Button like;

    @FXML
    private Button dislike;

    @FXML
    private Button rate;

    private List<Document> articles;
    private int currentIndex = 0;
    private String username = SessionManager.getInstance().getUsername();

    public void setArticles(List<Document> articles) {
        this.articles = articles;
        if (!articles.isEmpty()) {
            displayArticle(0);
        } else {
            articals.setText("No articles found for this category.");
        }
    }


    private void displayArticle(int index) {
        if (articles != null && index >= 0 && index < articles.size()) {
            currentIndex = index;
            Document article = articles.get(index);
            String title = article.getString("title");
            String content = article.getString("content");
            String category = article.getString("category");

            String formattedArticle = String.format("Title: %s\n\nCategory: %s\n\n%s",
                    title != null ? title : "No Title",
                    category != null ? category : "Unknown Category",
                    content != null ? content : "No Content");

            articals.setText(formattedArticle);
        }
    }

    @FXML
    void onLikeClick(ActionEvent event) {
        saveInteraction("like", null); // No rating for like interaction
        articals.setText(articals.getText() + "\n\nYou liked this article.");
    }

    @FXML
    void onDislikeClick(ActionEvent event) {
        saveInteraction("dislike", null); // No rating for dislike interaction
        articals.setText(articals.getText() + "\n\nYou disliked this article.");
    }

    @FXML
    void onRateClick(ActionEvent event) {
        TextInputDialog ratingDialog = new TextInputDialog();
        ratingDialog.setTitle("Rate the Article");
        ratingDialog.setHeaderText("Please rate the article (1 to 10):");
        ratingDialog.setContentText("Rating:");

        Optional<String> result = ratingDialog.showAndWait();
        if (result.isPresent()) {
            try {
                int rating = Integer.parseInt(result.get());
                if (rating >= 1 && rating <= 10) {
                    saveInteraction("rated", rating); // Save rating interaction
                    articals.setText(articals.getText() + "\n\nYou rated this article: " + rating + "/10");
                } else {
                    articals.setText(articals.getText() + "\n\nInvalid rating! Please enter a number between 1 and 10.");
                }
            } catch (NumberFormatException e) {
                articals.setText(articals.getText() + "\n\nInvalid input! Please enter a number.");
            }
        }
    }

    @FXML
    void onNextClick(ActionEvent event) {
        if (articles != null && currentIndex < articles.size() - 1) {
            displayArticle(currentIndex + 1);
        }
    }

    @FXML
    void onPreviousClick(ActionEvent event) {
        if (articles != null && currentIndex > 0) {
            displayArticle(currentIndex - 1);
        }
    }

    @FXML
    void onBackToUserMenuClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));
            AnchorPane userMenu = loader.load();
            Stage stage = (Stage) back2UserMenu.getScene().getWindow();
            stage.setScene(new Scene(userMenu));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save interaction (like, dislike, or rating)
    private void saveInteraction(String interactionType, Integer rating) {
        if (articles != null && !articles.isEmpty() && username != null) {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("CwOOD");
                MongoCollection<Document> interactionsCollection = database.getCollection("ArticleInteractions");

                Document currentArticle = articles.get(currentIndex);

                // Check if the user has already interacted with this article
                Document existingInteraction = interactionsCollection.find(new Document("articleId", currentArticle.get("_id"))
                        .append("username", username)).first();

                if (existingInteraction != null) {
                    // Update the existing interaction (with rating if available)
                    interactionsCollection.updateOne(
                            new Document("_id", existingInteraction.get("_id")),
                            new Document("$set", new Document("interactionType", interactionType)
                                    .append("rating", rating) // Add the rating field
                                    .append("timestamp", System.currentTimeMillis())));
                } else {
                    // Insert a new interaction with rating field if rating is not null
                    Document interaction = new Document("articleId", currentArticle.get("_id"))
                            .append("category", currentArticle.getString("category"))
                            .append("interactionType", interactionType)
                            .append("username", username)
                            .append("rating", rating) // Include rating in the interaction
                            .append("timestamp", System.currentTimeMillis());

                    interactionsCollection.insertOne(interaction);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Save rating in Ratings collection
    private void saveRating(int rating) {
        if (articles != null && !articles.isEmpty() && username != null) {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("CwOOD");
                MongoCollection<Document> ratingsCollection = database.getCollection("Ratings");

                Document currentArticle = articles.get(currentIndex);

                // Check if the user has already rated the article
                Document existingRating = ratingsCollection.find(new Document("articleId", currentArticle.get("_id"))
                        .append("username", username)).first();

                if (existingRating != null) {
                    // Update the existing rating
                    ratingsCollection.updateOne(
                            new Document("_id", existingRating.get("_id")),
                            new Document("$set", new Document("rating", rating)
                                    .append("timestamp", System.currentTimeMillis())));
                } else {
                    // Insert a new rating
                    Document ratingDoc = new Document("articleId", currentArticle.get("_id"))
                            .append("category", currentArticle.getString("category"))
                            .append("rating", rating)
                            .append("username", username)
                            .append("timestamp", System.currentTimeMillis());

                    ratingsCollection.insertOne(ratingDoc);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
