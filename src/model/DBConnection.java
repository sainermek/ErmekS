// DBConnection
package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/OOP"; // имя твоей базы
        String user = "postgres";      // имя пользователя
        String password = "Ermek2008"; // пароль
        return DriverManager.getConnection(url, user, password);
    }
}
