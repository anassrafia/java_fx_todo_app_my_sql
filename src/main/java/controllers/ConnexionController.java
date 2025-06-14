// src/controllers/ConnexionController.java
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionController {
    // URL de connexion à la base MySQL (hôte, port, nom de la base)
    private final String DB_URL = "jdbc:mysql://localhost:3306/todo_app_java_fx";

    // Nom d'utilisateur pour la base de données
    private final String DB_USER = "root";

    // Mot de passe pour la base de données (vide ici, modifier si besoin)
    private final String DB_PASSWORD = "";

    // Méthode pour établir et retourner une connexion à la base de données
    public Connection connectToDatabase() throws SQLException {
        // DriverManager crée la connexion en utilisant l'URL, le user et le mdp
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
