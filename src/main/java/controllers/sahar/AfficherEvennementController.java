package controllers.sahar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Evennement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import services.sahar.Example;
import services.sahar.ServiceEvennement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///////////////////// affichage pour l'admin //////////
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/sahar/UpdateEvennement.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/sahar/AfficherParticipant.fxml"));
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700);
                    contentStream.showText("Liste des Événements");
                    contentStream.endText();
                }

                document.save(file.getAbsolutePath());
                System.out.println("PDF exporté avec succès à : " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du PDF : " + e.getMessage());
            }
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

                    String message = "L'événement '" + selectedEvennement.getNom_event() + "'  a été annulé. Nous nous excusons pour les désagréments causés.";

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/sahar/AjoutEvennement.fxml"));
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
