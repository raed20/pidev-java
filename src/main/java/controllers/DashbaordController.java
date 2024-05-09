package controllers;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import services.*;
import tools.MyConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashbaordController {
    private final InvestissementService investissmentService;


    @FXML
    private ListView<Utilisateurs> customerListeView;

    @FXML
    private ListView<Product> CRMListView;

    @FXML
    private AnchorPane customerAnchorPane;

    @FXML
    private AnchorPane reclamationAnchorPane;

    private ObservableList<Utilisateurs> allUtilisateurs;
    private ObservableList<Product> allProducts;

    private final UtilisateurService utilisateurService;
    private final ProductService productService;
    @FXML
    private Label totalLabel;
    @FXML
    private Label productLabel;
    @FXML
    private Label loanLabel;
    @FXML
    private Label viewLabel;

private LoanService loanService;
private BlogService blogservice;
    public DashbaordController() {
        MyConnection connection = new MyConnection();
        this.utilisateurService = new UtilisateurService();
        this.productService = new ProductService(connection);
        this.investissmentService = new InvestissementService(connection);
        this.loanService = new LoanService();
        this.blogservice = new BlogService(connection);


    }

    @FXML
    public void initialize() throws SQLException {
        // Set custom cell factories for the ListViews

        customerListeView.setCellFactory(param -> new cellfactorydashsmall(utilisateurService, customerAnchorPane).call(param));
        CRMListView.setCellFactory(param -> new cellfactorydashbig(productService, reclamationAnchorPane).call(param));

        // Initialize ObservableLists
        allUtilisateurs = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();

        // Load data into ObservableLists
        try {
            loadUsers();
            loadReclamations();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Investissement> investissements = investissmentService.getAllInvestissements();
        long totalValue = 0;
        for (Investissement investissement : investissements) {
            totalValue += investissement.getTotalValue();
        }
        totalLabel.setText(String.valueOf(totalValue));
        List<Product> products = productService.getAll();
        long numberofproducts = 0;
        for (Product product : products) {
            numberofproducts++;
        }
        productLabel.setText(String.valueOf(numberofproducts));
        List<Pret> prets;
        try {
            prets = loanService.getDataLoan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long numberofloans = 0;
        for (Pret pret : prets) {
            numberofloans++;
        }
        loanLabel.setText(String.valueOf(numberofloans));
        List<Blog> blogs = blogservice.getAllBlog();
        long nblog = 0;
        for (Blog blog : blogs) {
            totalValue += blog.getVu();
        }
        viewLabel.setText(String.valueOf(numberofloans));


    }

    private void loadUsers() throws SQLException {
        List<Utilisateurs> users = utilisateurService.getData();
        allUtilisateurs.addAll(users);
        customerListeView.setItems(allUtilisateurs);
    }

    private void loadReclamations() throws SQLException {
        List<Product> reclamations = productService.getAll();
        allProducts.addAll(reclamations);
        CRMListView.setItems(allProducts);
    }
}