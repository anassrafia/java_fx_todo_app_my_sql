package dao;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDAO {
    private final Connection connection;

    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    public void loadTasksForUser(int userId, VBox taskList) {
        taskList.getChildren().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, task FROM todo WHERE user_id=?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("id");
                String task = rs.getString("task");

                Label taskLabel = new Label(task);
                taskLabel.setStyle("-fx-font-size: 14px;");

                Button doneButton = new Button("Fait");
                doneButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
                        + "-fx-background-radius: 5px;");
                doneButton.setOnAction(e -> {
                    deleteTask(taskId);
                    loadTasksForUser(userId, taskList); // reload list after deletion
                });

                HBox taskBox = new HBox(10, taskLabel, doneButton);
                taskBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 8px; "
                        + "-fx-border-color: #dddddd; -fx-border-width: 1px; -fx-border-radius: 5px; "
                        + "-fx-background-radius: 5px;");
                taskBox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(taskLabel, Priority.ALWAYS);

                taskList.getChildren().add(taskBox);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de chargement des tâches: " + e.getMessage());
        }
    }


    public void deleteTask(int taskId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM todo WHERE id=?");
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la tâche: " + e.getMessage());
        }
    }


}
