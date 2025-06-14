import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;  // this includes Button, Label, etc.
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;   // includes HBox, VBox, Region, Priority, etc.
import javafx.stage.Stage;

import java.sql.*;  // for your database stuff

import views.LoginScreen;
import views.SignupScreen;
import controllers.AuthService;
import controllers.ConnexionController;
import dao.UserDAO;






public class TodoApp extends Application implements LoginScreen.LoginCallback, SignupScreen.SignupCallback{

    private Connection connection;
    private Stage primaryStage;
    private VBox taskList;
    private int loggedInUserId = -1;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        connectToDatabase();
        showLoginScreen();
    }



    private void connectToDatabase() {
        ConnexionController controller = new ConnexionController();
        try {
            connection = controller.connectToDatabase();
            System.out.println("Connecté à MySQL.");
        } catch (SQLException e) {
            showError("Échec de la connexion à la base de données: " + e.getMessage());
        }
    }





    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(primaryStage, this);
        loginScreen.showLoginScreen();
    }




    public void showSignupScreen() {
        SignupScreen signupScreen = new SignupScreen(primaryStage, this);
        signupScreen.showSignupScreen();
    }







private boolean authenticateUser(String username, String password) {
    AuthService authService = new AuthService(connection);
    try {
        int userId = authService.authenticateUser(username, password);
        if (userId != -1) {
            loggedInUserId = userId;
            return true;
        }
    } catch (SQLException e) {
        showError("Erreur d'authentification: " + e.getMessage());
    }
    return false;
}











public boolean registerUser(String username, String password) {
    UserDAO userDAO = new UserDAO(connection);
    return userDAO.registerUser(username, password);
}






    private void showTodoScreen(String username) {
        ImageView logoLeft = new ImageView(new Image("https://i.ibb.co/3Ymh4sKG/supmti-logo-todo.png"));
        logoLeft.setFitHeight(50);
        logoLeft.setPreserveRatio(true);

        ImageView logoutLogo = new ImageView(new Image("https://i.ibb.co/qYLty3g1/8345293.png"));
        logoutLogo.setFitHeight(30);
        logoutLogo.setPreserveRatio(true);
        logoutLogo.setCursor(javafx.scene.Cursor.HAND);
        logoutLogo.setOnMouseClicked(e -> {
            loggedInUserId = -1;
            showLoginScreen();
        });

        HBox header = new HBox(logoLeft, new Region(), logoutLogo);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #f2f2f2;");

        TextField taskInput = new TextField();
        taskInput.setPromptText("Ajouter une tâche...");
        Button addButton = new Button("Ajouter");

        HBox inputBox = new HBox(10, taskInput, addButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        taskList = new VBox(10);
        taskList.setPadding(new Insets(10));
        taskList.setAlignment(Pos.TOP_CENTER);

        addButton.setOnAction(e -> {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                addTaskToDatabase(task);
                addTaskToUI(task);
                taskInput.clear();
            }
        });

        loadTasks();

        VBox root = new VBox(header, inputBox, taskList);
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bienvenue, " + username);
    }

    private void loadTasks() {
        taskList.getChildren().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT task FROM todo WHERE user_id=?");
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                addTaskToUI(rs.getString("task"));
            }
        } catch (SQLException e) {
            showError("Erreur de chargement des tâches: " + e.getMessage());
        }
    }

    private void addTaskToDatabase(String task) {
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
        Label taskLabel = new Label(taskText);
        Button doneButton = new Button("Fait");
        doneButton.setStyle("-fx-background-color: #34A853; -fx-text-fill: white;");
        doneButton.setOnAction(e -> {
            taskLabel.setStyle("-fx-text-fill: gray; -fx-strikethrough: true;");
            doneButton.setDisable(true);
        });

        HBox taskBox = new HBox(10, taskLabel, doneButton);
        taskBox.setAlignment(Pos.CENTER_LEFT);
        taskBox.setPadding(new Insets(5));
        taskBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        taskList.getChildren().add(taskBox);
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Erreur");
        alert.showAndWait();
    }

    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Info");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }











    @Override
public void onLoginAttempt(String username, String password) {
    if (authenticateUser(username, password)) {
        showTodoScreen(username);
    } else {
        showError("Utilisateur non trouvé ou mot de passe incorrect.");
    }
}

@Override
public void onSignupClicked() {
    showSignupScreen();
}


}
