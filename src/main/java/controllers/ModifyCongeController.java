package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Conge;
import models.TypeConge;
import services.CongeService;
import utils.JWTUtils;
import utils.PDFUtils;
import utils.BrevoService;
import utils.SessionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifyCongeController {

    @FXML
    private ComboBox<TypeConge> typeCongeComboBox;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button saveButton, cancelButton;

    private final CongeService congeService = new CongeService();
    private final BrevoService brevoService = new BrevoService();
    private Conge congeToModify;

    @FXML
    public void initialize() {
        typeCongeComboBox.getItems().setAll(TypeConge.values());
        saveButton.setOnAction(event -> updateConge());
        cancelButton.setOnAction(event -> closeWindow());
    }

    public void setCongeData(Conge conge) {
        this.congeToModify = conge;
        typeCongeComboBox.setValue(conge.getTypeConge());
        dateDebutPicker.setValue(convertToLocalDate(conge.getDateDebut()));
        dateFinPicker.setValue(convertToLocalDate(conge.getDateFin()));
    }

    private void updateConge() {
        TypeConge typeConge = typeCongeComboBox.getValue();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (typeConge == null || dateDebut == null || dateFin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        congeToModify.setTypeConge(typeConge);
        congeToModify.setDateDebut(java.sql.Date.valueOf(dateDebut));
        congeToModify.setDateFin(java.sql.Date.valueOf(dateFin));

        boolean success = congeService.updateConge(congeToModify);
        if (success) {
            String userEmail = JWTUtils.validateAccessToken(SessionManager.getAccessToken()).getEmail();
            String pdfUrl = PDFUtils.generatePDF(userEmail, congeToModify, congeToModify.getImage());
            if (pdfUrl != null) {
                brevoService.sendCongeEmail(userEmail, pdfUrl);
            }
            showAlert("Succès", "Congé mis à jour avec succès et email envoyé.", Alert.AlertType.INFORMATION);
            closeWindow();
        } else {
            showAlert("Erreur", "Échec de la mise à jour.", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private LocalDate convertToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
