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
    private Stage primaryStage;       // Fenêtre principale de l'application
    private LoginCallback callback;   // Interface pour communiquer avec le contrôleur

    // Constructeur qui reçoit la fenêtre principale et la callback pour gérer les événements
    public LoginScreen(Stage primaryStage, LoginCallback callback) {
        this.primaryStage = primaryStage;
        this.callback = callback;
    }

    // Méthode pour afficher l'écran de connexion
    public void showLoginScreen() {
        // Logo affiché en haut de l'écran
        ImageView logo = new ImageView(new Image("https://i.ibb.co/3Ymh4sKG/supmti-logo-todo.png"));
        logo.setFitHeight(100);       // Hauteur fixe
        logo.setPreserveRatio(true);  // Proportion conservée
        logo.setTranslateY(-40);      // Décalage vers le haut (ajustement visuel)

        // Label pour le nom d'utilisateur
        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Champ texte pour saisir le nom d'utilisateur
        TextField usernameField = new TextField();
        usernameField.setPromptText("Entrez votre nom d'utilisateur");
        usernameField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ddd; -fx-padding: 8 12;");

        // Label pour le mot de passe
        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Champ de type mot de passe (texte masqué)
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ddd; -fx-padding: 8 12;");

        // Boutons pour se connecter et créer un compte
        Button loginButton = new Button("Se connecter");
        Button signupButton = new Button("Créer un compte");

        // Ajout des classes CSS pour styliser les boutons (styles dans login.css)
        loginButton.getStyleClass().add("btn-login");
        signupButton.getStyleClass().add("btn-signup");

        // Conteneur horizontal pour les boutons avec un espacement entre eux
        FlowPane buttonPane = new FlowPane();
        buttonPane.setHgap(15);            // Espacement horizontal entre boutons
        buttonPane.getChildren().addAll(loginButton, signupButton);
        buttonPane.setAlignment(Pos.CENTER);  // Centrer les boutons

        // Conteneur vertical principal regroupant tous les éléments
        VBox loginBox = new VBox(15, logo, usernameLabel, usernameField, passwordLabel, passwordField, buttonPane);
        loginBox.setAlignment(Pos.CENTER);      // Centrer tous les éléments verticalement
        loginBox.setPadding(new Insets(30));    // Espacement intérieur
        // Style CSS inline pour fond, arrondis et ombre portée légère
        loginBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Quand on clique sur le bouton connexion
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();  // Récupère le texte saisi sans espaces superflus
            String password = passwordField.getText().trim();
            callback.onLoginAttempt(username, password);       // Appelle la méthode de callback pour tenter la connexion
        });

        // Quand on clique sur le bouton créer un compte
        signupButton.setOnAction(e -> callback.onSignupClicked());

        // Création de la scène (zone affichée dans la fenêtre) avec taille fixe
        Scene scene = new Scene(loginBox, 400, 520);
        // Ajout d'une feuille de style externe (fichier CSS)
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());

        // Mise à jour de la scène et du titre dans la fenêtre principale
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connexion - Supmti & ToDo");
        primaryStage.show();   // Affiche la fenêtre
    }

    // Interface que doit implémenter la classe qui crée cette vue pour gérer les actions
    public interface LoginCallback {
        void onLoginAttempt(String username, String password);  // Quand on tente de se connecter
        void onSignupClicked();                                // Quand on clique pour créer un compte
    }
}
