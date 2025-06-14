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
import views.TodoScreen;
import controllers.AuthService;
import controllers.ConnexionController;
import dao.TaskDAO;
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
    TodoScreen todoScreen = new TodoScreen(primaryStage, username);

    // link taskList from the UI
    this.taskList = todoScreen.getTaskList();

    // handle Add button action
    todoScreen.getAddButton().setOnAction(e -> {
        String task = todoScreen.getTaskInput().getText().trim();
        if (!task.isEmpty()) {
            addTaskToDatabase(task);
            addTaskToUI(task);
            todoScreen.getTaskInput().clear();
        }
    });

    // handle logout
    todoScreen.getHeader().getChildren().get(2).setOnMouseClicked(e -> {
        loggedInUserId = -1;
        showLoginScreen();
    });

    // Load existing tasks
    loadTasks();
}













    

private void loadTasks() {
    TaskDAO taskDAO = new TaskDAO(connection);
    taskDAO.loadTasksForUser(loggedInUserId, taskList);
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
            loadTasks();
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