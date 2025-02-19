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

public class ModifierEquipeController {

    @FXML
    private TextField idEquipeTextField;

    @FXML
    private TextField nomEquipeTextField;

    @FXML
    private TextField nbrEmployeeTextField;

    @FXML
    private TextField nomTeqleadTextField;

    private final EquipeServise equipeService = new EquipeServise();

    @FXML
    public void modifierEquipe(ActionEvent event) {
        // Validation des champs
        if (idEquipeTextField.getText().isEmpty() || nomEquipeTextField.getText().isEmpty() ||
                nbrEmployeeTextField.getText().isEmpty() || nomTeqleadTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation de l'ID et du nombre d'employés
        int idEquipe, nbrEmployee;
        try {
            idEquipe = Integer.parseInt(idEquipeTextField.getText());
            nbrEmployee = Integer.parseInt(nbrEmployeeTextField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Veuillez entrer des nombres valides pour l'ID et le nombre d'employés.");
            return;
        }

        try {
            // Créer un objet Equipe avec les nouvelles valeurs
            Equipe equipeModifiee = new Equipe(nomEquipeTextField.getText(), nbrEmployee, nomTeqleadTextField.getText());
            equipeModifiee.setIdEquipe(idEquipe); // Assurez-vous que setIdEquipe existe dans votre classe Equipe

            // Appeler le service pour modifier l'équipe
            equipeService.modifier(equipeModifiee);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'équipe a été modifiée avec succès !");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de la modification de l'équipe : " + e.getMessage());
        }
    }

    @FXML
    public void retour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/afficherEquipe.fxml"));
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