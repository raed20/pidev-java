package com.example.reclamation.config;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnexion {

    final String URL="jdbc:mariadb://localhost:3307/reclamations";
    final String USER ="root";
    final String PWD ="";
    private static java.sql.Connection cnx ;
    private static DBConnexion instance ;


    private  DBConnexion() {

        try {
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("connexion etablie ......");
        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }
    public static DBConnexion getInstance(){

        if (instance == null){
            instance = new DBConnexion();
        }

        return instance;
    }
    public static java.sql.Connection getCnx (){
        return cnx;
    }

}
