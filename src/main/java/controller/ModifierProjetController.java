package controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Projet;
import service.ProjetService;

import java.sql.SQLException;

public class ModifierProjetController {

    @FXML
    private TextField nomProjetField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private TextField managerField;

    @FXML
    private TextField nomClientField;

    private Projet projet; // Variable to store the selected project

    private ProjetService projetService = new ProjetService();

    // Method to set the selected project
    public void setProjet(Projet projet) {
        this.projet = projet;
        populateFields(); // Populate the fields with the project data
    }

    // Populate the fields with the project data
    private void populateFields() {
        if (projet != null) {
            nomProjetField.setText(projet.getNom_projet());
            deadlinePicker.setValue(projet.getDeadline().toLocalDate());
            managerField.setText(projet.getManager());
            nomClientField.setText(projet.getNom_client());
        }
    }

    @FXML
    private void handleModifierButtonClick() {
        if (projet != null) {
            // Update the project object with the new values
            projet.setNom_projet(nomProjetField.getText());
            projet.setDeadline(java.sql.Date.valueOf(deadlinePicker.getValue()));
            projet.setManager(managerField.getText());
            projet.setNom_client(nomClientField.getText());

            try {
                // Call the service to update the project in the database
                projetService.modifier(projet);
                System.out.println("Projet modifié avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la modification du projet : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun projet sélectionné.");
        }
    }
}