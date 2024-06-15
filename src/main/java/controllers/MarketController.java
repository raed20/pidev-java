package controllers;

import entities.Panier;
import entities.Product;
import interfaces.MyListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import services.PanierService;
import services.ProductService;
import tools.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MarketController implements Initializable {

    @FXML
    private Button addProd;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView cartButton;

    @FXML
    private Label descP;

    @FXML
    private GridPane grid;

    @FXML
    private VBox selectedProd;

    @FXML
    private Label discountP;

    @FXML
    private Label nameProd;

    @FXML
    private ImageView imgP;

    @FXML
    private Spinner<Integer> qtyP;

    @FXML
    private Label saledP;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label unsaledP;
    private SpinnerValueFactory<Integer> spin;
    private Product product;

    private MyListener myListener;
    //private final ProductService productService;

    //private List<Product> products=new ArrayList<>();


    private ObservableList<Product> products = FXCollections.observableArrayList();
    public ObservableList<Product> menuGetData() throws SQLException {
        MyConnection connection = new MyConnection();
        ProductService productService=new ProductService(connection);
        ObservableList<Product> observableList = FXCollections.observableArrayList(productService.getAll());
        return observableList;
    }

    private void setChosenProduct(Product product) {
        this.product = product; // Assigning the product to the class variable
        nameProd.setText(product.getName());
        unsaledP.setText(String.valueOf(product.getPrice()+" TND"));
        discountP.setText(String.valueOf("-"+product.getDiscount()+ "%"));
        saledP.setText(String.valueOf(product.getPrice() - ((product.getPrice() * product.getDiscount()) / 100))+" TND");
        descP.setText(product.getDescription());
        if (product.getImage() != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/Images/" + product.getImage()));
                imgP.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle image loading failure
            }
        }
    }


    public void menuDisplayCard() {
        try {
            if(menuGetData().size()>0) {
                setChosenProduct(menuGetData().get(0));
                myListener = new MyListener() {
                    @Override
                    public void onClickListener(Product product) {
                        setChosenProduct(product);
                    }
                };
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            products.clear();
            products.addAll(menuGetData());

            int row = 0;
            int column = 0;

            grid.getChildren().clear();
            grid.getRowConstraints().clear();
            grid.getColumnConstraints().clear();

            for (int q = 0; q < products.size(); q++) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/Command/Item.fxml"));
                AnchorPane pane = load.load();
                ItemProductController cardC = load.getController();
                cardC.setData(products.get(q),myListener);

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                grid.add(pane, column++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.prefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.prefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int qty;
    public void setQunatity(){
        spin=new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,0);
        qtyP.setValueFactory(spin);
    }

    public void addToCart(javafx.event.ActionEvent actionEvent) {
        int qty = qtyP.getValue();
        if (qty > 0) {
            Panier panier = new Panier();
            Map<Product, Integer> productsMap = new HashMap<>();
            productsMap.put(product, qty);
            panier.setProducts(productsMap);

            PanierService panierService = new PanierService(new MyConnection());
            panierService.add(panier);

            System.out.println("Product added to Cart!");
        } else {
            System.out.println("Please select a quantity.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQunatity();
        try {
            menuGetData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        menuDisplayCard();
        // Set up event handler for searchTextField
        searchTextField.setOnKeyPressed(this::searchProducts);
    }

    public void cartNav(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/Command/Cart.fxml"));
            Parent root = loader.load();

            // Get the controller of the loaded FXML
            CartController cartController = loader.getController();

            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchProducts(KeyEvent keyEvent) {
        try {
            String searchText = searchTextField.getText();
            ObservableList<Product> searchResults = searchProductsByName(searchText);
            displaySearchResults(searchResults);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }

    private ObservableList<Product> searchProductsByName(String searchText) throws SQLException {
        MyConnection connection = new MyConnection();
        ProductService productService = new ProductService(connection);
        return FXCollections.observableArrayList(productService.searchByName(searchText));
    }

    private void displaySearchResults(ObservableList<Product> searchResults) {
        if (searchResults != null && !searchResults.isEmpty()) {
            setChosenProduct(searchResults.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Product product) {
                    setChosenProduct(product);
                }
            };
        } else {
            // Clear the selected product UI elements if no search results found
            clearSelectedProduct();
        }

        try {
            products.clear();
            products.addAll(searchResults);

            // Update the grid with search results
            int row = 0;
            int column = 0;

            grid.getChildren().clear();
            grid.getRowConstraints().clear();
            grid.getColumnConstraints().clear();

            for (int q = 0; q < products.size(); q++) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/Command/Item.fxml"));
                AnchorPane pane = load.load();
                ItemProductController cardC = load.getController();
                cardC.setData(products.get(q), myListener);

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                grid.add(pane, column++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.prefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.prefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSelectedProduct() {
        nameProd.setText("");
        unsaledP.setText("");
        discountP.setText("");
        saledP.setText("");
        descP.setText("");
        imgP.setImage(null);
        addProd.setVisible(false);
        qtyP.setVisible(false);
    }

}
