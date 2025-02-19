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
import service.EquipeServise;

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
    private ImageView logoImageView;

    @FXML
    private Button addTeamButton;

    @FXML
    private Button afficherBtn;

    @FXML
    public void initialize() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), logoImageView);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        addTeamButton.setOnMouseEntered(event -> {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), addTeamButton);
            translateTransition.setByY(-5);
            translateTransition.play();
        });

        addTeamButton.setOnMouseExited(event -> {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), addTeamButton);
            translateTransition.setByY(5);
            translateTransition.play();
        });
    }

    @FXML
    public void ajouter(javafx.event.ActionEvent actionEvent) {
        // Validation des champs vides
        if (NomEquipe_TF.getText().isEmpty() || Nombre_empl_TF.getText().isEmpty() || techLeadField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation du format du nombre d'employés
        int nombreEmployes;
        try {
            nombreEmployes = Integer.parseInt(Nombre_empl_TF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Veuillez entrer un nombre valide pour le nombre d'employés.");
            return;
        }

        // Validation du nom de l'équipe (par exemple, pas de caractères spéciaux)
        if (!NomEquipe_TF.getText().matches("[a-zA-Z0-9\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Le nom de l'équipe ne doit contenir que des lettres, des chiffres et des espaces.");
            return;
        }

        // Validation du nom du tech lead (par exemple, pas de caractères spéciaux)
        if (!techLeadField.getText().matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Le nom du tech lead ne doit contenir que des lettres et des espaces.");
            return;
        }

        try {
            ps.ajouter(new Equipe(NomEquipe_TF.getText(), nombreEmployes, techLeadField.getText()));
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'équipe a été ajoutée avec succès !");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", e.getMessage());
        }
    }

    @FXML
    public void afficherEquipes(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/afficherEquipe.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}