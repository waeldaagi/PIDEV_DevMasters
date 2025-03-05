package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Conge;
import models.StatutConge;
import services.CongeService;
import utils.BrevoService;

import java.util.List;
import java.util.stream.Collectors;

public class HistoriquecongeRhController {

    @FXML private TextField searchField;
    @FXML private ListView<Conge> congeListView;
    @FXML private Button searchButton, approveButton, declineButton;

    private final CongeService congeService = new CongeService();
    private final BrevoService brevoService = new BrevoService();

    @FXML
    public void initialize() {
        loadCongeList();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCongeList(newValue));
        searchButton.setOnAction(event -> filterCongeList(searchField.getText()));

        approveButton.setOnAction(event -> updateCongeStatus(StatutConge.APPROVED));
        declineButton.setOnAction(event -> updateCongeStatus(StatutConge.DECLINED));

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

    private void updateCongeStatus(StatutConge newStatus) {
        Conge selectedConge = congeListView.getSelectionModel().getSelectedItem();
        if (selectedConge == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un congé à modifier.", Alert.AlertType.WARNING);
            return;
        }

        selectedConge.setStatut(newStatus);
        boolean success = congeService.updateCongeStatus(selectedConge.getIdConge(), newStatus);

        if (success) {
            String userEmail = congeService.getUserEmailById(selectedConge.getIdUser());
            if (userEmail != null) {
                brevoService.sendStatusUpdateEmail(userEmail, selectedConge, newStatus);
            }
            showAlert("Succès", "Le statut du congé a été mis à jour avec succès.", Alert.AlertType.INFORMATION);
            loadCongeList();
        } else {
            showAlert("Erreur", "Échec de la mise à jour du statut.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
