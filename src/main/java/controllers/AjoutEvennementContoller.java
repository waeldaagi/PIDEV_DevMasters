package controllers;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import models.Evennement;
import services.ServiceEvennement;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import javafx.scene.control.DatePicker;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AjoutEvennementContoller {

    @FXML
    private DatePicker date_event;

    @FXML
    private TextField description;

    @FXML
    private TextField lieu_event;

    @FXML
    private TextField nom_event;

    @FXML
    private TextField organisateur;

    @FXML
    private TextField statut;

    @FXML
    void ajouter_evennement(ActionEvent event) {
        String nom = nom_event.getText();
        String desc = description.getText();
        String lieu = lieu_event.getText();
        String org = organisateur.getText();
        String stat = statut.getText();

        // Convert DatePicker value (LocalDate) to java.sql.Date
        java.sql.Date sqlDate = null;
        if (date_event.getValue() != null) {
            sqlDate = Date.valueOf(date_event.getValue());
        } else {
            System.out.println("Erreur : Date non sélectionnée !");
            return; // Stop execution if no date is selected
        }

        // Convert String date to java.sql.Date
       // Date sqlDate = Date.valueOf(date); // Ensure date is in "YYYY-MM-DD" format

        // Create an instance of ServiceEvennement
        ServiceEvennement serviceEvennement = new ServiceEvennement();
        Evennement ev = new Evennement(nom, desc, sqlDate, lieu, org, stat);

        try {
            // Add event to the database
            serviceEvennement.ajouter(ev);
        } catch (SQLException e) {
            System.out.println("Erreur ajout événement: " + e.getMessage());
        }

        try {
            // Load the FXML to display events
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvennement.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the event data
            AfficherEvennementController ac = loader.getController();
             ac.setListeEvenements(serviceEvennement.recuperer());  // Update the list of events

            // Switch scene
            Stage stage = (Stage) nom_event.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Erreur chargement FXML: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur récupération événements: " + e.getMessage());
        }

    }
}

