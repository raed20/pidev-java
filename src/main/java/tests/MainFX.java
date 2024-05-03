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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Javafx/FrontOffice/FrontSidebar.fxml")));
        primaryStage.setTitle("Loan App");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
        /*WebView webView = new WebView();
        webView.getEngine().load("https://app.powerbi.com/groups/900809af-865b-4244-a487-4110c5e5926c/reports/d4ef8903-50e7-4839-9501-0ca2e4a9bad0/ReportSection?experience=power-bi");

        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Web View Example");
        primaryStage.setScene(scene);
        primaryStage.show();*/
        // Définissez les paramètres de l'authentification
      /*


        ExecutorService service = Executors.newFixedThreadPool(1);
        try {
            // Authenticate and obtain an embed token
            AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
            ClientCredential credential = new ClientCredential(CLIENT_ID, CLIENT_SECRET);
            Future<AuthenticationResult> future = context.acquireToken("https://analysis.windows.net/powerbi/api", credential, null);
            AuthenticationResult result = future.get();
            String embedToken = result.getAccessToken();
            System.out.println(embedToken);

            // Load the Power BI dashboard in the WebView using the embed token
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            String embedUrl = "https://app.powerbi.com/reportEmbed?reportId=" + REPORT_ID + "&autoAuth=true&groupId=" + GROUP_ID;
            String htmlContent = "<html><body><iframe src=\"" + embedUrl + "&token=" + embedToken + "\" width=\"1140\" height=\"541.25\" frameborder=\"0\" allowFullScreen=\"true\"></iframe></body></html>";
            webEngine.loadContent(htmlContent);

            // Create the scene and show it
            StackPane root = new StackPane();
            root.getChildren().add(webView);
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Power BI Embedded");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }*/
    }

        public static void main(String[] args) {
        launch(args);
    }
}
