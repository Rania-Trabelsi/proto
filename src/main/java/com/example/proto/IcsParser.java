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

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("BEGIN:VEVENT")) {
                event = new Event(null, null, null, null, null, null, null);
            } else if (line.startsWith("SUMMARY")) {
                // Extraction améliorée pour SUMMARY
                String summary = line.substring(line.indexOf(":") + 1);
                String[] details = summary.split(" - ");
                if (details.length >= 3) {
                    event.setSubject(details[0].trim());
                    event.setTeacher(details[1].trim());
                    // Utiliser la partie après le dernier " - " comme groupe
                    event.setGroup(details[details.length - 1].trim().replaceAll("\\\\", ""));
                }
            } else if (line.startsWith("LOCATION")) {
                event.setRoom(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("DTSTART")) {
                event.setStartDateTime(parseDateTime(line.substring(line.indexOf(":") + 1)));
            } else if (line.startsWith("DTEND")) {
                event.setEndDateTime(parseDateTime(line.substring(line.indexOf(":") + 1)));
            } else if (line.startsWith("DESCRIPTION")) {
                // Parse DESCRIPTION pour Type et d'autres détails
                String description = line.substring(line.indexOf(":") + 1).trim().replace("\\n", "");
                // Extrait le type de l'événement
                if (description.contains("Type :")) {
                    String type = description.substring(description.indexOf("Type :")).trim();
                    event.setType(type.split("\n")[0]); // Assurez-vous de ne capturer que la première ligne après "Type :"
                }
            } else if (line.startsWith("END:VEVENT")) {
                events.add(event);
                event = null;
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