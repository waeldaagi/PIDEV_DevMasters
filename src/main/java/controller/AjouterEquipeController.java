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
import models.Equipe;
import service.EquipeServise;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterEquipeController {

    @FXML
    private TextField nomEquipeTextField;

    @FXML
    private TextField nbrEmployeeTextField;

    @FXML
    private TextField nomTechLeadTextField;

    private final EquipeServise equipeService = new EquipeServise();

    @FXML
    private void ajouterEquipe(ActionEvent event) {
        try {
            // Récupérer les valeurs saisies
            String nomEquipe = nomEquipeTextField.getText();
            int nbrEmployee = Integer.parseInt(nbrEmployeeTextField.getText());
            String nomTechLead = nomTechLeadTextField.getText();

            // Créer un objet Equipe
            Equipe equipe = new Equipe();
            equipe.setNomEquipe(nomEquipe);
            equipe.setNbrEmployee(nbrEmployee);
            equipe.setNomTeqlead(nomTechLead);

            // Appeler le service pour ajouter l'équipe
            equipeService.ajouter(equipe);

            // Afficher un message de succès
            showAlert("Succès", "Équipe ajoutée avec succès !", Alert.AlertType.INFORMATION);

            // Rediriger vers l'écran d'affichage des équipes
            retour(event);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Nombre d'employés invalide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'équipe : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void retour(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'écran d'affichage des équipes
            Parent root = FXMLLoader.load(getClass().getResource("/afficherEquipe.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Operation sur les Equipes");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'écran d'affichage des équipes.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}