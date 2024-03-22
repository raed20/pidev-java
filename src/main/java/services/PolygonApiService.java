package services;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PolygonApiService extends Application {

    private static final String API_KEY = "hE35s2CmZd4Q0EHSF5pjm3BdDXsmBkAx";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void start(Stage primaryStage) {
        // Method to get stock quote data
        getStockQuote();
    }

    private void getStockQuote() {
        String url = "https://api.polygon.io/v2/aggs/grouped/locale/us/market/stocks/2023-01-09?apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Successful response
                String responseBody = response.body();
                System.out.println(responseBody); // Handle the response data
            } else {
                // Handle non-200 response status
                showErrorAlert("Error", "HTTP Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Handle request or response exception
            showErrorAlert("Error", "Failed to fetch data: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
