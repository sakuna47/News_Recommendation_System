module com.example.newsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires jbcrypt;


    opens com.example.newsrecommendationsystem to javafx.fxml;
    exports com.example.newsrecommendationsystem;
}