package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import services.ServiceEvennement;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateEvennementController {

    @FXML
    private TextField id_e;

    @FXML
    private TextField nom_e;

    @FXML
    private TextField description_r;

    @FXML
    private TextField statut_e;

    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service

    @FXML
    void update_evennement(ActionEvent event) {
        try {
            // Vérification de l'ID
            if (id_e.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de l'événement est requis !");
                return;
            }

            int id = Integer.parseInt(id_e.getText().trim());

            // Récupération et nettoyage des champs
            String nom = nom_e.getText().trim();
            String description = description_r.getText().trim();
            String statut = statut_e.getText().trim();

            // 1. Vérification des champs obligatoires
            if (nom.isEmpty() || description.isEmpty() || statut.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            // 2. Vérification de la longueur minimale
            if (nom.length() < 3 || description.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et la description doivent contenir au moins 3 caractères !");
                return;
            }

            // 3. Vérification du statut (doit être "Confirmé", "Annulé" ou "En attente")
            if (!statut.matches("(?i)Confirmé|Annulé|En attente")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'Confirmé', 'Annulé' ou 'En attente' !");
                return;
            }

            // Mise à jour de l'événement dans la base de données
            serviceEvennement.modifier(id, nom, description, statut);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès !");

            // Charger la page "AfficherEvennement.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvennement.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page AfficherEvennement
            AfficherEvennementController controller = loader.getController();
            controller.refreshList(); // Mise à jour de la liste

            // Changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID doit être un nombre valide !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Problème lors de la mise à jour : " + e.getMessage());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de chargement de la page AfficherEvennement.fxml");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
