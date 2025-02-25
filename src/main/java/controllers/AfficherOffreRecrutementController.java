package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.OffreRecrutement;
import services.ServiceOffreRecrutement;
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

public class AfficherOffreRecrutementController {

    @FXML
    private ListView<String> list_offre;
    @FXML
    private TextField id_offre;
    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();
    private final Map<String, Integer> offreMap = new HashMap<>(); // Associer affichage ↔ ID réel
    private String selectedOffre; // Variable pour stocker l'offre sélectionnée

    @FXML
    public void initialize() {
        refreshList();
        // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur une offre
        list_offre.setOnMouseClicked(event -> {
            selectedOffre = list_offre.getSelectionModel().getSelectedItem(); // Mettre à jour l'offre sélectionnée
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
        if (idOffre != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOffreRecrutement.fxml"));
                Parent root = loader.load();

                // Passer l'ID à UpdateOffreRecrutementController
                UpdateOffreRecrutementController controller = loader.getController();
                controller.setIdOffre(idOffre);

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

    // Mise à jour de la liste des offres
    public void refreshList() {
        try {
            List<OffreRecrutement> offres = serviceOffre.recuperer();
            list_offre.getItems().clear();
            offreMap.clear(); // Réinitialiser la map

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
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float lineSpacing = 20;
            float yPosition = yStart;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Liste des Offres de Recrutement:");
            contentStream.endText();
            yPosition -= lineSpacing; // Move down

            contentStream.setFont(PDType1Font.HELVETICA, 10);

            for (String offreText : list_offre.getItems()) {
                if (yPosition < margin) { // New page if needed
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    yPosition = yStart;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(offreText.replace("\n", " | ")); // Format text
                contentStream.endText();
                yPosition -= lineSpacing;
            }

            contentStream.close();

            // Save PDF
            document.save("Offres_Recrutement.pdf");
            document.close();
            System.out.println("PDF exporté avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du PDF : " + e.getMessage());
        }


    }

}
