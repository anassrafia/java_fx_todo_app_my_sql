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
    private VBox taskList;       // Liste des tâches affichées verticalement
    private TextField taskInput; // Champ texte pour ajouter une nouvelle tâche
    private Button addButton;    // Bouton pour ajouter la tâche écrite
    private HBox header;         // Barre d'en-tête avec logo et bouton déconnexion
    private VBox root;           // Conteneur principal vertical
    private Scene scene;         // Scène principale pour afficher à l'écran

    // Constructeur prend la fenêtre principale et le nom d'utilisateur affiché
    public TodoScreen(Stage stage, String username) {
        // Logo à gauche
        ImageView logoLeft = new ImageView(new Image("https://i.ibb.co/3Ymh4sKG/supmti-logo-todo.png"));
        logoLeft.setFitHeight(50);     // Hauteur fixe
        logoLeft.setPreserveRatio(true); // Garder les proportions de l'image

        // Logo déconnexion à droite, avec curseur main pour indiquer que c'est cliquable
        ImageView logoutLogo = new ImageView(new Image("https://i.ibb.co/qYLty3g1/8345293.png"));
        logoutLogo.setFitHeight(30);
        logoutLogo.setPreserveRatio(true);
        logoutLogo.setCursor(javafx.scene.Cursor.HAND);

        // Header : logo à gauche, espace flexible au milieu, logo déconnexion à droite
        header = new HBox(logoLeft, new Region(), logoutLogo);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS); // Le Region prend tout l'espace libre
        header.setPadding(new Insets(10));        // Marge interne autour
        header.setAlignment(Pos.CENTER);          // Centre verticalement les éléments
        header.setStyle("-fx-background-color: #f2f2f2;"); // Couleur gris clair en fond

        // Champ texte pour saisir une nouvelle tâche avec un texte d'indication
        taskInput = new TextField();
        taskInput.setPromptText("Ajouter une tâche...");

        // Bouton pour valider l'ajout
        addButton = new Button("Ajouter");

        // Boîte horizontale pour aligner champ + bouton avec un espacement de 10 px
        HBox inputBox = new HBox(10, taskInput, addButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        // Liste verticale qui contiendra les tâches
        taskList = new VBox(10);
        taskList.setPadding(new Insets(10));
        taskList.setAlignment(Pos.TOP_CENTER); // Alignement en haut et centré horizontalement

        // Conteneur principal vertical qui empile : header, input + bouton, liste des tâches
        root = new VBox(header, inputBox, taskList);

        // Création de la scène avec dimensions fixes
        scene = new Scene(root, 500, 600);

        // Appliquer la scène à la fenêtre principale
        stage.setScene(scene);

        // Définir le titre de la fenêtre avec le nom d'utilisateur
        stage.setTitle("Bienvenue, " + username);
    }

    // Getters pour accéder aux éléments UI depuis l'extérieur

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
