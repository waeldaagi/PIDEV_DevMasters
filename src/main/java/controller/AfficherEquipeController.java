package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Equipe;
import service.EquipeServise;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherEquipeController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Equipe> listeEquipes;


    @FXML
    private Button ajouterEquipeButton;

    @FXML
    private Button modifierEquipeButton;

    private final EquipeServise equipeService = new EquipeServise();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadTeams();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTeams() throws SQLException {
        List<Equipe> equipes = equipeService.getAll(new Equipe());
        ObservableList<Equipe> observableList = FXCollections.observableArrayList(equipes);
        listeEquipes.setItems(observableList);
    }

    @FXML
    private void handleSearchButtonClick() {
        try {
            String searchText = searchField.getText();
            List<Equipe> equipes = equipeService.searchTeams(searchText);
            ObservableList<Equipe> observableList = FXCollections.observableArrayList(equipes);
            listeEquipes.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadTeamsButtonClick() {
        try {
            loadTeams();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterEquipe() {
        try {
            // Load the "ajoutEquipe.fxml" file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutEquipe.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ajouterEquipeButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifierButtonClick() {
        // Get the selected team
        Equipe selectedEquipe = listeEquipes.getSelectionModel().getSelectedItem();

        if (selectedEquipe != null) {
            try {
                // Load the "modifierEquipe.fxml" file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierEquipe.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected team
                ModifierEquipeController controller = loader.getController();
                controller.setEquipe(selectedEquipe);

                // Create a new stage (window)
                Stage stage = new Stage();
                stage.setTitle("Modifier l'Équipe");
                stage.setScene(new Scene(root));
                stage.show(); // Show the new window
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucune équipe sélectionnée.");
        }
    }
    @FXML
    private void handleSupprimerButtonClick(ActionEvent event) {
        Equipe selectedEquipe = listeEquipes.getSelectionModel().getSelectedItem();

        if (selectedEquipe == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune équipe sélectionnée", "Veuillez sélectionner une équipe à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'équipe ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer l'équipe : " + selectedEquipe.getNomEquipe() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Appeler votre service pour supprimer l'équipe
                EquipeServise equipeService = new EquipeServise(); // Assurez-vous d'instancier votre service
                equipeService.supprimer(selectedEquipe.getIdEquipe()); // Supprimer par ID

                // Rafraîchir la liste après la suppression
                listeEquipes.getItems().remove(selectedEquipe); // Mettez à jour la ListView
                // ou bien :
                // List<Equipe> equipes = equipeService.getAll(new Equipe()); // Récupérer la liste mise à jour
                // listeEquipes.setItems(FXCollections.observableArrayList(equipes)); // Mettre à jour la ListView

                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'équipe a été supprimée avec succès.");

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'équipe : " + e.getMessage());
                e.printStackTrace(); // Important pour le débogage
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}