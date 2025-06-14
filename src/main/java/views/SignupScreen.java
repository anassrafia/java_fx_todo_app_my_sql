package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupScreen {

    // Interface pour communiquer avec la logique métier depuis cette vue
    public interface SignupCallback {
        boolean registerUser(String username, String password);  // Essayer d'enregistrer un utilisateur
        void showInfo(String message);                          // Afficher un message d'information
        void showError(String message);                         // Afficher un message d'erreur
        void showLoginScreen();                                 // Revenir à l'écran de connexion
    }

    private final Stage primaryStage;   // Fenêtre principale
    private final SignupCallback callback; // Callback pour gérer les interactions

    // Constructeur, reçoit la fenêtre et la callback
    public SignupScreen(Stage primaryStage, SignupCallback callback) {
        this.primaryStage = primaryStage;
        this.callback = callback;
    }

    // Méthode pour afficher l'écran d'inscription
    public void showSignupScreen() {
        // Label et champ texte pour le nom d'utilisateur
        Label usernameLabel = new Label("Nom d'utilisateur:");
        TextField usernameField = new TextField();

        // Label et champ mot de passe (texte masqué)
        Label passwordLabel = new Label("Mot de passe:");
        PasswordField passwordField = new PasswordField();

        // Bouton pour s'inscrire
        Button registerButton = new Button("S'inscrire");
        // Bouton pour revenir à l'écran de connexion
        Button backButton = new Button("Retour");

        // Conteneur vertical qui organise les éléments avec un espacement de 10px
        VBox signupBox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, registerButton, backButton);
        signupBox.setAlignment(Pos.CENTER);           // Centre tous les éléments horizontalement
        signupBox.setPadding(new Insets(20));         // Marge intérieure autour des éléments

        // Action quand on clique sur le bouton "S'inscrire"
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();  // Récupérer le nom sans espaces inutiles
            String password = passwordField.getText().trim();  // Récupérer le mot de passe sans espaces
            if (!username.isEmpty() && !password.isEmpty()) {  // Vérifier que les champs ne sont pas vides
                if (callback.registerUser(username, password)) {  // Appeler la méthode d'inscription
                    callback.showInfo("Compte créé avec succès.");  // Afficher message succès
                    callback.showLoginScreen();                      // Retourner à l'écran de connexion
                } else {
                    callback.showError("Nom d'utilisateur déjà utilisé."); // Afficher erreur si nom déjà pris
                }
            }
            // Optionnel : sinon, on pourrait afficher un message si champs vides
        });

        // Action quand on clique sur "Retour"
        backButton.setOnAction(e -> callback.showLoginScreen()); // Revenir à l'écran de connexion

        // Création de la scène avec la boîte principale et dimensions fixes
        Scene scene = new Scene(signupBox, 400, 300);
        primaryStage.setScene(scene);        // Appliquer la scène à la fenêtre
        primaryStage.setTitle("Créer un compte");  // Titre de la fenêtre
    }
}
