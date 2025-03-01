package controllers;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;

import models.Evennement;
import services.ServiceEvennement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import org.controlsfx.control.Notifications;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class AjoutEvennementContoller {

    @FXML
    private DatePicker date_event;

    @FXML
    private TextField description;

    @FXML
    private TextField lieu_event;

    @FXML
    private TextField nom_event;

    @FXML
    private TextField organisateur;

    @FXML
    private TextField statut;

    @FXML
    private TextField image_event;

    private File selectedImageFile; // Stocke temporairement l'image sélectionnée

    @FXML
    void ajouter_evennement(ActionEvent event) {
        String nom = nom_event.getText().trim();
        String desc = description.getText().trim();
        String lieu = lieu_event.getText().trim();
        String org = organisateur.getText().trim();
        LocalDate date = date_event.getValue();
        String statut = "Confirmé";
        String img_event = "";

        // 1. Contrôle de saisie
        if (nom.isEmpty() || desc.isEmpty() || date == null || lieu.isEmpty() || org.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        if (nom.length() < 3 || desc.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et la description doivent contenir au moins 3 caractères !");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de l'événement ne peut pas être dans le passé !");
            return;
        }

        // Copie de l'image dans htdocs
        if (selectedImageFile != null) {
            img_event = copierImage(selectedImageFile);
            if (img_event.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image !");
                return;
            }
        }

        // Convert DatePicker value (LocalDate) to java.sql.Date
        java.sql.Date sqlDate = Date.valueOf(date);

        // 2. Création et ajout de l'événement
        ServiceEvennement serviceEvennement = new ServiceEvennement();
        Evennement ev = new Evennement(nom, desc, sqlDate, lieu, org, statut, img_event);

        try {
            serviceEvennement.ajouter(ev);

            // Show success notification
            Notifications.create()
                    .title("Succès")
                    .text("L'événement '" + nom + "' a été ajouté avec succès avec le statut 'Confirmé' !")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Problème lors de l'ajout : " + e.getMessage());
            return;
        }

        // 3. Changement de scène pour afficher la liste des événements
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvennement.fxml"));
            Parent root = loader.load();

            AfficherEvennementController ac = loader.getController();
            ac.setListeEvenements(serviceEvennement.recuperer());

            Stage stage = (Stage) nom_event.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur chargement FXML: " + e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur récupération événements: " + e.getMessage());
        }
    }

    @FXML
    void click_img(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedImageFile = file; // Stocke l'image pour la copie
            image_event.setText(file.getName()); // Affiche uniquement le nom du fichier
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Information", "Aucune image sélectionnée.");
        }
    }

    private String copierImage(File file) {
        try {
            File destinationDir = new File("C:/xampp/htdocs");
            if (!destinationDir.exists()) {
                destinationDir.mkdirs(); // Créer le dossier s'il n'existe pas
            }

            String destinationPath = "C:/xampp/htdocs/" + file.getName();
            File destinationFile = new File(destinationPath);
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destinationPath;
        } catch (IOException e) {
            System.err.println("Erreur lors de la copie de l'image : " + e.getMessage());
            return "";
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
