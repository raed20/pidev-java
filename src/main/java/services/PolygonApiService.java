package services;

import io.fair_acc.dataset.spi.financial.OhlcvDataSet;
import io.fair_acc.dataset.spi.financial.api.attrs.AttributeModel;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcv;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcvItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.StockQuote;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

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
                    int day = i;
                    String name = result.getString("T");
                    double open = result.getDouble("o");
                    double high = result.getDouble("h");
                    double low = result.getDouble("l");
                    double close = result.getDouble("c");
                    long volume = result.getLong("v");
                    StockQuote stockQuote = new StockQuote(name, day, open, high, low, close, volume);
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

    public OhlcvDataSet fetchStockQuoteByNames(String stockName, LocalDate now) {
        // Fetch stock data by name
        StockQuote stockQuote = fetchStockQuoteByName(stockName);
        // Convert stock quote to OhlcvDataSet object
        return convertToDataSet(stockQuote);
    }

    private OhlcvDataSet fetchStockQuoteBySymbols(String symbol, LocalDate date) {
        // Fetch stock data by symbol
        StockQuote stockQuote = fetchStockQuoteBySymbol(symbol);
        // Convert stock quote to OhlcvDataSet object
        return convertToDataSet(stockQuote);
    }

    private OhlcvDataSet convertToDataSet(StockQuote stockQuote) {
        // Create an OhlcvDataSet object to hold the data
        OhlcvDataSet ohlcvDataSet = new OhlcvDataSet(stockQuote.getName()); // Use stock name as title

        // Set the OHLCV data
        ohlcvDataSet.setData(new IOhlcv() {
            @NotNull
            @Override
            public Iterator<IOhlcvItem> iterator() {
                return null;
            }

            @Override
            public int size() {
                return 1; // Assuming only one data point
            }

            @Override
            public AttributeModel getAddon() {
                return null;
            }

            @Override
            public AttributeModel getAddonOrCreate() {
                return null;
            }

            @Override
            public IOhlcvItem getOhlcvItem(int index) {
                return new IOhlcvItem() {
                    @Override
                    public Date getTimeStamp() {
                        // Use the current date as the timestamp
                        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }

                    @Override
                    public double getOpen() {
                        return stockQuote.getOpen();
                    }

                    @Override
                    public double getHigh() {
                        return stockQuote.getHigh();
                    }

                    @Override
                    public double getLow() {
                        return stockQuote.getLow();
                    }

                    @Override
                    public double getClose() {
                        return stockQuote.getClose();
                    }

                    @Override
                    public double getVolume() {
                        return stockQuote.getVolume();
                    }

                    @Override
                    public double getOpenInterest() {
                        return 0;
                    }

                    @Override
                    public AttributeModel getAddon() {
                        return null;
                    }

                    @Override
                    public AttributeModel getAddonOrCreate() {
                        return null;
                    }
                };
            }
        });

        return ohlcvDataSet;
    }


    public StockQuote fetchStockQuoteByName(String stockName) {
        StockQuote stockQuote = null;
        String url = "https://api.polygon.io/v1/meta/symbols/" + stockName + "/company?apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Successful response
                String responseBody = response.body();
                // Parse JSON response and get symbol
                JSONObject jsonResponse = new JSONObject(responseBody);
                String symbol = jsonResponse.getString("symbol");
                // Fetch stock quote using symbol
                stockQuote = fetchStockQuoteBySymbol(symbol);
            } else {
                // Handle non-200 response status
                showErrorAlert("Error", "HTTP Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Handle request or response exception
            showErrorAlert("Error", "Failed to fetch data: " + e.getMessage());
        }

        return stockQuote;
    }

    private StockQuote fetchStockQuoteBySymbol(String symbol) {
        StockQuote stockQuote = null;
        String url = "https://api.polygon.io/v2/aggs/ticker/" + symbol + "/prev?apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Successful response
                String responseBody = response.body();
                // Parse JSON response and create StockQuote object
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray results = jsonResponse.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    int day = 0;
                    double open = result.getDouble("o");
                    double high = result.getDouble("h");
                    double low = result.getDouble("l");
                    double close = result.getDouble("c");
                    long volume = result.getLong("v");
                    stockQuote = new StockQuote(symbol, day, open, high, low, close, volume);
                } else {
                    // Handle case where response array is empty
                    showErrorAlert("Error", "No data found for symbol: " + symbol);
                }
            } else {
                // Handle non-200 response status
                showErrorAlert("Error", "HTTP Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Handle request or response exception
            showErrorAlert("Error", "Failed to fetch data: " + e.getMessage());
        }

        return stockQuote;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public ObservableList<OhlcvDataSet> getStockData(String id, LocalDate startDate, LocalDate endDate) {
        ObservableList<OhlcvDataSet> ohlcvDataSets = FXCollections.observableArrayList();

        // Format start and end dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        String url = "https://api.polygon.io/v2/aggs/ticker/" + id + "/range/1/day/" +
                startDateStr + "/" + endDateStr + "?apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Successful response
                String responseBody = response.body();
                // Parse JSON response and populate ohlcvDataSets list with OhlcvDataSet objects
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray results = jsonResponse.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    // Extract data from JSON
                    double open = result.getDouble("o");
                    double high = result.getDouble("h");
                    double low = result.getDouble("l");
                    double close = result.getDouble("c");
                    long volume = result.getLong("v");
                    String stockName = id; // Assuming "T" is the key for stock name in JSON response

                    // Generate X value (date) for the current data point
                    LocalDate date = startDate.plusDays(i);

                    // Create OhlcvDataSet object for the current data point with the stock name as the title
                    OhlcvDataSet ohlcvDataSet = new OhlcvDataSet(stockName);
                    ohlcvDataSet.setData(new IOhlcv() {
                        @NotNull
                        @Override
                        public Iterator<IOhlcvItem> iterator() {
                            return null; // Not implementing iterator for simplicity
                        }

                        @Override
                        public int size() {
                            return 1; // Assuming only one data point
                        }

                        @Override
                        public AttributeModel getAddon() {
                            return null; // Not implementing add-ons for simplicity
                        }

                        @Override
                        public AttributeModel getAddonOrCreate() {
                            return null; // Not implementing add-ons for simplicity
                        }

                        @Override
                        public IOhlcvItem getOhlcvItem(int index) {
                            return new IOhlcvItem() {
                                @Override
                                public Date getTimeStamp() {
                                    // Convert LocalDate to Date
                                    return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                }

                                @Override
                                public double getOpen() {
                                    return open;
                                }

                                @Override
                                public double getHigh() {
                                    return high;
                                }

                                @Override
                                public double getLow() {
                                    return low;
                                }

                                @Override
                                public double getClose() {
                                    return close;
                                }

                                @Override
                                public double getVolume() {
                                    return volume;
                                }

                                @Override
                                public double getOpenInterest() {
                                    return 0;
                                }

                                @Override
                                public AttributeModel getAddon() {
                                    return null; // Not implementing add-ons for simplicity
                                }

                                @Override
                                public AttributeModel getAddonOrCreate() {
                                    return null; // Not implementing add-ons for simplicity
                                }
                            };
                        }
                    });

                    // Add OhlcvDataSet to the list
                    ohlcvDataSets.add(ohlcvDataSet);
                }
            } else {
                // Handle non-200 response status
                showErrorAlert("Error", "HTTP Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Handle request or response exception
            showErrorAlert("Error", "Failed to fetch data: " + e.getMessage());
        }

        return ohlcvDataSets;
    }


}
