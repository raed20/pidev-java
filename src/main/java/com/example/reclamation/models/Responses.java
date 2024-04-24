package com.example.reclamation.models;

import java.util.Date;

public class Responses {
    private int id;
    private String message;
    private Date date;
    private Reclamations reclamation;

    public Responses(int id, String message, Date date, Reclamations reclamation) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.reclamation = reclamation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Reclamations getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamations reclamation) {
        this.reclamation = reclamation;
    }
}
