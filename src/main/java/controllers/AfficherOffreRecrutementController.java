package controllers;
// awel ma thel ka user wala rh
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import models.OffreRecrutement;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import services.ServiceOffreRecrutement;

import java.net.URL;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;



import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;

import java.util.Date;

import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.ImageView;



public class AfficherOffreRecrutementController {

    @FXML
    private ListView<String> list_offre;
    @FXML
    private TextField id_offre;

    @FXML
    private ChoiceBox<String> poste; // Make sure the type matches your FXML

    @FXML
    private AnchorPane anchorPane;

    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();
    private final Map<String, Integer> offreMap = new HashMap<>(); // Associer affichage ↔ ID réel
    private String selectedOffre; // Variable pour stocker l'offre sélectionnée

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
        refreshList(); // Load offers

        // Populate the ChoiceBox with job titles
        poste.getItems().addAll("Développeur", "Designer", "Ingénieur", "Manager", "Analyste");

        // Listener for the ListView to update the ChoiceBox based on selection
        list_offre.setOnMouseClicked(event -> {
            selectedOffre = list_offre.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                System.out.println("Offre sélectionnée : " + selectedOffre);
            }
        });
    }

    @FXML
    void delete_offre(ActionEvent event) {
        if (selectedOffre == null) {
            System.err.println("Erreur : Aucune offre sélectionnée.");
            return;
        }

        Integer idOffre = offreMap.get(selectedOffre);
        if (idOffre != null) {
            try {
                serviceOffre.supprimer(idOffre);
                refreshList();
                System.out.println("Offre supprimée avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la suppression de l'offre : " + e.getMessage());
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'offre sélectionnée.");
        }
    }

    @FXML
    void go_update_offre(ActionEvent event) {
        if (selectedOffre == null) {
            System.err.println("Erreur : Aucune offre sélectionnée.");
            return;
        }

        Integer idOffre = offreMap.get(selectedOffre);
        String selectedPoste = poste.getValue(); // Get selected poste from ChoiceBox

        if (idOffre != null && selectedPoste != null) {
            try {
                // Update the offer with the selected poste
                serviceOffre.modifier(idOffre, selectedPoste);  // Calling the modifier method

                // Go to the UpdateOffreRecrutement screen if needed (or handle updates directly here)
                System.out.println("Offre modifiée avec succès!");

                // Optionally, refresh the list to reflect changes
                refreshList();

            } catch (SQLException e) {
                System.err.println("Erreur lors de la mise à jour de l'offre : " + e.getMessage());
            }
        } else {
            System.err.println("Erreur : ID ou Poste introuvable pour l'offre sélectionnée.");
        }
    }


    // Mise à jour de la liste des offres
    public void refreshList() {
        try {
            List<OffreRecrutement> offres = serviceOffre.recuperer();
            list_offre.getItems().clear();
            offreMap.clear(); // Réinitialiser la map

            Date currentDate = new Date(); // Current date

            for (OffreRecrutement offre : offres) {
                // Ensure that date_limite is a Date type
                Date dateLimite = offre.getDate_limite();

                // Format the offer's display text
                String offreText = String.format(
                        "Poste: %s\nSalaire: %s\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                        offre.getPoste(),
                        (double) offre.getSalaire(),
                        offre.getDate_pub(),
                        offre.getDate_limite()
                );

                // If the deadline has passed, prepend "Offre Hors Service"
                if (dateLimite.before(currentDate)) {
                    offreText = "       !!  !!         Offre Hors Service        !!   !!\n" + offreText; // Prepend message if expired
                }

                list_offre.getItems().add(offreText);
                offreMap.put(offreText, offre.getId_offre()); // Associer affichage ↔ ID réel
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }
    }
    public void setListeOffreRecrutement(List<OffreRecrutement> offres) {
        list_offre.getItems().clear();
        offreMap.clear();

        for (OffreRecrutement offre : offres) {
            String offreText = String.format(
                    "Poste: %s\nSalaire: %s\nDate de publication: %s\nDate limite: %s\n-------------------------------",
                    offre.getPoste(),
                    offre.getSalaire(),
                    offre.getDate_pub(),
                    offre.getDate_limite()
            );



            list_offre.getItems().add(offreText);
            offreMap.put(offreText, offre.getId_offre()); // Associer affichage ↔ ID réel
        }
    }
    @FXML
    void demande_go(ActionEvent event) {
        if (selectedOffre == null) {
            System.err.println("Erreur : Aucune offre sélectionnée.");
            return;
        }

        Integer idOffre = offreMap.get(selectedOffre);
        if (idOffre != null) {
            try {
                // Load the AfficherDemande.fxml page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDemande.fxml"));
                Parent root = loader.load();

                // Pass the ID of the selected offer to AfficherDemandeController
                AfficherDemandeController controller = loader.getController();
                controller.setSelectedOffre(idOffre); // Passing the selected offer's ID

                // Navigate to the AfficherDemande page
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'offre sélectionnée.");
        }
    }

    @FXML
    void exporte_pdf(ActionEvent event) {
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
            String imagePath = "C:/Users/Yass/projet_pidev/log.PNG"; // Change this path
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
            float imageWidth = 80, imageHeight = 80;
            float imageX = pageWidth - imageWidth - margin;
            float imageY = yStart - imageHeight;
            contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

            // === Title with Dark Blue Color and New Line ===
            contentStream.setNonStrokingColor(0, 0, 139); // Dark Blue (RGB)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16); // Bigger font
            contentStream.newLineAtOffset(margin, yPosition - 50);
            contentStream.showText("Liste des Offres de Recrutement");
            contentStream.endText();
            contentStream.setNonStrokingColor(0, 0, 0); // Reset to black
            yPosition -= 80; // Move down for spacing

            contentStream.setFont(PDType1Font.HELVETICA, 10);

            // === Table Headers ===
            float[] columnWidths = {150, 80, 100, 100}; // Widths for columns
            String[] headers = {"Poste", "Salaire", "Date Pub.", "Date Limite"};

            float xPos = margin;
            for (String header : headers) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPos, yPosition);
                contentStream.showText(header);
                contentStream.endText();
                xPos += columnWidths[headers.length - 1];
            }
            yPosition -= 20;

            // === Draw Separator Line ===
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(pageWidth - margin, yPosition);
            contentStream.stroke();
            yPosition -= 20;

            // === List Content ===
            for (OffreRecrutement offre : serviceOffre.recuperer()) {
                xPos = margin;
                String[] rowData = {
                        offre.getPoste(),
                        String.valueOf(offre.getSalaire()),
                        offre.getDate_pub().toString(),
                        offre.getDate_limite().toString()
                };

                for (String data : rowData) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPos, yPosition);
                    contentStream.showText(data);
                    contentStream.endText();
                    xPos += columnWidths[rowData.length - 1];
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
            document.save("Offres_Recrutement.pdf");
            document.close();
            System.out.println("PDF exporté avec succès !");
        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de la création du PDF : " + e.getMessage());
        }
    }

    @FXML
    void go_ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutOffreRecrutement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






}
