package controllers;

import entities.Product;
import interfaces.MyListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import services.ProductService;
import tools.MyConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MarketController implements Initializable {

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
                ItemController cardC = load.getController();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            menuGetData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        menuDisplayCard();

    }



//    public MarketController() {
//        MyConnection connection = new MyConnection();
//        productService = new ProductService(connection);
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        List<Product> products = productService.getAll();
//        int column=0;
//        int row=0;
//        try {
//            for (int i =0;i<products.size();i++){
//                FXMLLoader fxmlLoader=new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("Javafx/FrontOffice/Command/Item.fxml"));
//                AnchorPane anchorPane=fxmlLoader.load();
//
//                ItemController itemController=fxmlLoader.getController();
//                itemController.setData(products.get(i));
//
//                if(column==3){
//                    column=0;
//                    row++;
//                }
//                grid.add(anchorPane,column++,row);
//                GridPane.setMargin(anchorPane,new Insets(10));
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//    }
}
