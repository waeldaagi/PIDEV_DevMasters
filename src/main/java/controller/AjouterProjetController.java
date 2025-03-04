package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Equipe;
import models.Projet;
import service.EquipeService;
import service.ProjetService;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterProjetController implements Initializable {

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

    private EquipeService equipeService = new EquipeService();
    private ProjetService projetService = new ProjetService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load teams into the ComboBox
        chargerEquipes();
    }

    private void chargerEquipes() {
        try {
            // Fetch all teams from the database
            List<Equipe> equipes = equipeService.getAll(new Equipe());
            equipeComboBox.getItems().addAll(equipes); // Add teams to the ComboBox
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipes : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterProjet() {
        // Get values from the form fields
        String nomProjet = nomProjetField.getText();
        Date deadline = Date.valueOf(deadlinePicker.getValue());
        String manager = managerField.getText();
        String nomClient = nomClientField.getText();
        Equipe equipe = equipeComboBox.getValue(); // Get the selected team

        // Validate the selected team
        if (equipe == null) {
            showAlert("Erreur", "Veuillez sélectionner une équipe valide.");
            return;
        }

        // Debug: Print the selected team
        System.out.println("Équipe sélectionnée : " + equipe.getNomEquipe() + " (ID: " + equipe.getIdEquipe() + ")");

        // Create a new Projet object
        Projet projet = new Projet(nomProjet, deadline, manager, nomClient, equipe);

        try {
            // Add the project to the database
            projetService.ajouter(projet);
            showAlert("Succès", "Projet ajouté avec succès.");

            // Clear the form fields after successful addition
            clearFields();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du projet : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomProjetField.clear();
        deadlinePicker.setValue(null);
        managerField.clear();
        nomClientField.clear();
        equipeComboBox.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}