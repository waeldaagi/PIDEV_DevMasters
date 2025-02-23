package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.OffreRecrutement;
import services.ServiceOffreRecrutement;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class AfficherCandidatController {

    @FXML
    private ListView<String> list_candidat;

    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    // HashMap to store the relation between the displayed text and the actual ID
    private final HashMap<String, Integer> offreIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur une offre
        list_candidat.setOnMouseClicked(event -> {
            String selectedOffre = list_candidat.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                System.out.println("Offre sélectionnée : " + selectedOffre);

                // Retrieve ID from the stored map
                Integer idOffre = offreIdMap.get(selectedOffre);
                if (idOffre != null) {
                    ouvrirAjoutDemande(idOffre);
                }
            }
        });
    }

    // Méthode pour actualiser la liste des offres de recrutement
    public void refreshList() {
        try {
            List<OffreRecrutement> offres = serviceOffre.recuperer();
            list_candidat.getItems().clear();
            offreIdMap.clear(); // Clear previous stored data

            for (OffreRecrutement offre : offres) {
                String displayText = String.format(
                        "Poste: %s\nSalaire: %sDT\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                        offre.getPoste(),
                        offre.getSalaire(),
                        offre.getDate_pub(),
                        offre.getDate_limite()
                );

                list_candidat.getItems().add(displayText);
                offreIdMap.put(displayText, offre.getId_offre()); // Store ID
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }
    }

    // Méthode pour ouvrir la fenêtre d'ajout de demande avec l'ID sélectionné
    private void ouvrirAjoutDemande(int idOffre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutDemande.fxml"));
            Parent root = loader.load();

            // Passer l'ID de l'offre sélectionnée à AjoutDemandeController
            AjoutDemandeController controller = loader.getController();
            controller.setIdOffre(idOffre);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TextField recherche_offre;



    @FXML
    public void chercher_offre(javafx.event.ActionEvent actionEvent) {
        String query = recherche_offre.getText().toLowerCase(); // Get the search query and convert it to lowercase
        list_candidat.getItems().clear(); // Clear the current list of offers

        try {
            List<OffreRecrutement> offres = serviceOffre.recuperer(); // Retrieve all job offers

            for (OffreRecrutement offre : offres) {
                // Check if the job position (Poste) contains the query
                if (offre.getPoste().toLowerCase().contains(query)) {

                    // Format the offer information to display in the ListView
                    String offerText = String.format(
                            "Poste: %s\nSalaire: %sDT\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                            offre.getPoste(),
                            offre.getSalaire(),
                            offre.getDate_pub(),
                            offre.getDate_limite()
                    );

                    // Add the formatted offer text to the ListView
                    list_candidat.getItems().add(offerText);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }
    }


}
