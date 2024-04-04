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
        // Charger le fichier FXML pour l'authentification
        Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml")); // Assurez-vous que le chemin est correct

        // Définir le titre de la fenêtre
        primaryStage.setTitle("Connexion");

        // Définir la scène avec une taille appropriée pour la fenêtre de connexion
        primaryStage.setScene(new Scene(root, 600, 600)); // Ajustez la taille selon vos besoins

        // Afficher la fenêtre
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
