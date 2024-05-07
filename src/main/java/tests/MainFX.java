package tests;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    double x,y = 0;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Javafx/FrontOffice/FrontSidebar.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/Javafx/BackOffice/Blog/AfficheBack.fxml"));
        primaryStage.setTitle("Polygon API Demo");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
