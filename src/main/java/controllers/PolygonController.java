package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.StockQuote;
import services.PolygonApiService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PolygonController implements Initializable {

    @FXML
    private TableView<StockQuote> tableView;
    @FXML
    private TableColumn<StockQuote, Double> openColumn;
    @FXML
    private TableColumn<StockQuote, Double> highColumn;
    @FXML
    private TableColumn<StockQuote, Double> lowColumn;
    @FXML
    private TableColumn<StockQuote, Double> closeColumn;
    @FXML
    private TableColumn<StockQuote, Long> volumeColumn;
    @FXML
    private AnchorPane cardPane1;
    @FXML
    private AnchorPane cardPane2;
    @FXML
    private AnchorPane cardPane3;
    @FXML
    private AnchorPane cardPane4;
    @FXML
    private Text resultText;
    @FXML
    private MenuButton currencyButton;

    private String selectedCurrency = "USD"; // Default currency

    private final PolygonApiService polygonApiService;

    public PolygonController() {
        this.polygonApiService = new PolygonApiService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add menu items for different currencies
        MenuItem usdItem = new MenuItem("USD");
        MenuItem eurItem = new MenuItem("EUR");
        MenuItem gbpItem = new MenuItem("GBP");

        // Add event handlers for menu items
        usdItem.setOnAction(event -> handleCurrencySelection("USD"));
        eurItem.setOnAction(event -> handleCurrencySelection("EUR"));
        gbpItem.setOnAction(event -> handleCurrencySelection("GBP"));

        // Add menu items to the menu button
        currencyButton.getItems().addAll(usdItem, eurItem, gbpItem);
        resultText = new Text();

        // Set cell value factories for each column
        openColumn.setCellValueFactory(new PropertyValueFactory<>("open"));
        highColumn.setCellValueFactory(new PropertyValueFactory<>("high"));
        lowColumn.setCellValueFactory(new PropertyValueFactory<>("low"));
        closeColumn.setCellValueFactory(new PropertyValueFactory<>("close"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));

        // Fetch data for specific stocks and populate the table
        Task<List<StockQuote>> fetchTableDataTask = new Task<>() {
            @Override
            protected List<StockQuote> call() throws Exception {
                tableView.setItems(polygonApiService.fetchStockQuotes());

                return fetchFamousStocks();
            }
        };


        new Thread(fetchTableDataTask).start();
    }
    private List<StockQuote> fetchFamousStocks() {
        List<String> famousStocksSymbols = List.of("TSLA", "AAPL", "GOOGL", "MSFT"); // Add more as needed

        List<StockQuote> famousStocks = new ArrayList<>();
        for (String symbol : famousStocksSymbols) {
            StockQuote stock = polygonApiService.fetchStockQuoteByName(symbol);
            if (stock != null) {
                famousStocks.add(stock);
            }
        }

        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> {
            displayStockCards(famousStocks);
        });

        return famousStocks;
    }
//cinvert currency functions
private void handleCurrencySelection(String currency) {
    selectedCurrency = currency;
    currencyButton.setText(currency);
    // Call a method to convert values to the selected currency
    convertTableToSelectedCurrency();
    convertAllCardsToSelectedCurrency();
}
    private double getConversionRate(String currency) {
        // Implement the logic to retrieve the conversion rate for the selected currency
        // You can use a web service, an API, or any other method to get the conversion rate
        // For demonstration purposes, let's assume a simple conversion rate based on the selected currency
        switch (currency) {
            case "EUR":
                return 0.85; // 1 USD = 0.85 EUR
            case "GBP":
                return 0.73; // 1 USD = 0.73 GBP
            default:
                return 1.0; // Default currency is USD, so no conversion needed
        }
    }
    private double convertToSelectedCurrency(double value) {
        // Get the conversion rate for the selected currency
        double conversionRate = getConversionRate(selectedCurrency);

        // Convert the value to the selected currency
        return value * conversionRate;
    }

    private void convertTableToSelectedCurrency() {
        // Iterate through the TableView items and update the values based on the selected currency
        for (StockQuote stock : tableView.getItems()) {
            // Convert the values for each stock
            stock.setOpen(convertToSelectedCurrency(stock.getOpen()));
            stock.setHigh(convertToSelectedCurrency(stock.getHigh()));
            stock.setLow(convertToSelectedCurrency(stock.getLow()));
            stock.setClose(convertToSelectedCurrency(stock.getClose()));
        }

        // Refresh the TableView to reflect the changes
        tableView.refresh();
    }

    private void convertCardToSelectedCurrency(AnchorPane card, StockQuote stock) {
        if (card == null) return;

        // Get the labels for the card
        Label nameLabel = (Label) card.lookup("#invname");
        Label currencyLabel = (Label) card.lookup("#curency");
        Label amountLabel = (Label) card.lookup("#amount");
        Label changeRateLabel = (Label) card.lookup("#chgrate");

        // Convert the amount value to the selected currency
        double convertedAmount = convertToSelectedCurrency(stock.getOpen());

        // Update the labels with converted values
        nameLabel.setText(stock.getName());
        currencyLabel.setText("Currency: " + selectedCurrency);
        amountLabel.setText("Amount: " + convertedAmount);

        // Calculate and update the change rate
        double open = stock.getOpen();
        double close = stock.getClose();
        double changeRate = ((close - open) / open) * 100.0;
        changeRateLabel.setText(String.format("Change Rate: %.2f%%", changeRate));
    }

    private void convertAllCardsToSelectedCurrency() {
        // Iterate through the TableView items and update the card values based on the selected currency
        List<StockQuote> stocks = tableView.getItems();
        for (int i = 0; i < stocks.size(); i++) {
            StockQuote stock = stocks.get(i);
            AnchorPane cardPane = getCardPane(i + 1);
            convertCardToSelectedCurrency(cardPane, stock);
        }
    }



    private void displayStockCards(List<StockQuote> famousStocks) {
        for (int i = 0; i < famousStocks.size(); i++) {
            StockQuote stock = famousStocks.get(i);
            AnchorPane cardPane = getCardPane(i + 1);
            populateStockCard(cardPane, stock);
        }
    }

    private AnchorPane getCardPane(int index) {
        switch (index) {
            case 1:
                return cardPane1;
            case 2:
                return cardPane2;
            case 3:
                return cardPane3;
            case 4:
                return cardPane4;
            default:
                return null;
        }
    }

    private void populateStockCard(AnchorPane card, StockQuote stock) {
        if (card == null) return;

        Label nameLabel = (Label) card.lookup("#invname");
        nameLabel.setText(stock.getName());

        Label currencyLabel = (Label) card.lookup("#curency");
        currencyLabel.setText("Currency: ");

        Label amountLabel = (Label) card.lookup("#amount");
        amountLabel.setText("Amount: " + stock.getOpen());

        Label changeRateLabel = (Label) card.lookup("#chgrate");
        double changeRate = ((stock.getClose() - stock.getOpen()) / stock.getOpen()) * 100.0;
        changeRateLabel.setText(String.format("Change Rate: %.2f%%", changeRate));
    }
}
