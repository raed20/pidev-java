package services;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.StockQuote;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PolygonApiService extends Application {

    private static final String API_KEY = "hE35s2CmZd4Q0EHSF5pjm3BdDXsmBkAx";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void start(Stage primaryStage) {
        // Method to fetch stock quotes data
        fetchStockQuotes();
    }

    public ObservableList<StockQuote> fetchStockQuotes() {
        ObservableList<StockQuote> stockQuotes = FXCollections.observableArrayList();
        String url = "https://api.polygon.io/v2/aggs/grouped/locale/us/market/stocks/2023-01-09?apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Successful response
                String responseBody = response.body();
                // Parse JSON response and populate stockQuotes list with StockQuote objects
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray results = jsonResponse.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    // Extract data from JSON and create StockQuote object
                    double open = result.getDouble("o");
                    double high = result.getDouble("h");
                    double low = result.getDouble("l");
                    double close = result.getDouble("c");
                    long volume = result.getLong("v");
                    StockQuote stockQuote = new StockQuote( open, high, low, close, volume);
                    // Add quote to stockQuotes list
                    stockQuotes.add(stockQuote);
                }
            } else {
                // Handle non-200 response status
                showErrorAlert("Error", "HTTP Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Handle request or response exception
            showErrorAlert("Error", "Failed to fetch data: " + e.getMessage());
        }

        return stockQuotes;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}