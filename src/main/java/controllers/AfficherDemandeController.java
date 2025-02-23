package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import models.Demande;
import services.ServiceDemande;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AfficherDemandeController {

    @FXML
    private TextField id_d_delete;

    @FXML
    private ListView<String> list_demande;

    private final ServiceDemande serviceDemande = new ServiceDemande();

    private int selectedOffreId;  // Variable to store the selected offer ID
    private String selectedDemand; // Variable to store the selected demand

    private final Map<String, Integer> demandeMap = new HashMap<>(); // To associate displayed text with demand ID

    // Method to set the selected offer's ID
    public void setSelectedOffre(int idOffre) {
        this.selectedOffreId = idOffre;
        refreshList();  // Refresh the list after setting the ID
    }

    public void initialize() {
        refreshList();

        // Listener to detect a click on a demand
        list_demande.setOnMouseClicked((MouseEvent event) -> {
            selectedDemand = list_demande.getSelectionModel().getSelectedItem(); // Set the selected demand
            if (selectedDemand != null) {
                System.out.println("Demande sélectionnée : " + selectedDemand);
            }
        });
    }

    @FXML
    void delete_d(ActionEvent event) {
        if (selectedDemand == null) {
            System.err.println("Erreur : Aucune demande sélectionnée.");
            return;
        }

        Integer idDemand = demandeMap.get(selectedDemand); // Get the ID of the selected demand
        if (idDemand != null) {
            try {
                serviceDemande.supprimer(idDemand); // Delete the demand from the database
                refreshList(); // Refresh the list after deletion
                System.out.println("Demande supprimée avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la suppression de la demande : " + e.getMessage());
            }
        } else {
            System.err.println("Erreur : ID introuvable pour la demande sélectionnée.");
        }
    }

    @FXML
    void update_d(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateDemande.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update the ListView with demands
    public void setList_demande(List<Demande> demandes) {
        list_demande.getItems().clear(); // Clear previous list
        demandeMap.clear(); // Clear previous map

        for (Demande re : demandes) {
            String demandeText = String.format(
                    "Lettre de motivation: %-30s\n" +   // Motivation letter on a new line, maximum 30 characters width
                            "Type contrat: %-20s\n" +   // Contract type (type_contrat)
                            "Cv: %-20s\n" +   // CV (cv)
                            "-------------------------------", // Separator line
                    re.getLettre_motivation(),
                    re.getType_contrat(),
                    re.getCv()
            );

            list_demande.getItems().add(demandeText);
            demandeMap.put(demandeText, re.getId_demande()); // Associate displayed text with demand ID
        }
    }

    // Method to refresh the list with updated data
    public void refreshList() {
        try {
            // Retrieve demands for the selected offer
            List<Demande> demandes = serviceDemande.recupererDemandeParOffre(selectedOffreId);
            list_demande.getItems().clear(); // Clear list before filling it again
            demandeMap.clear(); // Clear the map

            for (Demande ev : demandes) {
                String demandeText = String.format(
                        "Lettre de motivation: %-30s\n" +   // Motivation letter on a new line, maximum 30 characters width
                                "Type contrat: %-20s\n" +   // Contract type (type_contrat)
                                "Cv: %-20s\n" +   // CV (cv)
                                "-------------------------------", // Separator line
                        ev.getLettre_motivation(),
                        ev.getType_contrat(),
                        ev.getCv()
                );

                list_demande.getItems().add(demandeText);
                demandeMap.put(demandeText, ev.getId_demande()); // Associate displayed text with demand ID
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
    }
}
