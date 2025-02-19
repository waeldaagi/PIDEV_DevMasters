package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

public class AfficherDemandeController {

    @FXML
    private TextField id_d_delete;

    @FXML
    private ListView<String> list_demande;

    private final ServiceDemande serviceDemande = new ServiceDemande();

    public void initialize() {
        refreshList();
    }

    @FXML
    void delete_d(ActionEvent event) {
        try {
            int id = Integer.parseInt(id_d_delete.getText());
            serviceDemande.supprimer(id); // Suppression de l’événement
            refreshList(); // Actualiser la liste après suppression
        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID invalide.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de la demande : " + e.getMessage());
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
        for (Demande re : demandes) {
            list_demande.getItems().add(
                    re.getId_offre() + " | " +
                            re.getId_demande() + " | "
                            + re.getLettre_motivation() + " | "
                            + re.getType_contrat()
            );
        }
    }

    // New method to refresh the list with updated data
    public void refreshList() {
        try {
            List<Demande> demandes = serviceDemande.recuperer(); // Get demands from database
            list_demande.getItems().clear(); // Clear list before filling it again
            for (Demande ev : demandes) {
                list_demande.getItems().add(
                        ev.getId_offre() + " | " +
                                ev.getId_demande() + " | "
                                + ev.getLettre_motivation() + " | "
                                + ev.getType_contrat()
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
    }
}
