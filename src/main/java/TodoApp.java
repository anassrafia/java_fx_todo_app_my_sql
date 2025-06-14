import javafx.application.Application;           // Application JavaFX de base
import javafx.geometry.*;                        // Pour la gestion des marges, alignements
import javafx.scene.Scene;                       // Représentation de la scène graphique
import javafx.scene.control.*;                   // Boutons, Labels, etc.
import javafx.scene.image.*;                      // Gestion des images (non utilisé ici)
import javafx.scene.layout.*;                     // Conteneurs comme VBox, HBox
import javafx.stage.Stage;                        // Fenêtre principale

import java.sql.*;                               // Connexion et requêtes SQL

import views.LoginScreen;                        // Ecran de connexion
import views.SignupScreen;                       // Ecran d'inscription
import views.TodoScreen;                         // Ecran de liste de tâches

import controllers.AuthService;                  // Service d'authentification
import controllers.ConnexionController;          // Contrôleur pour connexion DB
import dao.TaskDAO;                              // DAO pour tâches
import dao.UserDAO;                              // DAO pour utilisateurs


public class TodoApp extends Application implements LoginScreen.LoginCallback, SignupScreen.SignupCallback {
    // Cette classe est le coeur de l'application JavaFX
    // Elle gère la connexion DB, les transitions entre écrans, et le stockage des tâches.

    private Connection connection;         // Connexion à la base de données
    private Stage primaryStage;            // Fenêtre principale JavaFX
    private VBox taskList;                 // Liste visuelle des tâches dans l'UI
    private int loggedInUserId = -1;      // ID de l'utilisateur connecté (non connecté = -1)


    @Override
    public void start(Stage primaryStage) {
        // Point d'entrée JavaFX appelé au lancement de l'application
        this.primaryStage = primaryStage;
        connectToDatabase();               // Se connecter à la base de données
        showLoginScreen();                 // Afficher l'écran de connexion en premier
    }

    private void connectToDatabase() {
        // Cette méthode crée la connexion à la base MySQL
        ConnexionController controller = new ConnexionController();
        try {
            connection = controller.connectToDatabase();
            System.out.println("Connecté à MySQL.");
        } catch (SQLException e) {
            showError("Échec de la connexion à la base de données: " + e.getMessage());
        }
    }

    public void showLoginScreen() {
        // Affiche l'écran de connexion
        LoginScreen loginScreen = new LoginScreen(primaryStage, this);
        loginScreen.showLoginScreen();
    }

    public void showSignupScreen() {
        // Affiche l'écran d'inscription
        SignupScreen signupScreen = new SignupScreen(primaryStage, this);
        signupScreen.showSignupScreen();
    }


    private boolean authenticateUser(String username, String password) {
        // Vérifie en base si les identifiants sont corrects
        AuthService authService = new AuthService(connection);
        try {
            int userId = authService.authenticateUser(username, password);
            if (userId != -1) {
                loggedInUserId = userId;   // Sauvegarde l'ID utilisateur si ok
                return true;              // Authentification réussie
            }
        } catch (SQLException e) {
            showError("Erreur d'authentification: " + e.getMessage());
        }
        return false;                     // Sinon échec
    }

    public boolean registerUser(String username, String password) {
        // Inscrit un nouvel utilisateur en base
        UserDAO userDAO = new UserDAO(connection);
        return userDAO.registerUser(username, password);
    }


    private void showTodoScreen(String username) {
        // Affiche l'écran des tâches une fois connecté
        TodoScreen todoScreen = new TodoScreen(primaryStage, username);

        // Récupère le conteneur des tâches de l'écran TodoScreen
        this.taskList = todoScreen.getTaskList();

        // Gère l'action du bouton "Ajouter" : ajout en DB et UI
        todoScreen.getAddButton().setOnAction(e -> {
            String task = todoScreen.getTaskInput().getText().trim();
            if (!task.isEmpty()) {
                addTaskToDatabase(task);    // Ajoute la tâche dans la base
                addTaskToUI(task);          // Ajoute la tâche dans l'interface
                todoScreen.getTaskInput().clear();  // Vide le champ de saisie
            }
        });

        // Gère la déconnexion (clic sur le 3e élément de l'en-tête)
        todoScreen.getHeader().getChildren().get(2).setOnMouseClicked(e -> {
            loggedInUserId = -1;         // Réinitialise l'utilisateur connecté
            showLoginScreen();           // Retour à l'écran de connexion
        });

        loadTasks();                    // Charge les tâches existantes pour l'utilisateur
    }


    private void loadTasks() {
        // Charge depuis la DB les tâches pour l'utilisateur connecté et les affiche
        TaskDAO taskDAO = new TaskDAO(connection);
        taskDAO.loadTasksForUser(loggedInUserId, taskList);
    }

    private void addTaskToDatabase(String task) {
        // Ajoute une tâche dans la table SQL
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO todo (task, user_id) VALUES (?, ?)");
            stmt.setString(1, task);
            stmt.setInt(2, loggedInUserId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            showError("Erreur d'ajout de tâche: " + e.getMessage());
        }
    }

    private void addTaskToUI(String taskText) {
        // Crée un composant visuel pour une tâche et l'ajoute dans la liste
        Label taskLabel = new Label(taskText);
        Button doneButton = new Button("Fait");

        // Style du bouton "Fait"
        doneButton.setStyle("-fx-background-color: #34A853; -fx-text-fill: white;");

        // Action du bouton "Fait" : recharge la liste (pour mise à jour)
        doneButton.setOnAction(e -> {
            loadTasks();
        });

        HBox taskBox = new HBox(10, taskLabel, doneButton); // Ligne avec tâche + bouton
        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setPadding(new Insets(5));
        taskBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");

        taskList.getChildren().add(taskBox);  // Ajoute cette ligne à la liste des tâches
    }

    public void showError(String message) {
        // Affiche une boîte d'alerte avec un message d'erreur
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Erreur");
        alert.showAndWait();
    }

    public void showInfo(String message) {
        // Affiche une boîte d'alerte avec un message d'information
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Info");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Point d'entrée main : lance l'application JavaFX
        launch(args);
    }


    // ---------- Callbacks venant des écrans LoginScreen et SignupScreen ----------

    @Override
    public void onLoginAttempt(String username, String password) {
        // Quand l'utilisateur tente de se connecter via LoginScreen
        if (authenticateUser(username, password)) {
            showTodoScreen(username);          // Si ok, affiche la liste des tâches
        } else {
            showError("Utilisateur non trouvé ou mot de passe incorrect.");
        }
    }

    @Override
    public void onSignupClicked() {
        // Quand l'utilisateur clique sur "S'inscrire" dans LoginScreen
        showSignupScreen();
    }
}
