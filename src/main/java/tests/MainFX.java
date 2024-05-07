package tests;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainFX extends Application {
    private static Stage primaryStage;



    @Override
    public void start(Stage primaryStage) throws IOException, ExecutionException, InterruptedException {
        MainFX.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/javafx/FrontOffice/FrontSideBar.fxml")));
        primaryStage.setTitle("Loan App");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

        public static void main(String[] args) {
        launch(args);
    }
}
