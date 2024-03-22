package entities;

import java.util.Collection;

public class Bank {
    private Integer id;

    private String nom;

    private String adresse;

    private String codeSwift;

    private String logo;

    private String phoneNum;


    public Bank() {
    }

    public Bank(String nom, String adresse, String codeSwift, String logo, String phoneNum) {
        this.nom = nom;
        this.adresse = adresse;
        this.codeSwift = codeSwift;
        this.logo = logo;
        this.phoneNum = phoneNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodeSwift() {
        return codeSwift;
    }

    public void setCodeSwift(String codeSwift) {
        this.codeSwift = codeSwift;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}

