package com.example.proto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));

        scene = new Scene(root, 800, 600);
        applyLightTheme(); // Appliquer le thème clair par défaut

        primaryStage.setTitle("Emploi du Temps");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void applyDarkTheme() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
    }

    public void applyLightTheme() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/light-theme.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
