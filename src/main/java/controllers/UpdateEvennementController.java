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
import models.Evennement;
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

    private final ServiceEvennement serviceEvennement = new ServiceEvennement();
    private Integer idEvent; // ID de l'événement à modifier

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
        loadEventDetails(); // Charger les détails de l'événement
    }

    private void loadEventDetails() {
        if (idEvent != null) {
            try {
                Evennement ev = serviceEvennement.getById(idEvent); // Assurez-vous que cette méthode existe
                if (ev != null) {
                    id_e.setText(String.valueOf(ev.getId_event()));
                    id_e.setEditable(false); // Make the ID text field non-editable
                    nom_e.setText(ev.getNom_event());
                    description_r.setText(ev.getDescription()); // Ajout de la description
                    statut_e.setText(ev.getStatut()); // Ajout du statut
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Événement non trouvé !");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des détails : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "ID de l'événement invalide !");
        }
    }

    @FXML
    void update_evennement(ActionEvent event) {
        try {
            String nom = nom_e.getText().trim();
            String description = description_r.getText().trim();
            String statut = statut_e.getText().trim();

            // Vérification des champs obligatoires
            if (nom.isEmpty() || description.isEmpty() || statut.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            // Vérification des valeurs
            if (nom.length() < 3 || description.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et la description doivent contenir au moins 3 caractères !");
                return;
            }

            if (!statut.matches("(?i)Confirmé|Annulé|En attente")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'Confirmé', 'Annulé' ou 'En attente' !");
                return;
            }

            // Mise à jour dans la base de données
            serviceEvennement.modifier(idEvent, nom, description, statut);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès !");

            // Retour à la liste des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvennement.fxml"));
            Parent root = loader.load();

            AfficherEvennementController controller = loader.getController();
            controller.refreshList();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Problème lors de la mise à jour : " + e.getMessage());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de chargement de la page AfficherEvennement.fxml");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

