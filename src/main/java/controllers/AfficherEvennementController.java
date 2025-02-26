package controllers;


import org.apache.pdfbox.pdmodel.font.PDFont;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Evennement;
import services.ServiceEvennement;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class AfficherEvennementController {

    @FXML
    private ListView<String> list_event;
    @FXML
    private TextField id_event_delete;
    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    private final Map<String, Integer> eventMap = new HashMap<>(); // Associer affichage ↔ ID réel
    private String selectedEvent; // Member variable to hold the selected event

    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur un événement
        list_event.setOnMouseClicked(event -> {
            selectedEvent = list_event.getSelectionModel().getSelectedItem(); // Update the selected event
            if (selectedEvent != null) {
                // Affiche l'événement sélectionné dans la console
                System.out.println("Événement sélectionné : " + selectedEvent);
            }
        });
    }

    @FXML
    void delete_evennement(ActionEvent event) {
        String selectedEvent = list_event.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        Integer idEvent = eventMap.get(selectedEvent);
        if (idEvent != null) {
            try {
                serviceEvennement.supprimer(idEvent);
                refreshList();
                System.out.println("Événement supprimé avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la suppression de l'événement : " + e.getMessage());
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
        }
    }

    @FXML
    void go_update_evennement(ActionEvent event) {
        if (selectedEvent == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        Integer idEvent = eventMap.get(selectedEvent);
        if (idEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
                Parent root = loader.load();

                // Passer l'ID à UpdateEvennementController
                UpdateEvennementController controller = loader.getController();
                controller.setIdEvent(idEvent); // Make sure this method exists in UpdateEvennementController

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
        }
    }

    // Method to update the ListView with events
    public void setListeEvenements(List<Evennement> events) {
        list_event.getItems().clear();  // Clear the previous list
        for (Evennement ev : events) {
            list_event.getItems().add(
                    //ev.getId_event() + " | " +
                    "Nom de l'événement : " + ev.getNom_event() + "\n" +
                            "Description : " + ev.getDescription() + "\n" +
                            "Date de l'événement : " + ev.getDate_event() + "\n" +
                            "Lieu de l'événement : " + ev.getLieu_event() + "\n" +
                            "Organisateur : " + ev.getOrganisateur() + "\n" +
                            "Statut : " + ev.getStatut()
            );
        }
    }

    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer();
            list_event.getItems().clear();
            eventMap.clear(); // Réinitialiser la map

            for (Evennement ev : evennements) {
                String eventText =
                        "Nom de l'événement : " + ev.getNom_event() + "\n" +
                        "Description : " + ev.getDescription() + "\n" +
                        "Date de l'événement : " + ev.getDate_event() + "\n" +
                        "Lieu de l'événement : " + ev.getLieu_event() + "\n" +
                        "Organisateur : " + ev.getOrganisateur() + "\n" +
                        "Statut : " + ev.getStatut();

                list_event.getItems().add(eventText);
                eventMap.put(eventText, ev.getId_event()); // Associer affichage ↔ ID réel
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    // Méthode pour extraire l'ID de l'événement depuis la chaîne affichée dans la ListView
    private int extractIdFromString(String eventString) {
        try {
            String[] parts = eventString.split("\\|");
            if (parts.length > 0) {
                return Integer.parseInt(parts[0].trim());
            } else {
                System.err.println("Erreur : La chaîne ne contient pas d'ID.");
                return -1;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Erreur lors de l'extraction de l'ID : " + e.getMessage());
            return -1;
        }
    }

    public void voirListP(ActionEvent actionEvent) {
        if (selectedEvent == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        Integer idEvent = eventMap.get(selectedEvent);
        if (idEvent != null) {
            try {
                // Chargez le FXML pour le contrôleur des participants
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
                Parent root = loader.load();

                // Passer l'ID à AfficherParticipantController
                AfficherParticipantController controller = loader.getController();
                controller.refreshListByEventId(idEvent); // Méthode pour charger les participants par ID

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
        }
    }
    @FXML
    void pdf_exp(ActionEvent event) {
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

            float margin = 50;
            float pageWidth = page.getMediaBox().getWidth();
            float yStart = page.getMediaBox().getHeight() - margin;
            float yPosition = yStart;

            // === Add Image at Top Right ===
            String imagePath = "C:/Users/PC/Documents/GitHub/PIDEV_DevMasters/logo.png"; // Change this path
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
            float imageWidth = 80, imageHeight = 80;
            float imageX = pageWidth - imageWidth - margin;
            float imageY = yStart - imageHeight + 20;
            contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

            // === Title with Dark Blue Color and New Line ===
            contentStream.setNonStrokingColor(0, 0, 139); // Dark Blue (RGB)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16); // Bigger font
            contentStream.newLineAtOffset(margin, yPosition - 50);
            contentStream.showText("Liste des Evennements");
            contentStream.endText();
            contentStream.setNonStrokingColor(0, 0, 0); // Reset to black
            yPosition -= 80; // Move down for spacing

            contentStream.setFont(PDType1Font.HELVETICA, 10);

            // === Table Headers ===
            float[] columnWidths = {100, 150, 60, 100, 80, 50}; // Widths for columns
            String[] headers = {"Nom", "Description", "Date", "Lieu", "Organisateur", "Statut"};

            float xPos = margin;
            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPos, yPosition);
                contentStream.showText(headers[i]);
                contentStream.endText();
                xPos += columnWidths[i]; // Use i to add the correct width
            }
            yPosition -= 20;

            // === Draw Separator Line ===
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(pageWidth - margin, yPosition);
            contentStream.stroke();
            yPosition -= 20;

            // === List Content ===
            for (Evennement events : serviceEvennement.recuperer()) {
                xPos = margin;
                String[] rowData = {
                        events.getNom_event(),
                        events.getDescription(),
                        events.getDate_event().toString(),
                        events.getLieu_event(),
                        events.getOrganisateur(),
                        events.getStatut()
                };

                for (int i = 0; i < rowData.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPos, yPosition);
                    contentStream.showText(rowData[i]);
                    contentStream.endText();
                    xPos += columnWidths[i]; // Use i to add the correct width
                }
                yPosition -= 20;

                if (yPosition < margin) { // New page if needed
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    yPosition = yStart;
                }
            }

            contentStream.close();

            // === Save PDF ===
            document.save("Evennement.pdf");
            document.close();
            System.out.println("PDF exporté avec succès !");
        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de la création du PDF : " + e.getMessage());
        }
    }

    private void generatePdf(int idEvent) throws IOException {
        Evennement evennement = getById(idEvent); // Retrieve the event using the ID

        if (evennement == null) {
            System.err.println("No event found with ID: " + idEvent);
            return; // Exit if no event is found
        }

        // Create a new PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText(); // Start the text content

            // Set the font and size
            PDFont font = PDType1Font.HELVETICA_BOLD; // Example font
            contentStream.setFont(font, 12f); // Use float for font size

            // Set the position for the text
            contentStream.newLineAtOffset(100, 700); // Set position (x, y)

            // Write the text to the PDF
            contentStream.showText("Événement sélectionné : Nom de l'événement : " + evennement.getNom_event());
            contentStream.newLineAtOffset(0, -15); // Move to the next line
            contentStream.showText("Description : " + evennement.getDescription());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Date de l'événement : " + evennement.getDate_event());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Lieu de l'événement : " + evennement.getLieu_event());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Organisateur : " + evennement.getOrganisateur());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Statut : " + evennement.getStatut());

            contentStream.endText(); // End the text content
        }

        // Save the document to the Desktop
        String filePath = "C:\\Users\\PC\\Desktop\\Evenement_" + idEvent + ".pdf";
        document.save(filePath);
        document.close(); // Close the document

        System.out.println("PDF saved to: " + filePath);
    }

    private Evennement getById(int idEvent) {
        try {
            return serviceEvennement.getById(idEvent); // Fetch event from the service
        } catch (SQLException e) {
            System.err.println("Error retrieving event by ID: " + e.getMessage());
            return null; // Return null if not found
        }
    }


}
