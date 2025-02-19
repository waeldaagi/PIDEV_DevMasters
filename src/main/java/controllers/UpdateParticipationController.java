package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateParticipationController {

    @FXML
    private TextField contact_p;

    @FXML
    private TextField depart_p;

    @FXML
    private TextField id_p;

    @FXML
    private TextField role_pa;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();

    @FXML
    void update_participation(ActionEvent event) {
        try {
            // Vérification de l'ID
            if (id_p.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de la participation est requis !");
                return;
            }

            int id = Integer.parseInt(id_p.getText().trim());

            // Récupération et nettoyage des champs
            String role = role_pa.getText().trim();
            String departement = depart_p.getText().trim();
            String contact = contact_p.getText().trim();

            // 1. Vérification des champs obligatoires
            if (role.isEmpty() || departement.isEmpty() || contact.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            // 2. Vérification de la longueur minimale
            if (role.length() < 3 || departement.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle et le département doivent contenir au moins 3 caractères !");
                return;
            }

            // 3. Vérification du contact (doit être un numéro à 8 chiffres)
            if (!contact.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le contact doit contenir exactement 8 chiffres !");
                return;
            }

            // Mise à jour de la participation dans la base de données
            serviceParticipation.modifier(id, role, departement, contact);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Participation mise à jour avec succès !");

            // Charger la page "AfficherParticipant.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page AfficherParticipant
            AfficherParticipantController controller = loader.getController();
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
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de chargement de la page AfficherParticipant.fxml");
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
