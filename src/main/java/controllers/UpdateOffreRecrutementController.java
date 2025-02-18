package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            // Récupération des valeurs des champs
            int id = Integer.parseInt(id_offre.getText());
            String p = poste.getText();


            // Mise à jour de l'événement dans la base de données
            serviceOffre.modifier(id, p);

            System.out.println(" offre mis à jour avec succès !");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page AfficherEvennement
            AfficherOffreRecrutementController controller = loader.getController();
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
