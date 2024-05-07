package controllers;

import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.renderer.ErrorStyle;
import io.fair_acc.chartfx.renderer.spi.ErrorDataSetRenderer;
import io.fair_acc.chartfx.renderer.spi.financial.CandleStickRenderer;
import io.fair_acc.chartfx.renderer.spi.financial.HighLowRenderer;
import io.fair_acc.dataset.spi.financial.OhlcvDataSet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import services.PolygonApiService;

import java.time.LocalDate;
import java.util.List;

public class InvestissementFrontController {

    @FXML
    private AnchorPane rootPane;

    private XYChart chart;
    private final PolygonApiService polygonApiService;

    public InvestissementFrontController() {
        this.polygonApiService = new PolygonApiService();
    }

    @FXML
    public void initialize() {
        // Initialize the chart
        chart = new XYChart();
        chart.setPrefWidth(1000);
        chart.setPrefHeight(1000);
        AnchorPane.setTopAnchor(chart, (rootPane.getPrefHeight() - chart.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(chart, (rootPane.getPrefWidth() - chart.getPrefWidth()) / 2);

        rootPane.getChildren().add(chart);
    }

    public void setSelectedName(String selectedName) {
        LocalDate startDate = LocalDate.now().minusDays(100);
        LocalDate endDate = LocalDate.now();
        List<OhlcvDataSet> ohlcvDataSets = polygonApiService.getStockData(selectedName, startDate, endDate);
        Platform.runLater(() -> initializeChart(ohlcvDataSets));
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
