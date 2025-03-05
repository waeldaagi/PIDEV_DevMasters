package controllers;
// hedhi li mkhobyetha khater mach nestamel feha
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.SQLException;

import services.ServiceDemande;

public class UpdateDemandeController {

    @FXML
    private TextField id_d;

    @FXML
    private TextField type_c;

    private final ServiceDemande serviceDemande = new ServiceDemande();

    // Method to set the ID of the demande
    public void setIdDemande(Integer idDemande) {
        // Set the ID value and make it non-editable
        id_d.setText(String.valueOf(idDemande));
        id_d.setEditable(false); // Make the ID text field non-editable
    }

    @FXML
    void update_demande(ActionEvent event) {
        // Vérification des champs vides
        if (id_d.getText().trim().isEmpty() || type_c.getText().trim().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            // Récupération des valeurs des champs
            int id = Integer.parseInt(id_d.getText().trim());
            String typeContrat = type_c.getText().trim();

            // Mise à jour de la demande dans la base de données
            serviceDemande.modifier(id, typeContrat);
            showAlert("Succès", "Demande mise à jour avec succès !");

            // Chargement de la page AfficherDemande.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDemande.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page et rafraîchir la liste
            AfficherDemandeController controller = loader.getController();
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
            showAlert("Erreur", "Erreur de chargement de la page AfficherDemande.fxml");
        }
    }

    // Method to show an alert with the specified title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
