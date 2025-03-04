package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Equipe;
import models.Projet;
import service.EquipeService;
import service.ProjetService;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierProjetController implements Initializable {

    @FXML
    private TextField nomProjetField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private TextField managerField;

    @FXML
    private TextField nomClientField;

    @FXML
    private ComboBox<Equipe> equipeComboBox;

    private Projet projetActuel;
    private ProjetService projetService = new ProjetService();
    private EquipeService equipeService = new EquipeService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerEquipes();
    }

    public void setProjet(Projet projet) {
        this.projetActuel = projet;
        populateFields();
    }

    private void chargerEquipes() {
        try {
            List<Equipe> equipes = equipeService.getAll(new Equipe());
            equipeComboBox.getItems().addAll(equipes);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipes : " + e.getMessage());
        }
    }

    private void populateFields() {
        if (projetActuel != null) {
            nomProjetField.setText(projetActuel.getNom_projet());
            deadlinePicker.setValue(projetActuel.getDeadline().toLocalDate());
            managerField.setText(projetActuel.getManager());
            nomClientField.setText(projetActuel.getNom_client());
            equipeComboBox.setValue(projetActuel.getEquipe());
        }
    }

    @FXML
    private void handleSaveButtonClick() {
        if (!validateFields()) {
            return;
        }

        try {
            // Mettre à jour les données du projet
            projetActuel.setNom_projet(nomProjetField.getText());
            projetActuel.setDeadline(Date.valueOf(deadlinePicker.getValue()));
            projetActuel.setManager(managerField.getText());
            projetActuel.setNom_client(nomClientField.getText());
            projetActuel.setEquipe(equipeComboBox.getValue());

            // Sauvegarder les modifications
            projetService.modifier(projetActuel);
            
            showAlert("Succès", "Projet modifié avec succès");
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification du projet : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelButtonClick() {
        closeWindow();
    }

    private boolean validateFields() {
        if (nomProjetField.getText().isEmpty() || 
            deadlinePicker.getValue() == null ||
            managerField.getText().isEmpty() ||
            nomClientField.getText().isEmpty() ||
            equipeComboBox.getValue() == null) {
            
            showAlert("Erreur", "Tous les champs sont obligatoires");
            return false;
        }
        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) nomProjetField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}