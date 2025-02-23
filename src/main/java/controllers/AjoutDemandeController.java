package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

import services.ServiceDemande;
import models.Demande;

public class AjoutDemandeController {

    @FXML
    private TextField cv;

    @FXML
    private TextField lettre;

    @FXML
    private TextField type_contrat;

    private int idOffre; // Stocker l'ID de l'offre sélectionnée

    private final ServiceDemande serviceDemande = new ServiceDemande();

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
        System.out.println("ID de l'offre reçu : " + idOffre);
    }

    @FXML
    void ajouter_demande(ActionEvent event) {
        // Récupération des valeurs saisies
        String cv_d = cv.getText().trim();
        String lettre_d = lettre.getText().trim();
        String type = type_contrat.getText().trim();

        // Vérification des champs vides
        if (cv_d.isEmpty() || lettre_d.isEmpty() || type.isEmpty()) {
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
}
