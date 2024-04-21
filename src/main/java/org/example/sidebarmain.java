package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class sidebarmain extends Application{
    double x,y = 0;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Javafx/FrontOffice/Sidebar.fxml"));
        primaryStage.setTitle("Polygon API Demo");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}