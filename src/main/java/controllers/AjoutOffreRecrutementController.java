package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import services.ServiceOffreRecrutement;
import models.OffreRecrutement;

public class AjoutOffreRecrutementController {

    @FXML
    private DatePicker date_limite;

    @FXML
    private DatePicker date_pub;

    @FXML
    private TextField poste;

    @FXML
    private TextField salaire;

    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    // Initialize method to set the current date for date_pub
    public void initialize() {
        // Set the current system date to the date_pub DatePicker
        date_pub.setValue(java.time.LocalDate.now());
    }

    @FXML
    void ajouter_offre(ActionEvent event) {
        // Vérification des champs vides
        if (date_pub.getValue() == null || date_limite.getValue() == null || poste.getText().trim().isEmpty() || salaire.getText().trim().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Validation du salaire
        int salaireValue;
        try {
            salaireValue = Integer.parseInt(salaire.getText().trim());
            if (salaireValue <= 0) {
                showAlert("Erreur", "Le salaire doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir un salaire valide !");
            return;
        }

        try {
            // Conversion des dates et création de l'objet OffreRecrutement
            OffreRecrutement offre = new OffreRecrutement(
                    Date.valueOf(date_pub.getValue()),
                    Date.valueOf(date_limite.getValue()),
                    salaireValue,
                    poste.getText().trim()
            );

            // Ajout de l'offre
            serviceOffre.ajouter(offre);
            showAlert("Succès", "Offre ajoutée avec succès !");

            // Chargement de l'affichage des offres
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();
            AfficherOffreRecrutementController ac = loader.getController();
            ac.setListeOffreRecrutement(serviceOffre.recuperer());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Offres");
            stage.show();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Une erreur SQL est survenue : " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur est survenue lors du chargement de la page : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert;
        if (title.equals("Erreur")) {
            alert = new Alert(AlertType.ERROR);
        } else {
            alert = new Alert(AlertType.INFORMATION);
        }

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
