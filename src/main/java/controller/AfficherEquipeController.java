package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Equipe;
import service.EquipeService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherEquipeController implements Initializable {

    @FXML
    private TableView<Equipe> listeEquipes;

    @FXML
    private TableColumn<Equipe, Integer> idColumn;

    @FXML
    private TableColumn<Equipe, String> nomEquipeColumn;

    @FXML
    private TableColumn<Equipe, Integer> nbrEmployeeColumn;

    @FXML
    private TableColumn<Equipe, String> nomTeqleadColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button ajouterEquipeButton;

    @FXML
    private Button modifierEquipeButton;

    @FXML
    private Button supprimerEquipeButton;

    private EquipeService equipeService = new EquipeService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idEquipe"));
        nomEquipeColumn.setCellValueFactory(new PropertyValueFactory<>("nomEquipe"));
        nbrEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("nbrEmployee"));
        nomTeqleadColumn.setCellValueFactory(new PropertyValueFactory<>("nomTeqlead"));

        // Load teams into the TableView
        try {
            loadTeams();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipes : " + e.getMessage());
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
            showAlert("Erreur", "Erreur lors de la recherche des équipes : " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadTeamsButtonClick() {
        try {
            loadTeams();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipes : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterEquipe() {
        try {
            // Load the "ajouterEquipe.fxml" file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterEquipe.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ajouterEquipeButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue : " + e.getMessage());
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
                showAlert("Erreur", "Erreur lors du chargement de la vue : " + e.getMessage());
            }
        } else {
            showAlert("Avertissement", "Veuillez sélectionner une équipe à modifier.");
        }
    }

    @FXML
    private void handleSupprimerButtonClick() {
        // Get the selected team
        Equipe selectedEquipe = listeEquipes.getSelectionModel().getSelectedItem();

        if (selectedEquipe != null) {
            try {
                // Delete the team from the database
                equipeService.supprimer(selectedEquipe.getIdEquipe());

                // Reload the teams in the TableView
                loadTeams();
                showAlert("Succès", "Équipe supprimée avec succès.");
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression de l'équipe : " + e.getMessage());
            }
        } else {
            showAlert("Avertissement", "Veuillez sélectionner une équipe à supprimer.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}