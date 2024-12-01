package com.example.newsrecommendationsystem.Controllers.User;

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
        saveInteraction("like", null);
        articals.setText(articals.getText() + "\n\nYou liked this article.");
    }

    @FXML
    void onDislikeClick(ActionEvent event) {
        saveInteraction("dislike", null);
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
                    saveInteraction("rated", rating);
                    articals.setText(articals.getText() + "\n\nYou rated this article: " + rating + "/10");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Rating Submitted");
                    alert.setHeaderText(null);
                    alert.setContentText("Thank you! You rated this article: " + rating + "/10");
                    alert.showAndWait();
                } else {
                    showInvalidRatingAlert();
                }
            } catch (NumberFormatException e) {
                showInvalidRatingAlert();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/User/UserView.fxml"));
            AnchorPane userMenu = loader.load();
            Stage stage = (Stage) back2UserMenu.getScene().getWindow();
            stage.setScene(new Scene(userMenu));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveInteraction(String interactionType, Integer rating) {
        if (articles != null && !articles.isEmpty() && username != null) {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("CwOOD");
                MongoCollection<Document> interactionsCollection = database.getCollection("ArticleInteractions");

                Document currentArticle = articles.get(currentIndex);

                Document existingInteraction = interactionsCollection.find(new Document("articleId", currentArticle.get("_id"))
                        .append("username", username)).first();

                if (existingInteraction != null) {
                    interactionsCollection.updateOne(
                            new Document("_id", existingInteraction.get("_id")),
                            new Document("$set", new Document("interactionType", interactionType)
                                    .append("rating", rating)
                                    .append("timestamp", System.currentTimeMillis())));
                } else {
                    Document interaction = new Document("articleId", currentArticle.get("_id"))
                            .append("category", currentArticle.getString("category"))
                            .append("interactionType", interactionType)
                            .append("username", username)
                            .append("rating", rating)
                            .append("timestamp", System.currentTimeMillis());

                    interactionsCollection.insertOne(interaction);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInvalidRatingAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Rating");
        alert.setHeaderText("Invalid Rating Input");
        alert.setContentText("Please enter a number between 1 and 10.");
        alert.showAndWait();
    }
}
