package com.example.proto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Controller {

    @FXML
    private GridPane scheduleGrid;


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

    public void initialize() {
        generateTimeSlots();
        generateWeekDays();

        LocalDate currentDate = LocalDate.now();
        int weekNumber = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());

        displayEventsOnGrid(events, weekNumber);
    }

    public void displayEventsOnGrid(List<Event> events, int weekNumber) {
        scheduleGrid.getChildren().clear();
        generateTimeSlots();
        generateWeekDays();

        List<Event> eventsForWeek = filterEventsByWeek(events, weekNumber);

        for (Event event : eventsForWeek) {
            VBox eventLabels = new VBox();

            Label eventSubject = new Label(event.getSubject());
            Label eventTeacher = new Label(event.getTeacher());
            Label eventRoom = new Label(event.getRoom());
            Label eventType = new Label(event.getType());
            Label eventGroup = new Label(event.getGroup());

            eventLabels.getChildren().addAll(eventSubject, eventTeacher, eventRoom, eventType, eventGroup);

            LocalTime baseTime = LocalTime.of(8, 30);
            long minutesSinceBaseTime = Duration.between(baseTime, event.getStartDateTime().toLocalTime()).toMinutes();

            int startRow;
            if (minutesSinceBaseTime >= 0) {
                startRow = (int) (minutesSinceBaseTime / 30);
            } else {
                startRow = 0;
            }

            int dayColumn = event.getStartDateTime().getDayOfWeek().getValue();
            long duration = java.time.Duration.between(event.getStartDateTime(), event.getEndDateTime()).toMinutes() / 30;

            scheduleGrid.add(eventLabels, dayColumn, startRow+1, 1, (int) duration);
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

    private void generateWeekDays() {
        LocalDate currentDate = LocalDate.now();
        int currentDayOfWeek = currentDate.getDayOfWeek().getValue();

        for (int i = 0; i < 5; i++) {
            LocalDate date = currentDate.plusDays(i - currentDayOfWeek + 1);

            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM"));

            Label dayLabel = new Label(date.getDayOfWeek().toString().substring(0, 3) + " " + formattedDate);

            scheduleGrid.add(dayLabel, i + 1, 0);
        }
    }


}
