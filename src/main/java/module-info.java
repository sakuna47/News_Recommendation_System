module com.example.newsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires jbcrypt;


    opens com.example.newsrecommendationsystem to javafx.fxml;
    exports com.example.newsrecommendationsystem;
    exports com.example.newsrecommendationsystem.Service;
    opens com.example.newsrecommendationsystem.Service to javafx.fxml;
    exports com.example.newsrecommendationsystem.Controllers;
    opens com.example.newsrecommendationsystem.Controllers to javafx.fxml;
    exports com.example.newsrecommendationsystem.Database;
    opens com.example.newsrecommendationsystem.Database to javafx.fxml;
    exports com.example.newsrecommendationsystem.Controllers.Admin;
    opens com.example.newsrecommendationsystem.Controllers.Admin to javafx.fxml;
    exports com.example.newsrecommendationsystem.Controllers.User;
    opens com.example.newsrecommendationsystem.Controllers.User to javafx.fxml;
}