package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Projet;
import service.ProjetService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterProjetController {

    @FXML
    private TextField nomProjetTextField;

    @FXML
    private TextField dureeTextField;

    @FXML
    private TextField managerTextField;

    @FXML
    private TextField nomClientTextField;

    @FXML
    private TextField idEquipeTextField;

    private final ProjetService projetService = new ProjetService();

    @FXML
    public void ajouterProjet(ActionEvent event) {
        // Validation des champs
        if (nomProjetTextField.getText().isEmpty() || dureeTextField.getText().isEmpty() ||
                managerTextField.getText().isEmpty() || nomClientTextField.getText().isEmpty() ||
                idEquipeTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation de l'ID de l'équipe
        int idEquipe;
        try {
            idEquipe = Integer.parseInt(idEquipeTextField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Veuillez entrer un ID d'équipe valide.");
            return;
        }

        try {
            // Créer un objet Projet avec les nouvelles valeurs
            Projet projet = new Projet(nomProjetTextField.getText(), dureeTextField.getText(),
                    managerTextField.getText(), nomClientTextField.getText(), idEquipe);

            // Appeler le service pour ajouter le projet
            projetService.ajouter(projet);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le projet a été ajouté avec succès !");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de l'ajout du projet : " + e.getMessage());
        }
    }

    @FXML
    public void retour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/votre_ecran_precedent.fxml")); // Remplacez par le chemin correct
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'écran précédent.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void afficherProjets(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/afficherProjet.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'écran des projets.");
        }
    }
}