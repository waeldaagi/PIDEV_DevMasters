package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*; // Updated: Changed import to include ChoiceBox
import javafx.stage.Stage;
import java.sql.SQLException;

import services.ServiceDemande;
import models.Demande;

import javafx.stage.FileChooser;
import java.io.File;

public class AjoutDemandeController {

    @FXML
    private TextField cv;

    @FXML
    private TextField lettre;

    @FXML
    private ChoiceBox<String> type_contrat; // Updated: Changed from TextField to ChoiceBox

    private int idOffre; // Stocker l'ID de l'offre sélectionnée

    private final ServiceDemande serviceDemande = new ServiceDemande();

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
        System.out.println("ID de l'offre reçu : " + idOffre);
    }

    @FXML
    public void initialize() { // Updated: Initialize ChoiceBox values
        type_contrat.getItems().addAll("CDI", "CDD", "Stage", "Freelance"); // Example contract types
    }

    @FXML
    void ajouter_demande(ActionEvent event) {
        // Récupération des valeurs saisies
        String cv_d = cv.getText().trim();
        String lettre_d = lettre.getText().trim();
        String type = type_contrat.getValue(); // Updated: Get value from ChoiceBox

        // Vérification des champs vides
        if (cv_d.isEmpty() || lettre_d.isEmpty() || type == null || type.isEmpty()) { // Updated: Check for null ChoiceBox selection
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // ID de l'utilisateur connecté (à adapter si nécessaire)
        int idUser = 1;

        // Création de l'objet Demande
        Demande demande = new Demande(0, idUser, idOffre, type, cv_d, lettre_d);

        try {
            serviceDemande.ajouter(demande);
            showAlert("Succès", "Demande ajoutée avec succès !");

            // Fermer la fenêtre actuelle
            Stage stage = (Stage) cv.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur SQL : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void cv_pdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");

        // Filtrer pour n'afficher que les fichiers PDF
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Ouvrir le dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            cv.setText(selectedFile.getAbsolutePath()); // Mettre le chemin dans le champ texte
        } else {
            showAlert("Information", "Aucun fichier sélectionné.");
        }
    }

    @FXML
    void lettre_pdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une lettre de motivation");

        // Filtrer pour n'afficher que les fichiers PDF ou TXT
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Fichiers Texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(pdfFilter, txtFilter);

        // Ouvrir le dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            lettre.setText(selectedFile.getAbsolutePath()); // Mettre le chemin dans le champ texte
        } else {
            showAlert("Information", "Aucun fichier sélectionné.");
        }
    }
}
