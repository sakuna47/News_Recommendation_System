module com.example.newsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.newsrecommendationsystem to javafx.fxml;
    exports com.example.newsrecommendationsystem;
}