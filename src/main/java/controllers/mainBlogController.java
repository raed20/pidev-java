package controllers;


import entities.Blog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.BlogService;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import entities.Bank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.BankService;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class mainBlogController implements Initializable {

    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridPane;

    private ObservableList<Blog> blogListData = FXCollections.observableArrayList();


    public ObservableList<Blog> menuGetData() throws SQLException {
        BlogService b=new BlogService();
        ObservableList<Blog> observableList = FXCollections.observableArrayList(b.getAllBlog());
        return observableList;
    }

    public void menuDisplayCard() {
        try {
            blogListData.clear();
            blogListData.addAll(menuGetData());

            int row = 0;
            int column = 0;

            menu_gridPane.getChildren().clear();
            menu_gridPane.getRowConstraints().clear();
            menu_gridPane.getColumnConstraints().clear();

            for (int q = 0; q < blogListData.size(); q++) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/LoanFront/AfficheBlog.fxml"));
                AnchorPane pane = load.load();
                AfficherblogFrontController Blogfront = load.getController();
                Blogfront.setData(blogListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);

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

}
