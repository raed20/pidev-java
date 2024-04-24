package com.example.reclamation.models;

import java.util.Date;

public class Reclamations {
    private int id;
    private String objet;
    private String description;
    private Date date_reclamation = new Date();
    private boolean etat = false;

    private String imagePath;

    public Reclamations(int id, String objet, String description, Date date_reclamation, boolean etat) {
        this.id = id;
        this.objet = objet;
        this.description = description;
        this.date_reclamation = date_reclamation;
        this.etat = etat;
    }

    public Reclamations(String objet, String description) {
        this.objet = objet;
        this.description = description;
    }

    public Reclamations(int id, String objet, String description) {
        this.id=id;
        this.objet = objet;
        this.description = description;
    }

    public Reclamations(int id, String objet, String description, Date date_reclamation, boolean etat, String imagePath) {
        this.id = id;
        this.objet = objet;
        this.description = description;
        this.date_reclamation = date_reclamation;
        this.etat = etat;
        this.imagePath = imagePath;
    }

    public Reclamations(String objet, String description, String imagePath) {
        this.objet = objet;
        this.description = description;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_reclamation() {
        return date_reclamation;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
