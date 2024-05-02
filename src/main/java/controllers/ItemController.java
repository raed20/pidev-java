package controllers;

import entities.Product;
import interfaces.MyListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

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

    private Product product;
    private MyListener myListener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(product);
    }
}
