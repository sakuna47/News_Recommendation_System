package com.example.newsrecommendationsystem;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Choose_Person.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Simulate multiple users logging in
        List<String> usernames = Arrays.asList("user1", "user2", "user3");
        startBackgroundTasks(usernames);
    }

    private void startBackgroundTasks(List<String> usernames) {
        for (String username : usernames) {
            Task<Void> backgroundTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    System.out.println("Fetching recommendations for user: " + username);
                    Thread.sleep(2000); // Simulate delay
                    return null;
                }

                @Override
                protected void succeeded() {
                    System.out.println("Background task completed for " + username + "!");
                }

                @Override
                protected void failed() {
                    System.out.println("Background task failed for " + username + "!");
                }
            };

            Thread taskThread = new Thread(backgroundTask);
            taskThread.setDaemon(true);
            taskThread.start();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
