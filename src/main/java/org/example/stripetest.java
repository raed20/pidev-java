package org.example;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class stripetest  extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/InvestismentFront/payment.fxml"));
        Parent root = loader.load();

        // Create the scene
        Scene scene = new Scene(root);

        // Set the scene on the stage
        primaryStage.setScene(scene);

        // Set the title of the stage
        primaryStage.setTitle("Your Application");

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
