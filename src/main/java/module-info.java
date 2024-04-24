module com.example.reclamation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mail;
    requires org.apache.pdfbox;


    opens com.example.reclamation to javafx.fxml;
    exports com.example.reclamation;
    opens com.example.reclamation.controllers to javafx.fxml;
    exports com.example.reclamation.controllers;
    opens com.example.reclamation.config to javafx.fxml;
    exports com.example.reclamation.config;
    opens com.example.reclamation.models to javafx.base;
}