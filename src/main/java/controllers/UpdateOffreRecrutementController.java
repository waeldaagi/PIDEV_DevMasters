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

    @FXML
    void update_offre(ActionEvent event) {
        // Vérification des champs vides
        if (id_offre.getText().trim().isEmpty() || poste.getText().trim().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            // Récupération des valeurs des champs
            int id = Integer.parseInt(id_offre.getText().trim());
            String p = poste.getText().trim();

            // Mise à jour de l'offre dans la base de données
            serviceOffre.modifier(id, p);
            showAlert("Succès", "Offre mise à jour avec succès !");

            // Chargement de la page AfficherOffreRecrutement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page et rafraîchir la liste
            AfficherOffreRecrutementController controller = loader.getController();
            controller.refreshList();

            // Changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID invalide. Veuillez entrer un nombre !");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement de la page AfficherOffreRecrutement.fxml");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
