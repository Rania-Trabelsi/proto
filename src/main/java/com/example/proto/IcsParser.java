package com.example.proto;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class IcsParser {
    public static List<Event> parseIcsFileFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        List<Event> events = new ArrayList<>();
        String line;
        Event event = null;
        String previousLine = null; // Ajouté pour conserver la ligne précédente

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("BEGIN:VEVENT")) {
                event = new Event(null, null, null, null, null, null, null);
            } else if (line.startsWith("SUMMARY")) {
                String summary = line.substring(line.indexOf(":") + 1);
                String[] details = summary.split(" - ");
                if (details.length >= 3) {
                    event.setSubject(details[0].trim());
                    event.setTeacher(details[1].trim());
                    event.setGroup(details[details.length - 1].trim().replaceAll("\\\\", ""));
                }
            } else if (line.startsWith("LOCATION")) {
                // Extraction des deux caractères précédents comme "type"
                if (previousLine != null && previousLine.length() >= 2) {
                    String potentialType = previousLine.substring(previousLine.length() - 2);
                    if (potentialType.matches("[A-Za-z]{2}")) { // Vérifie si les deux derniers caractères sont alphabétiques
                        event.setType(potentialType);
                    }
                }
                event.setRoom(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("DTSTART")) {
                event.setStartDateTime(parseDateTime(line.substring(line.indexOf(":") + 1)));
            } else if (line.startsWith("DTEND")) {
                event.setEndDateTime(parseDateTime(line.substring(line.indexOf(":") + 1)));
            } else if (line.startsWith("DESCRIPTION")) {

            }
            // Mise à jour de la ligne précédente à la fin de la boucle
            previousLine = line;

            if (line.startsWith("END:VEVENT")) {
                events.add(event);
                event = null;
                previousLine = null; // Réinitialiser la ligne précédente pour le prochain événement
            }
        }
        reader.close();
        return events;
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            return LocalDateTime.parse(dateTimeStr, fullDateTimeFormatter);
        } catch (DateTimeParseException e) {
            try {
                LocalDate date = LocalDate.parse(dateTimeStr, dateFormatter);
                return LocalDateTime.of(date, LocalTime.MIDNIGHT);
            } catch (DateTimeParseException e2) {
                throw new DateTimeParseException("Format de date non reconnu: " + dateTimeStr, e2.getParsedString(), e2.getErrorIndex());
            }
        }
    }
}
