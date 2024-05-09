package controllers;
import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.renderer.ErrorStyle;
import io.fair_acc.chartfx.renderer.spi.ErrorDataSetRenderer;
import io.fair_acc.chartfx.renderer.spi.financial.CandleStickRenderer;
import io.fair_acc.chartfx.renderer.spi.financial.HighLowRenderer;
import io.fair_acc.dataset.spi.financial.OhlcvDataSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.StockQuote;
import services.PolygonApiService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class InvestissementFrontController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane chartAnchorPane; // Reference to the chart AnchorPane in FXML
    @FXML
    private Label SelectedName;
    @FXML
    private Label currentpriceLabel;
    @FXML
    private Button buyButton;
    private XYChart chart;
    private final PolygonApiService polygonApiService;

    public InvestissementFrontController() {
        this.polygonApiService = new PolygonApiService();
    }

    @FXML
    public void initialize() {
        // Initialize the chart
        chart = new XYChart();
        chart.setPrefWidth(900); // Adjust width to fit chartAnchorPane
        chart.setPrefHeight(800); // Adjust height to fit chartAnchorPane
        chartAnchorPane.getChildren().add(chart); // Add chart to chartAnchorPane
    }

    public void setSelectedName(String selectedName) {
        SelectedName.setText(selectedName);
        LocalDate startDate = LocalDate.now().minusDays(100);
        LocalDate endDate = LocalDate.now();
        StockQuote stockQuaote = polygonApiService.fetchStockQuoteByName(selectedName);
        currentpriceLabel.setText(" " + (stockQuaote.getClose()));
        List<OhlcvDataSet> ohlcvDataSets = polygonApiService.getStockData(selectedName, startDate, endDate);
        initializeChart(ohlcvDataSets);
        buyButton.setOnMouseClicked(event -> handleLabelClick(event, selectedName));

    }
    private void handleLabelClick(MouseEvent event, String selectedName) {
        navigateToPaymentFront(selectedName);
    }
    private void navigateToPaymentFront(String selectedName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/investissement/payment.fxml"));
            AnchorPane PaymentController = loader.load();
            PaymentController controller = loader.getController();
            controller.setSelectedName(selectedName);
            rootPane.getChildren().setAll(PaymentController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeChart(List<OhlcvDataSet> ohlcvDataSets) {
        if (ohlcvDataSets == null || ohlcvDataSets.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        chart.getRenderers().clear();

        HighLowRenderer highLowRenderer = new HighLowRenderer();
        ErrorDataSetRenderer avgRenderer = new ErrorDataSetRenderer();
        CandleStickRenderer candleStickRenderer = new CandleStickRenderer();
        avgRenderer.setDrawMarker(false);
        avgRenderer.setErrorStyle(ErrorStyle.NONE);
        highLowRenderer.getDatasets().addAll(ohlcvDataSets);
        candleStickRenderer.getDatasets().addAll(ohlcvDataSets);

        chart.getRenderers().add(highLowRenderer);
        chart.getRenderers().add(avgRenderer);
    }
}
