package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainUser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FrontOffice.Login/login.fxml"));
        primaryStage.setTitle("ProSync");
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();
    }

    public  static void main(String[] args) {
        launch(args);
    }
}

