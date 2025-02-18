package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Equipe;
import servise.EquipeServise;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class Ajouter_equipe_controller {
    private final EquipeServise ps = new EquipeServise();

    @FXML
    private TextField NomEquipe_TF;

    @FXML
    private TextField Nombre_empl_TF;

    @FXML
    private TextField techLeadField;

    @FXML
    private ImageView logoImageView; // Référence au logo

    @FXML
    private Button addTeamButton; // Référence au bouton

    @FXML
    private Button afficherBtn;

    @FXML
    public void initialize() {
        // Animation de fondu pour le logo
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), logoImageView);
        fadeTransition.setFromValue(0.0); // Début transparent
        fadeTransition.setToValue(1.0);   // Fin opaque
        fadeTransition.play(); // Lancer l'animation

        // Animation de déplacement pour le bouton
        addTeamButton.setOnMouseEntered(event -> {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), addTeamButton);
            translateTransition.setByY(-5); // Déplacer le bouton vers le haut de 5 pixels
            translateTransition.play();
        });

        addTeamButton.setOnMouseExited(event -> {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), addTeamButton);
            translateTransition.setByY(5); // Revenir à la position d'origine
            translateTransition.play();
        });
    }

    @FXML
    public void ajouter(javafx.event.ActionEvent actionEvent) {
        try {
            ps.ajouter(new Equipe(NomEquipe_TF.getText(),Integer.parseInt(Nombre_empl_TF.getText()),techLeadField.getText()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherEquipe.fxml"));
            try {
                Parent root = loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


    }
    @FXML
    public void afficherEquipes(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/afficherEquipe.fxml")); // Assurez-vous que le chemin est correct
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}