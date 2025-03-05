package controllers.wael;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Projet;
import services.ProjetService;
import services.PDFService;
import models.GoogleCalendar;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.security.GeneralSecurityException;

public class AfficherProjetsController implements Initializable {

    @FXML
    private TableView<Projet> projetTableView;

    @FXML
    private TableColumn<Projet, Integer> idColumn;

    @FXML
    private TableColumn<Projet, String> nomProjetColumn;

    @FXML
    private TableColumn<Projet, String> deadlineColumn;

    @FXML
    private TableColumn<Projet, String> managerColumn;

    @FXML
    private TableColumn<Projet, String> clientColumn;

    @FXML
    private TableColumn<Projet, String> equipeColumn;

    @FXML
    private TableColumn<Projet, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private ProjetService projetService = new ProjetService();
    private PDFService pdfService = new PDFService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les colonnes
        nomProjetColumn.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
        deadlineColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeadline().toString()));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
        equipeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEquipe().getNomEquipe()));

        // Configurer la colonne d'actions
        setupActionsColumn();

        // Charger les projets
        loadProjets();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("Voir détails");
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button pdfButton = new Button("PDF");
            private final HBox buttons = new HBox(5, viewButton, editButton, deleteButton, pdfButton);

            {
                viewButton.setOnAction(event -> {
                    Projet projet = getTableView().getItems().get(getIndex());
                    handleViewDetailsButtonClick(projet);
                });

                editButton.setOnAction(event -> {
                    Projet projet = getTableView().getItems().get(getIndex());
                    handleModifierButtonClick(projet);
                });

                deleteButton.setOnAction(event -> {
                    Projet projet = getTableView().getItems().get(getIndex());
                    handleSupprimerButtonClick(projet);
                });

                pdfButton.setOnAction(event -> {
                    Projet projet = getTableView().getItems().get(getIndex());
                    handlePDFButtonClick(projet);
                });

                // Style des boutons
                viewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                pdfButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void loadProjets() {
        try {
            List<Projet> projets = projetService.getAll(new Projet());
            projetTableView.getItems().setAll(projets);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des projets : " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchButtonClick() {
        try {
            String searchText = searchField.getText();
            List<Projet> projets = projetService.searchProjects(searchText);
            projetTableView.getItems().setAll(projets);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet/ajouterProjet.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Projet");
            stage.setScene(new Scene(root));
            stage.setOnHiding(event -> loadProjets()); // Recharger les projets après l'ajout
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void handleCalendarButtonClick() {
        try {
            // Add all projects to the Google Calendar
            GoogleCalendar.AjouterProjetsDansCalendrier();

            // Display the complete calendar
            GoogleCalendar.AfficherCalendrierComplet();

            // Show success message
            showAlert("Success", "Calendar has been updated with all projects.");
        } catch (IOException | GeneralSecurityException e) {
            showAlert("Error", "Failed to update calendar: " + e.getMessage());
        }
    }

    private void handleModifierButtonClick(Projet projet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet/modifierProjet.fxml"));
            Parent root = loader.load();

            ModifierProjetController controller = loader.getController();
            controller.setProjet(projet);

            Stage stage = new Stage();
            stage.setTitle("Modifier le Projet");
            stage.setScene(new Scene(root));
            stage.setOnHiding(event -> loadProjets()); // Recharger les projets après la modification
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification : " + e.getMessage());
        }
    }

    private void handleSupprimerButtonClick(Projet projet) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce projet ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    projetService.supprimer(projet.getId_projet());
                    loadProjets(); // Recharger les projets après la suppression
                } catch (SQLException e) {
                    showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleChatbotButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatbot.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Assistant IA - Gestion de Projet");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de l'assistant : " + e.getMessage());
        }
    }

    private void handlePDFButtonClick(Projet projet) {
        try {
            pdfService.generateProjetPDF(projet);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    private void handleViewDetailsButtonClick(Projet projet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsProjet.fxml"));
            Parent root = loader.load();

            DetailsProjetController controller = loader.getController();
            controller.setProjet(projet);

            Stage stage = new Stage();
            stage.setTitle("Détails du Projet - " + projet.getNom_projet());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Pour le débogage
            showAlert("Erreur", "Erreur lors de l'ouverture des détails : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}