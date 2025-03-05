package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Conge;
import models.StatutConge;
import models.TypeConge;
import services.CongeService;
import utils.JWTUtils;
import utils.SessionManager;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

public class AddCongeController {

    @FXML
    private ComboBox<TypeConge> typeCongeComboBox;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button selectImageButton;

    @FXML
    private Label imagePathLabel;

    @FXML
    private ImageView selectedImageView;

    @FXML
    private Button submitCongeButton;

    private final CongeService congeService = new CongeService();
    private String selectedImagePath = "";

    @FXML
    private void initialize() {
        typeCongeComboBox.getItems().setAll(TypeConge.values());

        selectImageButton.setOnAction(event -> selectImage());
        submitCongeButton.setOnAction(event -> submitConge());
    }

    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            imagePathLabel.setText(selectedFile.getName());

            Image image = new Image(selectedFile.toURI().toString());
            selectedImageView.setImage(image);
        }
    }


    private void submitConge() {
        int idUser = JWTUtils.validateAccessToken(SessionManager.getAccessToken()).getId();
        String userEmail = JWTUtils.validateAccessToken(SessionManager.getAccessToken()).getEmail(); // Retrieve the user email

        TypeConge typeConge = typeCongeComboBox.getValue();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (typeConge == null || dateDebut == null || dateFin == null || selectedImagePath.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        Date dateDebutConverted = java.sql.Date.valueOf(dateDebut);
        Date dateFinConverted = java.sql.Date.valueOf(dateFin);
        Date dateDemande = new Date();

        Conge conge = new Conge(0, idUser, typeConge, dateDebutConverted, dateFinConverted, dateDemande, StatutConge.PENDING, selectedImagePath);

        boolean success = congeService.addConge(conge, selectedImagePath, userEmail);

        if (success) {
            showAlert("Succès", "Votre demande de congé a été soumise.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Échec de l'ajout du congé.", Alert.AlertType.ERROR);
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
