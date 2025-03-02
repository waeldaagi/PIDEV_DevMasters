package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import models.OffreRecrutement;
import services.OpenRouterService;
import services.ServiceDemande;
import services.ServiceOffreRecrutement;

import java.net.URL;
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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AfficherCandidatController {
    @FXML
    private ImageView qr_code;


    @FXML
    private ListView<String> list_candidat;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private VBox chatVBox;



    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    // HashMap to store the relation between the displayed text and the actual ID
    private final HashMap<String, Integer> offreIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        URL imageUrl = getClass().getResource("/backgound.jpg");
        if (imageUrl != null) {
            Image backgroundImage = new Image(imageUrl.toExternalForm());
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            anchorPane.setBackground(new Background(bgImage));
        } else {
            System.err.println("Erreur : Image de fond introuvable !");
        }
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

            LocalDate today = LocalDate.now(); // Get today's date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Format dates

            for (OffreRecrutement offre : offres) {
                if (offre.getDate_limite() == null) {
                    System.err.println("Offre avec ID " + offre.getId_offre() + " a une date limite null.");
                    continue; // Skip this offer
                }

                // Convert java.util.Date to LocalDate
                Date dateLimiteUtil = offre.getDate_limite();
                LocalDate dateLimite = Instant.ofEpochMilli(dateLimiteUtil.getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                if (!dateLimite.isBefore(today)) { // Check if offer is still valid
                    String displayText = String.format(
                            "Poste: %s\nSalaire: %.2f DT\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                            offre.getPoste(),
                            (double) offre.getSalaire(),
                            (offre.getDate_pub() != null) ? Instant.ofEpochMilli(offre.getDate_pub().getTime())
                                    .atZone(ZoneId.systemDefault()).toLocalDate().format(formatter) : "N/A",
                            dateLimite.format(formatter) // Convert LocalDate to formatted String
                    );

                    list_candidat.getItems().add(displayText);
                    offreIdMap.put(displayText, offre.getId_offre()); // Store ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
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

    @FXML
    private void handleSend(ActionEvent event) {


        String userInput = inputField.getText();

        if (userInput.trim().isEmpty()) {
            return;
        }

        // Display user input in the chat (as a chat bubble)
        Label userMessage = new Label("You: " + userInput);
        userMessage.getStyleClass().add("chat-bubble");
        userMessage.getStyleClass().add("user-bubble");
        chatVBox.getChildren().add(userMessage);  // Add user message to VBox

        // Send message to OpenRouter API
        new Thread(() -> {
            try {
                OpenRouterService openRouterService = new OpenRouterService();
                String botResponse = openRouterService.sendMessageToOpenRouter(userInput);

                // Debugging: Log raw bot response
                System.out.println("Bot response: " + botResponse);

                // Clean and sanitize the bot's response
                String cleanedResponse = cleanUpResponse(botResponse);
                String sanitizedResponse = sanitizeResponse(cleanedResponse);

                // Create a label for the bot's response and add it to the VBox
                javafx.application.Platform.runLater(() -> {
                    Label botMessage = new Label("Bot: " + sanitizedResponse);
                    botMessage.getStyleClass().add("chat-bubble");
                    botMessage.getStyleClass().add("bot-bubble");
                    chatVBox.getChildren().add(botMessage);
                });
            } catch (IOException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    Label errorMessage = new Label("Bot: Error communicating with AI.");
                    chatVBox.getChildren().add(errorMessage);
                });
            }
        }).start();

        // Clear input field
        inputField.clear();
    }

    // Clean up the response (remove unwanted non-ASCII characters)
    private String cleanUpResponse(String response) {
        if (response != null) {
            response = response.replaceAll("[^\\x00-\\x7F]", "");  // Only keep ASCII characters
        }
        return response;
    }

    // Sanitize the response (remove unnecessary spaces and newlines)
    private String sanitizeResponse(String response) {
        if (response != null) {
            response = response.replaceAll("\\s+", " ").trim();  // Replace multiple spaces with a single space
            response = response.replace("\n", " ");  // Remove any newlines
        }
        return response;
    }


}
