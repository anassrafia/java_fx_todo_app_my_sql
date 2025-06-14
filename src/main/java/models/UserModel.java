package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    // Connexion à la base de données fournie par le contrôleur
    private final Connection connection;

    public UserModel(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour récupérer l'ID utilisateur en fonction du nom et mot de passe
    public int getUserIdByCredentials(String username, String password) throws SQLException {
        // Requête SQL sécurisée pour éviter injection SQL
        String query = "SELECT id FROM users WHERE username=? AND password=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);  // Place le username dans la requête
            stmt.setString(2, password);  // Place le password dans la requête
            ResultSet rs = stmt.executeQuery();  // Exécute la requête
            if (rs.next()) {  // Si un utilisateur est trouvé
                return rs.getInt("id");  // Retourne son ID
            }
        }
        // Si pas trouvé, retourne -1
        return -1;
    }
}
