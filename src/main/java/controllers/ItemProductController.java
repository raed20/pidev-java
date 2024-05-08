package controllers;

import entities.Panier;
import entities.Product;
import interfaces.MyListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import services.PanierService;
import tools.MyConnection;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ItemProductController implements Initializable {

    @FXML
    private Button addProd;

    @FXML
    private AnchorPane cardForm;

    @FXML
    private ImageView imgProd;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label salePriceLabel;

    @FXML
    private Label discountLabel;

    @FXML
    private Spinner<Integer> qtyLabel;

    private SpinnerValueFactory<Integer> spin;
    private Product product;
    private MyListener myListener;

    private PanierService panierService;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQunatity();
    }

    void setData(Product product,MyListener myListener) {
        this.product = product;
        this.myListener=myListener;
        nameLabel.setText(product.getName());
        priceLabel.setText(String.valueOf(product.getPrice()+" TND"));
        salePriceLabel.setText(String.valueOf(product.getPrice() - ((product.getPrice() * product.getDiscount()) / 100))+" TND");
        discountLabel.setText(String.valueOf("-"+product.getDiscount()+ "%"));
        // Load and set the image
        if (product.getImage() != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/Images/" + product.getImage()));
                imgProd.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle image loading failure
            }
        }
    }
    private int qty;
    public void setQunatity(){
        spin=new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,0);
        qtyLabel.setValueFactory(spin);
    }
    public ItemProductController(){
        this.panierService=new PanierService(new MyConnection());
    }



    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(product);
    }

    public void addToCart(javafx.event.ActionEvent actionEvent) {
        int qty = qtyLabel.getValue();
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

}
