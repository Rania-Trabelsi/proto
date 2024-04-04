package com.example.proto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Vérifier les identifiants (dans un vrai scénario, vérifiez contre une base de données ou un service d'authentification)
        if ("user".equals(username) && "pass".equals(password)) {
            Parent emploiDuTempsView = FXMLLoader.load(getClass().getResource("/main.fxml")); // Assurez-vous que le chemin est correct
            Scene scene = new Scene(emploiDuTempsView);

            // Obtenez le Stage actuel et définissez la nouvelle scène
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Utilisez n'importe quel composant de la vue actuelle pour obtenir le Stage
            stage.setScene(scene);
            stage.setTitle("Emploi du Temps"); // Optionnel : Changez le titre de la fenêtre
            stage.show();

        } else {
            System.out.println("Échec de l'authentification");
            // Afficher un message d'erreur ou réinitialiser les champs
        }
    }
}
