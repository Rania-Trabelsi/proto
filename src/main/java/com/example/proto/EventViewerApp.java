package com.example.proto;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class EventViewerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            TableView<Event> table = new TableView<>();
            TableColumn<Event, String> subjectCol = new TableColumn<>("Matière");
            TableColumn<Event, String> teacherCol = new TableColumn<>("Enseignant");
            TableColumn<Event, String> roomCol = new TableColumn<>("Salle");
            TableColumn<Event, String> typeCol = new TableColumn<>("Type");
            TableColumn<Event, String> groupCol = new TableColumn<>("Groupe");
            TableColumn<Event, String> startCol = new TableColumn<>("Début");
            TableColumn<Event, String> endCol = new TableColumn<>("Fin");

            // Configuration des cellules
            subjectCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject()));
            teacherCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeacher()));
            roomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoom()));
            typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
            groupCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGroup()));
            startCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime().toString()));
            endCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDateTime().toString()));

            table.getColumns().addAll(subjectCol, teacherCol, roomCol, typeCol, groupCol, startCol, endCol);

            // Charger les événements depuis une URL
            String urlString = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def502009db4cc18c89d1c0f782ad7dc1b2317d1b866e9b368292e2a5046d898d796146e873142f21aff63fbf54104ed9d2841c2377ab77f27c8d24d53d7999c5a3d55a76fdf89c9c4c6e4f1041c98d55a078b4c84cd3987d1e3264a45";
            List<Event> events = IcsParser.parseIcsFileFromUrl(urlString);

            table.getItems().addAll(events);

            VBox vbox = new VBox(table);
            Scene scene = new Scene(vbox);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Visualisation des Événements");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception comme vous le souhaitez
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
