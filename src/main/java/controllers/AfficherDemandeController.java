package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import models.Demande;
import services.ServiceDemande;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import java.awt.Desktop;
import java.io.File;

import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.ImageView;

public class AfficherDemandeController {

    @FXML
    private TextField id_d_delete;

    @FXML
    private ListView<Demande> list_demande;

    @FXML
    private AnchorPane anchorPane;

    private final ServiceDemande serviceDemande = new ServiceDemande();
    private int selectedOffreId;
    private Demande selectedDemand;

    public void setSelectedOffre(int idOffre) {
        this.selectedOffreId = idOffre;
        refreshList();
    }

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
        list_demande.setOnMouseClicked((MouseEvent event) -> {
            selectedDemand = list_demande.getSelectionModel().getSelectedItem();
            if (selectedDemand != null) {
                System.out.println("Demande sélectionnée : " + selectedDemand);
            }
        });
    }

    @FXML
    void delete_d(ActionEvent event) {
        if (selectedDemand == null) {
            System.err.println("Erreur : Aucune demande sélectionnée.");
            return;
        }

        try {
            // Directly use selectedDemand.getId_demande()
            serviceDemande.supprimer(selectedDemand.getId_demande());
            refreshList(); // Refresh the list to reflect the changes
            System.out.println("Demande supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de la demande : " + e.getMessage());
        }
    }

    @FXML
    void update_d(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateDemande.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshList() {
        try {
            List<Demande> demandes = serviceDemande.recupererDemandeParOffre(selectedOffreId);
            list_demande.getItems().clear();

            list_demande.setCellFactory(lv -> new ListCell<Demande>() {
                @Override
                protected void updateItem(Demande demande, boolean empty) {
                    super.updateItem(demande, empty);
                    if (empty || demande == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Text information
                        Text demandeText = new Text(
                                "Type de contrat: " + demande.getType_contrat()
                        );

                        // Lettre de motivation button
                        Button openLettreButton = new Button("📄 Ouvrir Lettre");
                        openLettreButton.setOnAction(e -> openPDF(demande.getLettre_motivation()));

                        // CV button
                        Button openCvButton = new Button("📄 Ouvrir CV");
                        openCvButton.setOnAction(e -> openPDF(demande.getCv()));

                        // Layout for buttons and text
                        HBox cellContent = new HBox(10, demandeText, openLettreButton, openCvButton);
                        cellContent.setAlignment(Pos.CENTER_LEFT);
                        setGraphic(cellContent);
                    }
                }
            });

            list_demande.getItems().addAll(demandes);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
    }

    private void openPDF(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'ouverture du fichier PDF : " + e.getMessage());
                }
            } else {
                System.err.println("Le fichier PDF n'existe pas : " + filePath);
            }
        } else {
            System.err.println("Aucun fichier PDF spécifié.");
        }
    }

    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
