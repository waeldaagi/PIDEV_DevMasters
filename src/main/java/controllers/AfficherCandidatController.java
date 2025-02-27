package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import models.OffreRecrutement;
import services.ServiceDemande;
import services.ServiceOffreRecrutement;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AfficherCandidatController {
    @FXML
    private ImageView qr_code;


    @FXML
    private ListView<String> list_candidat;


    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    // HashMap to store the relation between the displayed text and the actual ID
    private final HashMap<String, Integer> offreIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        refreshList();
        generateQRCode();  // Generate QR Code when the page loads
        displayQRCode();
        qr_code.setImage(new Image("file:qr_code.png"));// Display QR Code in ImageView// Charger la liste dès l’affichage de la page
        // Add listener to the search field
        recherche_offre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                refreshList(); // If empty, show all offers
            } else {
                filterOffers(newValue.trim().toLowerCase()); // Otherwise, filter offers
            }
        });
        // Listener pour détecter un clic sur une offre
        list_candidat.setOnMouseClicked(event -> {
            String selectedOffre = list_candidat.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                System.out.println("Offre sélectionnée : " + selectedOffre);

                // Retrieve ID from the stored map
                Integer idOffre = offreIdMap.get(selectedOffre);
                if (idOffre != null) {
                    ouvrirAjoutDemande(idOffre);
                }
            }
        });
    }

    // Méthode pour actualiser la liste des offres de recrutement
    public void refreshList() {
        try {
            List<OffreRecrutement> offres = serviceOffre.recuperer();
            list_candidat.getItems().clear();
            offreIdMap.clear(); // Clear previous stored data

            for (OffreRecrutement offre : offres) {
                String displayText = String.format(
                        "Poste: %s\nSalaire: %sDT\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                        offre.getPoste(),
                        offre.getSalaire(),
                        offre.getDate_pub(),
                        offre.getDate_limite()
                );

                list_candidat.getItems().add(displayText);
                offreIdMap.put(displayText, offre.getId_offre()); // Store ID
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }
    }

    // Méthode pour ouvrir la fenêtre d'ajout de demande avec l'ID sélectionné
    private void ouvrirAjoutDemande(int idOffre) {
        try {
            ServiceDemande serviceDemande = new ServiceDemande();
            int idUser = getCurrentUserId(); // Replace with your method to get the current user's ID

            // Check if the user has already made a request for this offer
            if (serviceDemande.checkDemande(idOffre, idUser)) {
                // If the user has already made a request, show a message or handle it
                showAlert("Vous avez déjà fait une demande pour cette offre.", Alert.AlertType.INFORMATION);
                // Optionally, you can show a dialog to the user instead of a print statement
                return; // Prevent opening the new scene if the request already exists
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutDemande.fxml"));
            Parent root = loader.load();

            // Passer l'ID de l'offre sélectionnée à AjoutDemandeController
            AjoutDemandeController controller = loader.getController();
            controller.setIdOffre(idOffre);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to show an alert
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private int getCurrentUserId() {
        return 1;
    }

    @FXML
    private TextField recherche_offre;


    private void generateQRCode() {
        try {
            // Get the job list and format it
            StringBuilder qrData = new StringBuilder("Liste des Offres:\n");

            List<OffreRecrutement> offres = serviceOffre.recuperer(); // Fetch offers
            for (OffreRecrutement offre : offres) {
                qrData.append("Poste: ").append(offre.getPoste()).append("\n")
                        .append("Salaire: ").append(offre.getSalaire()).append(" DT\n")
                        .append("Date de publication: ").append(offre.getDate_pub()).append("\n")
                        .append("Date limite: ").append(offre.getDate_limite()).append("\n")
                        .append("------------------------------\n");
            }

            // Generate and save the QR code
            String filePath = "qr_code.png";
            BitMatrix matrix = new MultiFormatWriter().encode(qrData.toString(), BarcodeFormat.QR_CODE, 300, 300);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            System.out.println("✅ QR Code generated with job list!");
        } catch (Exception e) {
            System.err.println("❌ Error generating QR Code: " + e.getMessage());
        }
    }

    private void displayQRCode() {
        File file = new File("qr_code.png"); // Ensure the QR Code image exists
        if (file.exists()) {
            qr_code.setImage(new Image(file.toURI().toString())); // Load image into ImageView
        } else {
            System.err.println("QR Code image not found!");
        }
    }

    private void filterOffers(String query) {
        list_candidat.getItems().clear(); // Clear the list before filtering

        try {
            List<OffreRecrutement> offres = serviceOffre.recuperer();

            for (OffreRecrutement offre : offres) {
                if (offre.getPoste().toLowerCase().contains(query)) {
                    String offerText = String.format(
                            "Poste: %s\nSalaire: %sDT\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                            offre.getPoste(),
                            offre.getSalaire(),
                            offre.getDate_pub(),
                            offre.getDate_limite()
                    );
                    list_candidat.getItems().add(offerText);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }
    }




}
