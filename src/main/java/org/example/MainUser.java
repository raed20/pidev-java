package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainUser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FrontOffice.Login/login.fxml"));
        primaryStage.setTitle("dhg");
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();
    }

    public  static void main(String[] args) {
        launch(args);
    }
}
/*
package org.example;

        import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

        import java.io.IOException;

public class MainUser extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/application/FRONT/Home.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(MainUser.class.getResource("/FrontOffice/Login/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}*/
