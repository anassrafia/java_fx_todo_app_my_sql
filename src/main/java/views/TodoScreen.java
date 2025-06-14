package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TodoScreen {
    private VBox taskList;
    private TextField taskInput;
    private Button addButton;
    private HBox header;
    private VBox root;
    private Scene scene;

    public TodoScreen(Stage stage, String username) {
        // Logo
        ImageView logoLeft = new ImageView(new Image("https://i.ibb.co/3Ymh4sKG/supmti-logo-todo.png"));
        logoLeft.setFitHeight(50);
        logoLeft.setPreserveRatio(true);

        // Logout icon
        ImageView logoutLogo = new ImageView(new Image("https://i.ibb.co/qYLty3g1/8345293.png"));
        logoutLogo.setFitHeight(30);
        logoutLogo.setPreserveRatio(true);
        logoutLogo.setCursor(javafx.scene.Cursor.HAND);

        header = new HBox(logoLeft, new Region(), logoutLogo);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #f2f2f2;");

        // Task input field
        taskInput = new TextField();
        taskInput.setPromptText("Ajouter une tâche...");

        // Add button
        addButton = new Button("Ajouter");

        HBox inputBox = new HBox(10, taskInput, addButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        // Task list
        taskList = new VBox(10);
        taskList.setPadding(new Insets(10));
        taskList.setAlignment(Pos.TOP_CENTER);

        // Root layout
        root = new VBox(header, inputBox, taskList);
        scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Bienvenue, " + username);
    }

    public VBox getTaskList() {
        return taskList;
    }

    public TextField getTaskInput() {
        return taskInput;
    }

    public Button getAddButton() {
        return addButton;
    }

    public HBox getHeader() {
        return header;
    }

    public Scene getScene() {
        return scene;
    }
}
