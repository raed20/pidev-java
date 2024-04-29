package controllers;

import entities.Investissement;
import interfaces.IInvestissement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import services.InvestissementService;
import tools.MyConnection;

import java.io.IOException;
import java.util.List;

public class InvestissementBackController {

    @FXML
    private TableView<Investissement> investmentTable;

    @FXML
    private TableColumn<Investissement, Integer> idColumn;

    @FXML
    private TableColumn<Investissement, Long> montantColumn;

    @FXML
    private TableColumn<Investissement, java.util.Date> dateColumn;

    @FXML
    private TableColumn<Investissement, Float> totalValueColumn;

    @FXML
    private TableColumn<Investissement, String> stockNameColumn;

    @FXML
    private TableColumn<Investissement, Float> changerateColumn;

    @FXML
    private TableColumn<Investissement, Float> priceColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;
    @FXML
    private AnchorPane anchorPane;

    private IInvestissement investissementService;

    public InvestissementBackController() {
        MyConnection connection = new MyConnection();
        this.investissementService = new InvestissementService(connection);
    }

    @FXML
    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateInvest"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        stockNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        changerateColumn.setCellValueFactory(new PropertyValueFactory<>("changerate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        addButton.setOnMouseClicked(event -> loadPage("/Javafx/BackOffice/Investissement/investissementaddback.fxml"));

        // Load data into TableView
        loadInvestissements();
    }

    private void loadInvestissements() {
        List<Investissement> investissements = investissementService.getAllInvestissements();
        ObservableList<Investissement> observableInvestissements = FXCollections.observableArrayList(investissements);
        investmentTable.setItems(observableInvestissements);
    }
    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateInvestissement() {
        Investissement selectedInvestissement = investmentTable.getSelectionModel().getSelectedItem();
        if (selectedInvestissement != null) {
            loadPage("/path/to/updateInvestissement.fxml");
            // You can pass the selectedInvestissement to the updateInvestissement controller
            // and pre-fill the form fields with its data
        } else {
            // Show an error message indicating that no investment is selected
        }
    }

    @FXML
    private void deleteInvestissement() {
        Investissement selectedInvestissement = investmentTable.getSelectionModel().getSelectedItem();
        if (selectedInvestissement != null) {
            investissementService.deleteInvestissement(selectedInvestissement.getId());
            investmentTable.getItems().remove(selectedInvestissement);
        } else {
            // Show an error message indicating that no investment is selected
        }
    }
}
