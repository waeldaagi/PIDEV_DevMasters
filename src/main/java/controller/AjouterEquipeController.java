package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.Equipe;
import service.EquipeService;
import java.sql.SQLException;

public class AjouterEquipeController {
    @FXML
    private TextField nomEquipeTextField;
    
    @FXML
    private TextField nbrEmployeeTextField;
    
    @FXML
    private TextField nomTechLeadTextField;

    private EquipeService equipeService = new EquipeService();

    @FXML
    private void ajouterEquipe() {
        try {
            // Validation des champs
            if (nomEquipeTextField.getText().isEmpty() || 
                nbrEmployeeTextField.getText().isEmpty() || 
                nomTechLeadTextField.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs sont obligatoires");
                return;
            }

            // Validation du nombre d'employés
            int nbrEmployee;
            try {
                nbrEmployee = Integer.parseInt(nbrEmployeeTextField.getText());
                if (nbrEmployee < 0) {
                    showAlert("Erreur", "Le nombre d'employés doit être positif");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le nombre d'employés doit être un nombre valide");
                return;
            }

            // Création de l'équipe
            Equipe equipe = new Equipe(
                nomEquipeTextField.getText(),
                nbrEmployee,
                nomTechLeadTextField.getText()
            );

            // Ajout dans la base de données
            equipeService.ajouter(equipe);
            
            // Fermeture de la fenêtre
            Stage stage = (Stage) nomEquipeTextField.getScene().getWindow();
            stage.close();
            
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'équipe : " + e.getMessage());
        }
    }

    @FXML
    private void retour() {
        Stage stage = (Stage) nomEquipeTextField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}