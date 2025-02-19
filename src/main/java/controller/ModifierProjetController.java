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

public class ModifierProjetController {

    @FXML
    private TextField idProjetTextField;

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
    public void modifierProjet(ActionEvent event) {
        // Validation des champs
        if (idProjetTextField.getText().isEmpty() || nomProjetTextField.getText().isEmpty() ||
                dureeTextField.getText().isEmpty() || managerTextField.getText().isEmpty() ||
                nomClientTextField.getText().isEmpty() || idEquipeTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation de l'ID du projet et de l'ID de l'équipe
        int idProjet, idEquipe;
        try {
            idProjet = Integer.parseInt(idProjetTextField.getText());
            idEquipe = Integer.parseInt(idEquipeTextField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Veuillez entrer des nombres valides pour l'ID du projet et l'ID de l'équipe.");
            return;
        }

        try {
            // Créer un objet Projet avec les nouvelles valeurs
            Projet projetModifie = new Projet(nomProjetTextField.getText(), dureeTextField.getText(),
                    managerTextField.getText(), nomClientTextField.getText(), idEquipe);
            projetModifie.setId_projet(idProjet); // Assurez-vous que setIdProjet existe dans votre classe Projet

            // Appeler le service pour modifier le projet
            projetService.modifier(projetModifie);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le projet a été modifié avec succès !");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de la modification du projet : " + e.getMessage());
        }
    }

    @FXML
    public void retour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/afficherProjet.fxml"));
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
}