package com.example.kursovaya;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/kursovaya/Main.fxml"));
        primaryStage.setTitle("Організація таксі");
        primaryStage.setScene(new Scene(root, 400, 400)); // Задаємо розмір вікна
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
