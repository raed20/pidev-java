package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import tools.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;


public class DashbordBack implements Initializable {

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;

    private Connection cnx;
    private PreparedStatement prepare;
    private ResultSet result;
    @FXML
    private Button back;

    public DashbordBack() {
        MyConnection myConnection = new MyConnection();
        cnx = myConnection.getConnection();
    }


    public void dashboardDisplayNC() {

        String sql = "SELECT COUNT(id) FROM pret";

        try {
            int nc = 0;
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt("COUNT(id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayTI() throws SQLException {

        String sql = "SELECT count(id) FROM bank";


        try {
            double ti = 0;
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getInt("COUNT(id)");
            }

            dashboard_TI.setText(String.valueOf(ti));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT max(Loan_amount) FROM pret";

        try {
            float ti = 0;
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("max(Loan_amount)");
            }
            dashboard_TotalI.setText(ti+ " DT");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardNSP() {

        String sql = "SELECT COUNT(*) FROM user";

        try {
            int q = 0;
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                q = result.getInt("COUNT(*)");
            }
            dashboard_NSP.setText(String.valueOf(q));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT b.nom AS nom, COUNT(*) AS loanCount " +
                "FROM pret p " +
                "JOIN bank b ON p.idBank = b.id " +
                "GROUP BY p.idBank";

        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart(){
        dashboard_CustomerChart.getData().clear();

        String sql = "SELECT b.nom AS bankName, COUNT(DISTINCT u.id) AS userCount " +
                "FROM user u " +
                "JOIN pret p ON u.id = p.iduser " +
                "JOIN bank b ON p.idbank = b.id " +
                "GROUP BY b.nom;";



        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = cnx.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_CustomerChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashboardDisplayNC();
        try {
            dashboardDisplayTI();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dashboardTotalI();
        dashboardNSP();
        dashboardIncomeChart();
        dashboardCustomerChart();

        back.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/BackOffice/BackSidebar.fxml"));
            Parent form;
            try {
                form = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the current scene from the button
            Scene currentScene = back.getScene();

            // Set the new content in the current scene
            currentScene.setRoot(form);
        });

    }
}
