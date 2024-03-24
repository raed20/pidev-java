package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    //DB
    final String URL="jdbc:mysql://localhost:3306/pidev";
    final String USERNAME ="root";
    final String PASSWORD ="";

    //Att
    private Connection connection;
    private static MyConnection instance;
    //Constructor
    //Singleton step1
    private MyConnection(){
        try {
            connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connection Successful !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static MyConnection getInstance() {
        if(instance== null)
            instance=new MyConnection();
        return instance;
    }
}
