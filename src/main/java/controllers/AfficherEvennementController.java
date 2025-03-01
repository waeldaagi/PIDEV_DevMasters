package controllers;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import javafx.fxml.FXML;
import models.Evennement;
import services.ServiceEvennement;
import java.util.List;
import javafx.event.ActionEvent;
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
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import services.Example;


public class AfficherEvennementController {
    @FXML
    private ListView<HBox> list_event; // The ListView that will display the events

    @FXML
    private TextField id_event_delete;
    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    private final Map<String, Integer> eventMap = new HashMap<>(); // Associer affichage ↔ ID réel
    private Evennement selectedEvennement;// Member variable to hold the selected event
    ObservableList<HBox> eventList = FXCollections.observableArrayList();
    // Vérifier que la liste des événements est bien initialisée
   // List<Evennement> evennements = getEvennements();

    @FXML
    public void initialize() {
        refreshList();

        list_event.setOnMouseClicked(event -> {
            HBox selectedHBox = list_event.getSelectionModel().getSelectedItem();
            if (selectedHBox != null) {
                selectedEvennement = (Evennement) selectedHBox.getUserData();
                if (selectedEvennement != null) {
                    System.out.println("Événement sélectionné : " + selectedEvennement.getNom_event());
                }
            }
        });

    }


    @FXML
    void delete_evennement(ActionEvent event) {
        if (selectedEvennement == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        try {
            serviceEvennement.supprimer(selectedEvennement.getId_event());
            refreshList();
            System.out.println("Événement supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de l'événement : " + e.getMessage());
        }
    }


    @FXML
    void go_update_evennement(ActionEvent event) {
        if (selectedEvennement == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
            Parent root = loader.load();

            UpdateEvennementController controller = loader.getController();
            controller.setIdEvent(selectedEvennement.getId_event()); // Passer l'ID directement

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to update the ListView with events
    public void setListeEvenements(List<Evennement> events) {
        ObservableList<HBox> eventList = FXCollections.observableArrayList();

        for (Evennement ev : events) {
            // Création des labels pour chaque attribut
            Label nomLabel = new Label("Nom: " + ev.getNom_event());
            Label descLabel = new Label("Description: " + ev.getDescription()); // Assure-toi que la méthode est correcte
            Label dateLabel = new Label("Date: " + ev.getDate_event().toString());
            Label lieuLabel = new Label("Lieu: " + ev.getLieu_event());
            Label organisateurLabel = new Label("Organisateur: " + ev.getOrganisateur());
            Label statutLabel = new Label("Statut: " + ev.getStatut());

            // Charger l'image depuis une URL ou un fichier
            ImageView imageView = new ImageView();
            if (ev.getImg_event() != null && !ev.getImg_event().isEmpty()) {
                Image image = new Image("file:" + ev.getImg_event(), 100, 100, true, true);
                imageView.setImage(image);
            }

            // Mise en page avec un VBox pour aligner les infos verticalement
            VBox eventDetails = new VBox(nomLabel, descLabel, dateLabel, lieuLabel, organisateurLabel, statutLabel);
            eventDetails.setSpacing(5);

            // HBox contenant les détails et l’image
            HBox eventHBox = new HBox(imageView, eventDetails);
            eventHBox.setSpacing(10);
            eventHBox.setAlignment(Pos.CENTER_LEFT);
            eventHBox.setUserData(ev);

            eventList.add(eventHBox);
        }

        // Affecter la liste à la ListView
        list_event.setItems(eventList);

        // Set custom cell factory
        list_event.setCellFactory(list -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });
    }



    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer();
            list_event.getItems().clear();

            ObservableList<HBox> eventList = FXCollections.observableArrayList();

            for (Evennement ev : evennements) {
                // Création des labels pour chaque attribut
                Label nomLabel = new Label("Nom: " + ev.getNom_event());
                Label descLabel = new Label("Description: " + ev.getDescription());
                Label dateLabel = new Label("Date: " + ev.getDate_event().toString());
                Label lieuLabel = new Label("Lieu: " + ev.getLieu_event());
                Label organisateurLabel = new Label("Organisateur: " + ev.getOrganisateur());
                Label statutLabel = new Label("Statut: " + ev.getStatut());

                // Charger l'image depuis une URL ou un fichier
                ImageView imageView = new ImageView();
                if (ev.getImg_event() != null && !ev.getImg_event().isEmpty()) {
                    Image image = new Image("file:" + ev.getImg_event(), 100, 100, true, true);
                    imageView.setImage(image);
                }

                // Mise en page avec un VBox pour aligner les infos verticalement
                VBox eventDetails = new VBox(nomLabel, descLabel, dateLabel, lieuLabel, organisateurLabel, statutLabel);
                eventDetails.setSpacing(5);

                // HBox contenant les détails et l’image
                HBox eventHBox = new HBox(imageView, eventDetails);
                eventHBox.setSpacing(10);
                eventHBox.setAlignment(Pos.CENTER_LEFT);
                eventHBox.setUserData(ev);

                eventList.add(eventHBox);
            }

            // Affecter la liste à la ListView
            list_event.setItems(eventList);

            // Set custom cell factory
            list_event.setCellFactory(list -> new ListCell<HBox>() {
                @Override
                protected void updateItem(HBox item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(item);
                    }
                }
            });

        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
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
        if (selectedEvennement == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
            Parent root = loader.load();

            AfficherParticipantController controller = loader.getController();
            controller.refreshListByEventId(selectedEvennement.getId_event()); // Passer l'ID directement

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    void annuler_event(ActionEvent event) {
        if (selectedEvennement == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        try {
            // Annuler l'événement dans la base de données
            serviceEvennement.annulerEvenement(selectedEvennement.getId_event());

            // Récupérer la liste des participants à cet événement
            List<String> participantContacts = serviceEvennement.getParticipantContacts(selectedEvennement.getId_event());

            // Vérifier si la liste des contacts est vide
            if (participantContacts.isEmpty()) {
                System.out.println("Aucun participant trouvé pour cet événement.");
            } else {
                // Envoyer un SMS à chaque participant
                for (String contact : participantContacts) {
                    if (!contact.startsWith("+")) {
                        contact = "+216" + contact;  // Ajoute le code pays si absent
                    }

                    String message = "L'événement '" + selectedEvennement.getNom_event() + "' a été annulé. Nous nous excusons pour le désagrément.";

                    try {
                        // Utiliser la classe Example pour envoyer un SMS
                        Example.sendSms(contact, message);
                        System.out.println("SMS envoyé à : " + contact);
                    } catch (Exception e) {
                        System.err.println("Erreur lors de l'envoi du SMS à " + contact + ": " + e.getMessage());
                    }
                }
            }

            // Mettre à jour la liste ou l'interface utilisateur après l'annulation
            refreshList();
            System.out.println("L'événement a été annulé avec succès et les participants ont été informés par SMS !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'annulation de l'événement : " + e.getMessage());
        }
    }


    @FXML
    void ajout_ev(ActionEvent event) {
        try {
            // Try loading the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvennement.fxml"));
            Parent root = loader.load();

            // Set the scene in the current stage (same window)
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the page.", Alert.AlertType.ERROR);
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
