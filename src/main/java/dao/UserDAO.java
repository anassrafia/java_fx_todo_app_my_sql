package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    // Connexion à la base de données, fournie au constructeur
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;  // On garde la connexion pour faire des requêtes SQL
    }

    // Méthode pour enregistrer un nouvel utilisateur dans la base
    public boolean registerUser(String username, String password) {
        try {
            // Prépare la requête SQL pour insérer un nouvel utilisateur avec son nom et mot de passe
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)"
            );
            stmt.setString(1, username);  // Remplace le premier ? par le nom d'utilisateur
            stmt.setString(2, password);  // Remplace le deuxième ? par le mot de passe
            stmt.executeUpdate();          // Exécute la requête (insertion dans la DB)
            return true;                   // Si pas d'erreur, renvoie vrai (succès)
        } catch (SQLException e) {
            // En cas d'erreur SQL (ex: doublon, problème DB), renvoie faux (échec)
            return false;
        }
    }
}
