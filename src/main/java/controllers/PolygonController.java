package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.concurrent.Task;
import models.StockQuote;
import services.PolygonApiService;

import java.net.URL;
import java.util.ResourceBundle;

public class PolygonController implements Initializable {

    @FXML
    private TableView<StockQuote> tableView;
    @FXML
    private TableColumn<StockQuote, String> dateColumn;
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
    private Text resultText;

    private PolygonApiService polygonApiService;

    public PolygonController() {
        this.polygonApiService = new PolygonApiService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set cell value factories for each column
        openColumn.setCellValueFactory(new PropertyValueFactory<>("open"));
        highColumn.setCellValueFactory(new PropertyValueFactory<>("high"));
        lowColumn.setCellValueFactory(new PropertyValueFactory<>("low"));
        closeColumn.setCellValueFactory(new PropertyValueFactory<>("close"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));

        // Fetch data from Polygon API and populate the table
        Task<Void> fetchDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                tableView.setItems(polygonApiService.fetchStockQuotes());
                return null;
            }
        };

        fetchDataTask.setOnFailed(event -> {
            resultText.setText("Failed to fetch data.");
        });

        new Thread(fetchDataTask).start();
    }
}
