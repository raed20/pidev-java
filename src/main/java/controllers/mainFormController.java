/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.BankService;


/**
 *
 * @author WINDOWS 10
 */
public class mainFormController implements Initializable {
    
    @FXML
    private AnchorPane m_form;

    

    @FXML
    private Button menu_btn;
    @FXML
    private Button calc_btn;
    

    @FXML
    private GridPane menu_gridPane;
    

    @FXML
    private AnchorPane dashboard_form;
    

    
    private ObservableList<Bank> bankListData = FXCollections.observableArrayList();


    public ObservableList<Bank> menuGetData() throws SQLException {
        BankService b=new BankService();
        ObservableList<Bank> observableList = FXCollections.observableArrayList(b.findAll());
        return observableList;
    }



    public void menuDisplayCard() {
        try {
            bankListData.clear();
            bankListData.addAll(menuGetData());

            int row = 0;
            int column = 0;

            menu_gridPane.getChildren().clear();
            menu_gridPane.getRowConstraints().clear();
            menu_gridPane.getColumnConstraints().clear();

            for (int q = 0; q < bankListData.size(); q++) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/LoanFront/cardBank.fxml"));
                AnchorPane pane = load.load();
                CardController cardC = load.getController();
                cardC.setData(bankListData.get(q));

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

    private void loadPage(String fxmlPath) {
        // Load a new FXML page into the anchor pane
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            m_form.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }


    public void switchForm(ActionEvent event) throws IOException {


         if (event.getSource() == menu_btn) {
             dashboard_form.setVisible(false);
             loadPage("/javafx/FrontOffice/LoanFront/LoanShow.fxml");
             /*Parent form = loader.load();
             Stage stage = new Stage();
             stage.setScene(new Scene(form));
             stage.showAndWait(); // Use showAndWait to refresh list after adding or updating*/

             //reloadPrets(); // Reload the list after closing the form

           /* menuDisplayTotal();
            menuShowOrderData();*/
        } else if (event.getSource() == calc_btn) {
             loadPage("/javafx/FrontOffice/LoanFront/LoanCalculator.fxml");
             /*Parent form = loader.load();
             Stage stage = new Stage();
             stage.setScene(new Scene(form));
             stage.showAndWait();*/

           // customersShowData();
        }

    }
// LETS PROCEED TO OUR DASHBOARD FORM : )


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


// THATS IT FOR THIS VIDEO, THANKS FOR WATCHING!
// SUBSCRIBE OUR CHANNEL FOR MORE UNIQUE TUTORIALS 
// THANK YOU!