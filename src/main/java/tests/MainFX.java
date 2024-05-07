package tests;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainFX extends Application {
    private static Stage primaryStage;



    @Override
    public void start(Stage primaryStage) throws IOException, ExecutionException, InterruptedException {
        MainFX.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Javafx/BackOffice/BackSidebar.fxml")));
        //FXMLLoader loader=new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/Command/Market.fxml"));
        //FXMLLoader loader=new FXMLLoader(getClass().getResource("/Javafx/BackOffice/BackSidebar.fxml"));
        //FXMLLoader loader=new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/Command/Cart.fxml"));
        primaryStage.setTitle("Prosync");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}