package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupScreen {

    public interface SignupCallback {
        boolean registerUser(String username, String password);
        void showInfo(String message);
        void showError(String message);
        void showLoginScreen();
    }

    private final Stage primaryStage;
    private final SignupCallback callback;

    public SignupScreen(Stage primaryStage, SignupCallback callback) {
        this.primaryStage = primaryStage;
        this.callback = callback;
    }

    public void showSignupScreen() {
        Label usernameLabel = new Label("Nom d'utilisateur:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Mot de passe:");
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button("S'inscrire");
        Button backButton = new Button("Retour");

        VBox signupBox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, registerButton, backButton);
        signupBox.setAlignment(Pos.CENTER);
        signupBox.setPadding(new Insets(20));

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                if (callback.registerUser(username, password)) {
                    callback.showInfo("Compte créé avec succès.");
                    callback.showLoginScreen();
                } else {
                    callback.showError("Nom d'utilisateur déjà utilisé.");
                }
            }
        });

        backButton.setOnAction(e -> callback.showLoginScreen());

        Scene scene = new Scene(signupBox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Créer un compte");
    }
}
