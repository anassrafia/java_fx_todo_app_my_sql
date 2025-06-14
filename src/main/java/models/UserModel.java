package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    private final Connection connection;

    public UserModel(Connection connection) {
        this.connection = connection;
    }

    public int getUserIdByCredentials(String username, String password) throws SQLException {
        String query = "SELECT id FROM users WHERE username=? AND password=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
}
