package com.example.proto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Controller {

    @FXML
    private Button previousWeekButton;

    @FXML
    private Button nextWeekButton;

    @FXML
    private GridPane scheduleGrid;

    LocalDate currentDate = LocalDate.now();
    private int actualWeek = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());

    private void increaseWeek(){
        actualWeek++;
        if(actualWeek==53){
            actualWeek = 1;
        }
    }

    private void decreaseWeek(){
        actualWeek--;
        if(actualWeek==0){
            actualWeek=52;
        }
    }


    String urlString = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def502009db4cc18c89d1c0f782ad7" +
            "dc1b2317d1b866e9b368292e2a5046d898d796146e873142f21aff63fbf54104ed9d2841c2377ab77f27c8d24d53d7999c5a3d" +
            "55a76fdf89c9c4c6e4f1041c98d55a078b4c84cd3987d1e3264a45";

    private List<Event> events;
    {
        try {
            events = IcsParser.parseIcsFileFromUrl(urlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void previousWeekClicked() {
        decreaseWeek();
        displayEventsOnGrid(events, actualWeek);
    }

    @FXML
    private void nextWeekClicked() {
        increaseWeek();
        displayEventsOnGrid(events, actualWeek);
    }

    public void initialize() {
        generateTimeSlots();
        generateWeekDays(actualWeek);

        displayEventsOnGrid(events, actualWeek);
    }

    public void displayEventsOnGrid(List<Event> events, int weekNumber) {
        scheduleGrid.getChildren().clear();
        generateTimeSlots();
        generateWeekDays(actualWeek);

        List<Event> eventsForWeek = filterEventsByWeek(events, weekNumber);

        for (Event event : eventsForWeek) {
            if(event.getSubject() != null) {
                VBox eventLabels = new VBox();

                Label eventSubject = new Label(event.getSubject());
                Label eventTeacher = new Label(event.getTeacher());
                Label eventRoom = new Label(event.getRoom());
                Label eventType = new Label(event.getType());
                Label eventGroup = new Label(event.getGroup());

                eventLabels.getChildren().addAll(eventSubject, eventTeacher, eventRoom, eventType, eventGroup);
                eventLabels.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

                LocalTime baseTime = LocalTime.of(6, 30);
                long minutesSinceBaseTime = Duration.between(baseTime, event.getStartDateTime().toLocalTime()).toMinutes();
                System.out.println(minutesSinceBaseTime);
                int startRow;
                if (minutesSinceBaseTime >= 0) {
                    startRow = (int) (minutesSinceBaseTime / 30);
                } else {
                    startRow = 0;
                }
                System.out.println(startRow);

                int dayColumn = event.getStartDateTime().getDayOfWeek().getValue();
                long duration = java.time.Duration.between(event.getStartDateTime(), event.getEndDateTime()).toMinutes() / 30;

                scheduleGrid.add(eventLabels, dayColumn, startRow + 1, 1, (int) duration);
            }
        }
    }

    private List<Event> filterEventsByWeek(List<Event> events, int weekNumber) {
        List<Event> eventsForWeek = new ArrayList<>();
        for (Event event : events) {
            if (event.getStartDateTime().get(WeekFields.ISO.weekOfYear()) == weekNumber) {
                eventsForWeek.add(event);
            }
        }
        return eventsForWeek;
    }

    private void generateTimeSlots() {
        int rowIndex = 1; // Commence à 1 pour laisser l'en-tête
        for (LocalTime time = LocalTime.of(8, 30); !time.isAfter(LocalTime.of(19, 0)); time = time.plusMinutes(30)) {
            Label timeLabel = new Label(time.toString());
            scheduleGrid.add(timeLabel, 0, rowIndex);
            rowIndex++;
        }
    }

    private void generateWeekDays(int actualWeek) {

        LocalDate currentDate = LocalDate.now();
        int a = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        int diff = actualWeek - a;
        LocalDate firstDayOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(diff);


        for (int i = 0; i < 5; i++) {
            LocalDate day = firstDayOfWeek.plusDays(i);

            String formattedDate = day.format(DateTimeFormatter.ofPattern("dd/MM"));

            Label dayLabel = new Label(day.getDayOfWeek().toString().substring(0, 4) + " " + formattedDate);

            scheduleGrid.add(dayLabel, i + 1, 0);
        }
    }


}
