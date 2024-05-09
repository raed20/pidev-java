package controllers;
import controllers.CustomCellWalletController;
import entities.Investissement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import services.InvestissementService;
import tools.MyConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class WalletController {
    @FXML
    private ListView<Investissement> investissementList;
    @FXML
    private AnchorPane listviewPane;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pagination pagination;
    private ObservableList<Investissement> allInvestissement;

    private final InvestissementService investissmentService;
    private static final int ITEMS_PER_PAGE = 6;

    public WalletController() {
        MyConnection connection = new MyConnection();
        this.investissmentService = new InvestissementService(connection);

    }

    @FXML
    public void initialize() throws SQLException {
        // Set custom cell factory for the ListView
        investissementList.setCellFactory(new CustomCellWalletController(investissmentService, anchorPane));

        allInvestissement = FXCollections.observableArrayList();
        loadInvestissement();

        // Set up pagination
        pagination.setPageCount(calculatePageCount());
        pagination.setPageFactory(this::createPage);

    }
    private void loadInvestissement() throws SQLException {
        List<Investissement> investissements = investissmentService.getAllInvestissements();
        allInvestissement.addAll(investissements);
    }
    private void loadPage(String fxmlPath) {
        // Load a new FXML page into the anchor pane
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }
    private int calculatePageCount() {
        int pageCount = allInvestissement.size() / ITEMS_PER_PAGE;
        if (allInvestissement.size() % ITEMS_PER_PAGE != 0) {
            pageCount++;
        }
        return pageCount;
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allInvestissement.size());

        ListView<Investissement> pageListView = new ListView<>();
        pageListView.setItems(FXCollections.observableArrayList(allInvestissement.subList(fromIndex, toIndex)));
        pageListView.setCellFactory(investissementList.getCellFactory());

        // Replace the existing ListView with the new ListView
        listviewPane.getChildren().setAll(pageListView);

        return pageListView;
    }
}
