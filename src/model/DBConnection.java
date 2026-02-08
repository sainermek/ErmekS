package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/OOP";
        String user = "postgres";
        String password = "Ermek2008";
        return DriverManager.getConnection(url, user, password);
    }
}
