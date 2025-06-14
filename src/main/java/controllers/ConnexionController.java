// src/controllers/ConnexionController.java
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionController {
    private final String DB_URL = "jdbc:mysql://localhost:3306/todo_app_java_fx";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = ""; // update if needed

    public Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
