package com.example.newsrecommendationsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ArticalViewController {

    @FXML
    private TextArea articals;

    @FXML
    private Button back2UserMenu;

    @FXML
    private Button dislike;

    @FXML
    private Button like;

    @FXML
    private Button next;

    @FXML
    private Button previous;

    @FXML
    private Button rate;

    public void setArticalContent(String content) {
        articals.setText(content);
    }

    @FXML
    void onBackToUserMenuClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));
            AnchorPane userMenuPage = loader.load();

            Stage stage = (Stage) back2UserMenu.getScene().getWindow();
            stage.setScene(new Scene(userMenuPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
