package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Evennement;
import services.ServiceEvennement;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;

public class AfficherEvennementController {

    @FXML
    private ListView<String> list_event;
    @FXML
    private TextField id_event_delete;
    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page
    }
    @FXML
    void delete_evennement(ActionEvent event) {
        try {
            int id = Integer.parseInt(id_event_delete.getText());
            serviceEvennement.supprimer(id); // Suppression de l’événement
            refreshList(); // Actualiser la liste après suppression
        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID invalide.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    @FXML
    void go_update_evennement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // Method to update the ListView with events
    public void setListeEvenements(List<Evennement> events) {
        list_event.getItems().clear();  // Clear the previous list
        for (Evennement ev : events) {
            list_event.getItems().add(
                    ev.getNom_event() + " | " +
                            ev.getDescription() + " | " +
                            ev.getDate_event() + " | " +
                            ev.getLieu_event() + " | " +
                            ev.getOrganisateur() + " | " +
                            ev.getStatut()
            );

        }
    }
    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer(); // Récupérer les événements depuis la BD
            list_event.getItems().clear(); // Vider la liste avant de la remplir à nouveau
            for (Evennement ev : evennements) {
                list_event.getItems().add(
                        ev.getNom_event() + " | " +
                                ev.getDescription() + " | " +
                                ev.getDate_event() + " | " +
                                ev.getLieu_event() + " | " +
                                ev.getOrganisateur() + " | " +
                                ev.getStatut()
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }
}
