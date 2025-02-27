package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import services.ServiceOffreRecrutement;
import models.OffreRecrutement;

import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;


public class AjoutOffreRecrutementController {

    @FXML
    private DatePicker date_limite;

    @FXML
    private DatePicker date_pub;

   // @FXML
   // private TextField poste;

    @FXML
    private ChoiceBox<String> poste; // Updated from TextField to ChoiceBox

    @FXML
    private TextField salaire;

    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    // Initialize method to set the current date for date_pub
    public void initialize() {
        // Set the current system date to the date_pub DatePicker
        date_pub.setValue(java.time.LocalDate.now());
        // Populate the ChoiceBox with example job positions

        poste.getItems().addAll(Arrays.asList("Développeur", "Analyste", "Designer", "Chef de projet"));

    }

    @FXML
    void ajouter_offre(ActionEvent event) {
        if (date_pub.getValue() == null || date_limite.getValue() == null || poste.getValue() == null || salaire.getText().trim().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

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
            OffreRecrutement offre = new OffreRecrutement(
                    Date.valueOf(date_pub.getValue()),
                    Date.valueOf(date_limite.getValue()),
                    salaireValue,
                    poste.getValue().trim() // Updated to use ChoiceBox value
            );

            serviceOffre.ajouter(offre);

            // **🔔 Show a Toast Notification**
            Notifications.create()
                    .title("Offre ajoutée")
                    .text("Votre offre de recrutement a été ajoutée avec succès.")
                    .position(Pos.BOTTOM_RIGHT)
                    .showConfirm();

            // Redirect to the offer list
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
