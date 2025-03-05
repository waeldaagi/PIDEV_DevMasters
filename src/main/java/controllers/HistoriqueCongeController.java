package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Conge;
import models.StatutConge;
import services.CongeService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HistoriqueCongeController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Conge> congeListView;

    @FXML
    private Button searchButton, updateButton, deleteButton;

    private final CongeService congeService = new CongeService();

    @FXML
    public void initialize() {
        loadCongeList();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCongeList(newValue));
        searchButton.setOnAction(event -> filterCongeList(searchField.getText()));

        updateButton.setOnAction(event -> openModifyWindow());
        deleteButton.setOnAction(event -> deleteSelectedConge());

        congeListView.setCellFactory(param -> new ListCell<>() {
            private final HBox content = new HBox(10);
            private final ImageView imageView = new ImageView();
            private final VBox detailsBox = new VBox(3);
            private final Label typeLabel = new Label();
            private final Label dateDebutLabel = new Label();
            private final Label dateFinLabel = new Label();
            private final Label dateDemandeLabel = new Label();
            private final Label statutLabel = new Label();

            {
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                detailsBox.getChildren().addAll(typeLabel, dateDebutLabel, dateFinLabel, dateDemandeLabel, statutLabel);
                content.getChildren().addAll(imageView, detailsBox);
            }

            @Override
            protected void updateItem(Conge conge, boolean empty) {
                super.updateItem(conge, empty);
                if (empty || conge == null) {
                    setGraphic(null);
                } else {
                    typeLabel.setText("Type: " + conge.getTypeConge().name());
                    dateDebutLabel.setText("Début: " + conge.getDateDebut().toString());
                    dateFinLabel.setText("Fin: " + conge.getDateFin().toString());
                    dateDemandeLabel.setText("Demandé: " + conge.getDateDemande().toString());
                    statutLabel.setText("Statut: " + conge.getStatut().name());

                    // ✅ Load Cloudinary Image
                    Image image = new Image(conge.getImage(), true);
                    imageView.setImage(image);

                    setGraphic(content);
                }
            }
        });
    }

    private void loadCongeList() {
        List<Conge> conges = congeService.getAllConges();
        congeListView.getItems().setAll(conges);
    }

    private void filterCongeList(String searchText) {
        List<Conge> filteredList = congeService.getAllConges().stream()
                .filter(conge ->
                        conge.getTypeConge().name().toLowerCase().contains(searchText.toLowerCase()) ||
                                conge.getDateDebut().toString().contains(searchText) ||
                                conge.getDateFin().toString().contains(searchText) ||
                                conge.getDateDemande().toString().contains(searchText) ||
                                conge.getStatut().name().toLowerCase().contains(searchText.toLowerCase())
                )
                .collect(Collectors.toList());

        congeListView.getItems().setAll(filteredList);
    }


    private void openModifyWindow() {
        Conge selectedConge = congeListView.getSelectionModel().getSelectedItem();
        if (selectedConge == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un congé à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/employe/ModifyConge.fxml"));
            Pane root = loader.load();

            ModifyCongeController controller = loader.getController();
            controller.setCongeData(selectedConge);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier le congé");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadCongeList();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.", Alert.AlertType.ERROR);
        }
    }

    private void deleteSelectedConge() {
        Conge selectedConge = congeListView.getSelectionModel().getSelectedItem();
        if (selectedConge == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un congé à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedConge.getStatut() != StatutConge.PENDING) {
            showAlert("Action refusée", "Vous ne pouvez supprimer que les congés en attente.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Voulez-vous vraiment supprimer ce congé ?");
        confirmation.setContentText("Cette action est irréversible.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = congeService.deleteConge(selectedConge.getIdConge());
                if (success) {
                    showAlert("Succès", "Le congé a été supprimé.", Alert.AlertType.INFORMATION);
                    loadCongeList();
                } else {
                    showAlert("Erreur", "Échec de la suppression.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
