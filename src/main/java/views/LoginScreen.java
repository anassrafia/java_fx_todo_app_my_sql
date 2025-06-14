// File: src/main/java/views/LoginScreen.java
package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    private Stage primaryStage;
    private LoginCallback callback;

    public LoginScreen(Stage primaryStage, LoginCallback callback) {
        this.primaryStage = primaryStage;
        this.callback = callback;
    }

    public void showLoginScreen() {
        ImageView logo = new ImageView(new Image("https://i.ibb.co/3Ymh4sKG/supmti-logo-todo.png"));
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);
        logo.setTranslateY(-40);

        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Entrez votre nom d'utilisateur");
        usernameField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ddd; -fx-padding: 8 12;");

        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ddd; -fx-padding: 8 12;");

        Button loginButton = new Button("Se connecter");
        Button signupButton = new Button("Créer un compte");

        loginButton.getStyleClass().add("btn-login");
        signupButton.getStyleClass().add("btn-signup");

        FlowPane buttonPane = new FlowPane();
        buttonPane.setHgap(15);
        buttonPane.getChildren().addAll(loginButton, signupButton);
        buttonPane.setAlignment(Pos.CENTER);

        VBox loginBox = new VBox(15, logo, usernameLabel, usernameField, passwordLabel, passwordField, buttonPane);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            callback.onLoginAttempt(username, password);
        });

        signupButton.setOnAction(e -> callback.onSignupClicked());

        Scene scene = new Scene(loginBox, 400, 520);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connexion - Supmti & ToDo");
        primaryStage.show();
    }

    public interface LoginCallback {
        void onLoginAttempt(String username, String password);
        void onSignupClicked();
    }
}
