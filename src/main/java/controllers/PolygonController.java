package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import models.StockQuote;
import services.PolygonApiService;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PolygonController implements Initializable {

    @FXML
    private ListView<StockQuote> listView;
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
    @FXML
    private AnchorPane listviewanchorepane;
    @FXML
    private Pagination pagination;
    @FXML
    private BorderPane borderPane;
    private StockQuote stockquote;

    private String selectedCurrency = "USD"; // Default currency
    private final PolygonApiService polygonApiService;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final int ITEMS_PER_PAGE = 10;


    public PolygonController() {
        this.polygonApiService = new PolygonApiService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCurrencyMenu();
        fetchAndPopulateData();
        setupPagination();

        convertToCurrency(selectedCurrency);

        // Set custom cell factory for the ListView
        CustomCellFactoryFront cellFactory = new CustomCellFactoryFront(borderPane);
        listView.setCellFactory(cellFactory);
    }

    private void initializeCurrencyMenu() {
        MenuItem usdItem = createCurrencyMenuItem("USD");
        MenuItem eurItem = createCurrencyMenuItem("EUR");
        MenuItem gbpItem = createCurrencyMenuItem("GBP");

        currencyButton.getItems().addAll(usdItem, eurItem, gbpItem);
        currencyButton.setText(selectedCurrency);
    }
    private void setupPagination() {
        pagination.setPageFactory(this::createPage);
    }

    private void fetchAndPopulateData() {
        Task<ObservableList<StockQuote>> fetchTableDataTask = new Task<>() {
            @Override
            protected ObservableList<StockQuote> call() throws Exception {
                List<StockQuote> stockQuotes = polygonApiService.fetchStockQuotes();
                return FXCollections.observableArrayList(stockQuotes);
            }
        };

        fetchTableDataTask.setOnSucceeded(event -> {
            ObservableList<StockQuote> stockQuotes = fetchTableDataTask.getValue();
            listView.setItems(stockQuotes);
            pagination.setPageCount((int) Math.ceil((double) stockQuotes.size() / ITEMS_PER_PAGE));
        });

        new Thread(fetchTableDataTask).start();
    }




    private MenuItem createCurrencyMenuItem(String currency) {
        MenuItem menuItem = new MenuItem(currency);
        menuItem.setOnAction(event -> handleCurrencySelection(currency));
        return menuItem;
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
        Platform.runLater(() -> displayStockCards(famousStocks));
        return famousStocks;
    }

    private void handleCurrencySelection(String currency) {
        if (!selectedCurrency.equals(currency)) {
            convertToCurrency(currency);
            selectedCurrency = currency;
            currencyButton.setText(currency);
        }
    }

    private void convertToCurrency(String currency) {
        double conversionRate = getConversionRate(currency) / getConversionRate(selectedCurrency);
        for (StockQuote stock : listView.getItems()) {
            convertStockQuote(stock, conversionRate);
        }
        listView.refresh();
        selectedCurrency = currency; // Update the selected currency
        currencyButton.setText(currency); // Update the currency button text
        updateCardCurrencies(); // Update the currency labels on the cards
        convertAllCardsToSelectedCurrency(); // Reapply conversion to all cards
    }

    private void updateCardCurrencies() {
        List<StockQuote> stocks = listView.getItems();
        for (int i = 0; i < stocks.size(); i++) {
            StockQuote stock = stocks.get(i);
            AnchorPane cardPane = getCardPane(i + 1);
            if (cardPane != null && stock != null) {
                updateCardCurrency(cardPane, selectedCurrency);
            }
        }
    }

    private void updateCardCurrency(AnchorPane card, String currency) {
        if (card == null) return;

        Label currencyLabel = (Label) card.lookup("#curency");
        currencyLabel.setText("Currency: " + currency);
    }

    private double getConversionRate(String currency) {
        switch (currency) {
            case "EUR":
                return 0.85; // 1 USD = 0.85 EUR
            case "GBP":
                return 0.73; // 1 USD = 0.73 GBP
            default:
                return 1.0; // Default currency is USD, so no conversion needed
        }
    }

    private void convertStockQuote(StockQuote stock, double conversionRate) {
        stock.setOpen(stock.getOpen() * conversionRate);
        stock.setHigh(stock.getHigh() * conversionRate);
        stock.setLow(stock.getLow() * conversionRate);
        stock.setClose(stock.getClose() * conversionRate);
    }

    private void convertAllCardsToSelectedCurrency() {
        List<StockQuote> stocks = listView.getItems();
        for (int i = 0; i < stocks.size(); i++) {
            StockQuote stock = stocks.get(i);
            AnchorPane cardPane = getCardPane(i + 1);
            if (cardPane != null && stock != null) {
                convertCardToSelectedCurrency(cardPane, stock);
            }
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

    private void convertCardToSelectedCurrency(AnchorPane card, StockQuote stock) {
        if (card == null) return;

        Label nameLabel = (Label) card.lookup("#invname");
        nameLabel.setText(stock.getName());

        Label currencyLabel = (Label) card.lookup("#curency");
        currencyLabel.setText(" " + selectedCurrency);

        double convertedAmount = stock.getOpen(); // Amount already converted in TableView
        Label amountLabel = (Label) card.lookup("#amount");
        amountLabel.setText(" " + decimalFormat.format(convertedAmount));

        Label changeRateLabel = (Label) card.lookup("#chgrate");
        double open = stock.getOpen();
        double close = stock.getClose();
        double changeRate = ((close - open) / open) * 100.0;
        changeRateLabel.setText(String.format("%.2f%%", changeRate));
    }

    private void displayStockCards(List<StockQuote> famousStocks) {
        for (int i = 0; i < famousStocks.size(); i++) {
            StockQuote stock = famousStocks.get(i);
            AnchorPane cardPane = getCardPane(i + 1);
            populateStockCard(cardPane, stock);
        }
    }

    private void populateStockCard(AnchorPane card, StockQuote stock) {
        if (card == null) return;

        Label nameLabel = (Label) card.lookup("#invname");
        nameLabel.setText(stock.getName());

        Label currencyLabel = (Label) card.lookup("#curency");
        currencyLabel.setText(" " + selectedCurrency);

        Label amountLabel = (Label) card.lookup("#amount");
        amountLabel.setText("" + stock.getOpen());

        Label changeRateLabel = (Label) card.lookup("#chgrate");
        double changeRate = ((stock.getClose() - stock.getOpen()) / stock.getOpen()) * 100.0;
        changeRateLabel.setText(String.format("%.2f%%", changeRate));
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, listView.getItems().size());

        ListView<StockQuote> pageListView = new ListView<>();
        pageListView.setItems(FXCollections.observableArrayList(listView.getItems().subList(fromIndex, toIndex)));

        // Set custom cell factory for the page's ListView
        CustomCellFactoryFront cellFactory = new CustomCellFactoryFront(borderPane);
        pageListView.setCellFactory(cellFactory);

        return pageListView;
    }

}
