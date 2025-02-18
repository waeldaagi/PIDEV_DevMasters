package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
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

    // Declare the service as a class attribute
    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    @FXML
    void ajouter_offre(ActionEvent event) {
        try {
            // Convert DatePicker values to java.sql.Date
            java.sql.Date sqlDate1 = (date_pub.getValue() != null) ? Date.valueOf(date_pub.getValue()) : null;
            java.sql.Date sqlDate2 = (date_limite.getValue() != null) ? Date.valueOf(date_limite.getValue()) : null;

            if (sqlDate1 == null || sqlDate2 == null) {
                System.out.println("Erreur : Date non sélectionnée !");
                return;
            }

            int salaireValue = Integer.parseInt(salaire.getText().trim());
            String posteValue = poste.getText();

            // Create an OffreRecrutement object
            OffreRecrutement offre = new OffreRecrutement(sqlDate1, sqlDate2, salaireValue, posteValue);

            // Add the offer to the database
            serviceOffre.ajouter(offre);
            System.out.println("Offre ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'offre : " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.out.println("Erreur: Veuillez saisir un salaire valide !");
            return;
        }

        try {
            // Load the FXML to display offers
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();

            // Get the controller and update the list of offers
            AfficherOffreRecrutementController ac = loader.getController();
            ac.setListeOffreRecrutement(serviceOffre.recuperer());

            // Switch scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Offres");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur chargement FXML: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur récupération des offres: " + e.getMessage());
        }

    }
}

