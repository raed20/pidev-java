package tests;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //FXMLLoader loader=new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/Command/Market.fxml"));
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/Javafx/BackOffice/BackSidebar.fxml"));
        try {
            Parent root=loader.load();
            Scene scene=new Scene(root);
            primaryStage.setTitle("Prosync");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}