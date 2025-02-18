package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.OffreRecrutement;
import services.ServiceOffreRecrutement;
import java.util.List;
import models.OffreRecrutement;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;

public class AfficherOffreRecrutementController {

    @FXML
    private ListView<String> list_offre;
    @FXML
    private TextField id_offre;
    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page
    }

    @FXML
    void delete_offre(ActionEvent event) {
        try {
            int id = Integer.parseInt(id_offre.getText());
            serviceOffre.supprimer(id); // Suppression de l’événement
            refreshList(); // Actualiser la liste après suppression
        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID invalide.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de l'événement : " + e.getMessage());
        }

    }

    @FXML
    void go_update_offre(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOffreRecrutement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Method to update the ListView with events
    public void setListeOffreRecrutement(List<OffreRecrutement> offres) {
        list_offre.getItems().clear();  // Clear the previous list
        for (OffreRecrutement re : offres) {
            list_offre.getItems().add(re.getPoste() + " | "
                    + re.getDate_limite() + " | "
                    + re.getDate_pub() + " | "
                    + re.getSalaire() + "DT | " );

        }


    }
    // New method to refresh the list with updated data
    public void refreshList() {
        try {
            List<OffreRecrutement> evennements = serviceOffre.recuperer(); // Récupérer les événements depuis la BD
            list_offre.getItems().clear(); // Vider la liste avant de la remplir à nouveau
            for (OffreRecrutement ev : evennements) {
                list_offre.getItems().add(
                        ev.getPoste() + " | " +
                                ev.getSalaire() + " | " +
                                ev.getDate_pub() + " | " +
                                ev.getDate_limite()

                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }



}

