package dao;

// Import des composants JavaFX pour construire la liste des tâches en UI
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

// Import pour gérer la base de données
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDAO {

    // Connexion à la base, initialisée dans le constructeur
    private final Connection connection;

    public TaskDAO(Connection connection) {
        this.connection = connection;  // On garde la connexion pour exécuter les requêtes
    }

    // Méthode pour charger toutes les tâches d'un utilisateur et les afficher dans un VBox
    public void loadTasksForUser(int userId, VBox taskList) {
        taskList.getChildren().clear();  // On vide d'abord la liste pour éviter les doublons

        try {
            // Prépare la requête SQL pour récupérer les tâches pour cet utilisateur
            PreparedStatement stmt = connection.prepareStatement("SELECT id, task FROM todo WHERE user_id=?");
            stmt.setInt(1, userId);  // Injecte l'ID utilisateur dans la requête

            ResultSet rs = stmt.executeQuery();  // Exécute la requête

            // Parcourt chaque tâche récupérée
            while (rs.next()) {
                int taskId = rs.getInt("id");         // Récupère l'ID de la tâche
                String task = rs.getString("task");   // Récupère le texte de la tâche

                // Crée un Label pour afficher le texte de la tâche
                Label taskLabel = new Label(task);
                taskLabel.setStyle("-fx-font-size: 14px;");  // Taille du texte

                // Crée un bouton "Fait" pour supprimer la tâche
                Button doneButton = new Button("Fait");
                doneButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
                        + "-fx-background-radius: 5px;");

                // Quand on clique sur "Fait", on supprime la tâche puis on recharge la liste
                doneButton.setOnAction(e -> {
                    deleteTask(taskId);                    // Supprime en base
                    loadTasksForUser(userId, taskList);   // Recharge la liste affichée
                });

                // Place le Label et le Bouton dans un HBox (ligne horizontale)
                HBox taskBox = new HBox(10, taskLabel, doneButton);
                // Style visuel de la ligne tâche
                taskBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 8px; "
                        + "-fx-border-color: #dddddd; -fx-border-width: 1px; -fx-border-radius: 5px; "
                        + "-fx-background-radius: 5px;");
                taskBox.setMaxWidth(Double.MAX_VALUE); // Prend toute la largeur possible
                HBox.setHgrow(taskLabel, Priority.ALWAYS);  // Le label prend tout l'espace libre

                // Ajoute cette ligne de tâche dans le VBox qui affiche la liste
                taskList.getChildren().add(taskBox);
            }
        } catch (SQLException e) {
            // En cas d'erreur SQL, affiche le message dans la console
            System.err.println("Erreur de chargement des tâches: " + e.getMessage());
        }
    }


    // Méthode pour supprimer une tâche de la base via son ID
    public void deleteTask(int taskId) {
        try {
            // Prépare la requête SQL DELETE avec l'ID de la tâche
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM todo WHERE id=?");
            stmt.setInt(1, taskId);   // Injecte l'ID dans la requête
            stmt.executeUpdate();     // Exécute la suppression
        } catch (SQLException e) {
            // Affiche une erreur dans la console si problème
            System.err.println("Erreur lors de la suppression de la tâche: " + e.getMessage());
        }
    }
}
