package com.example.proto;

import java.time.LocalDateTime;



public class Event {
    private String subject;
    private String teacher;
    private String room;
    private String type;
    private String group;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Event() {
        // Les champs sont initialisés à null par défaut
    }
    // Constructeur, getters et setters
    public Event(String subject, String teacher, String room, String type, String group, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.subject = subject;
        this.teacher = teacher;
        this.room = room;
        this.type = type;
        this.group = group;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
