package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import models.UserModel;

public class AuthService {
    // On utilise UserModel pour accéder aux données utilisateurs en base
    private final UserModel userModel;

    // Constructeur : on crée un UserModel avec la connexion à la base
    public AuthService(Connection connection) {
        this.userModel = new UserModel(connection);
    }

    // Méthode pour vérifier si un utilisateur existe avec le couple username/password
    // Renvoie l'ID de l'utilisateur si trouvé, sinon -1 (dans UserModel)
    public int authenticateUser(String username, String password) throws SQLException {
        return userModel.getUserIdByCredentials(username, password);
    }
}
