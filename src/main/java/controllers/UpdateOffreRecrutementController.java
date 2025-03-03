package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceOffreRecrutement;
import java.io.IOException;
import java.sql.SQLException;
import models.OffreRecrutement;
import javafx.scene.Node;

public class UpdateOffreRecrutementController {

    @FXML
    private TextField id_offre;

    @FXML
    private TextField poste;

    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();
    private Integer idOffre; // Declare idOffre as an instance variable

    // Method to set the ID of the offer
    public void setIdOffre(Integer idOffre) {
        this.idOffre = idOffre; // Assign idOffre
        loadOffreDetails(); // Call method to load offer details
    }

    // Method to load offer details based on the offer ID
    private void loadOffreDetails() {
        if (idOffre != null) {
            try {
                OffreRecrutement offre = serviceOffre.getById(idOffre); // Assuming this method exists in your service
                if (offre != null) {
                    id_offre.setText(String.valueOf(offre.getId_offre())); // Display ID in text field
                    poste.setText(offre.getPoste()); // Display poste in text field
                    id_offre.setEditable(false); // Make the ID text field non-editable
                } else {
                    showAlert("Erreur", "Offre non trouvée !");
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur SQL lors de la récupération des détails : " + e.getMessage());
            }
        }
    }

    // Method to update the offer
    @FXML
    void update_offre(ActionEvent event) {
        // Check if the 'poste' field is empty
        if (poste.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le poste ne peut pas être vide !");
            return;
        }

        try {
            String p = poste.getText().trim(); // Get the 'poste' value
            serviceOffre.modifier(idOffre, p); // Update the offer with the new 'poste' value
            showAlert("Succès", "Offre mise à jour avec succès !");

            // Load the "AfficherOffreRecrutement.fxml" page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();

            // Get the controller of the loaded page
            AfficherOffreRecrutementController controller = loader.getController();
            controller.refreshList(); // Refresh the list after updating

            // Navigate to the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement de la page AfficherOffreRecrutement.fxml");
        }
    }

    // Method to show an alert with the specified title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR); // Create an error alert
        alert.setTitle(title); // Set the title
        alert.setHeaderText(null); // No header
        alert.setContentText(message); // Set the content text
        alert.showAndWait(); // Show the alert and wait for user response
    }
}
