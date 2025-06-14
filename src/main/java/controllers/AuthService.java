package controllers;

import java.sql.Connection;
import java.sql.SQLException;

import models.UserModel;

public class AuthService {
    private final UserModel userModel;

    public AuthService(Connection connection) {
        this.userModel = new UserModel(connection);
    }

    public int authenticateUser(String username, String password) throws SQLException {
        return userModel.getUserIdByCredentials(username, password);
    }
}
