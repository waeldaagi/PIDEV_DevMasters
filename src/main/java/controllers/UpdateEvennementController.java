package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TextField;
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
            // Récupération des valeurs des champs
            int id = Integer.parseInt(id_e.getText());
            String nom = nom_e.getText();
            String description = description_r.getText();
            String statut = statut_e.getText();

            // Mise à jour de l'événement dans la base de données
            serviceEvennement.modifier(id, nom, description, statut);

            System.out.println("Événement mis à jour avec succès !");

            // Charger la page "AfficherEvennement.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvennement.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page AfficherEvennement
            AfficherEvennementController controller = loader.getController();
            controller.refreshList(); // Appel de la méthode pour recharger la liste

            // Changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID invalide.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur de chargement de la page AfficherEvennement.fxml");
        }
    }
}
