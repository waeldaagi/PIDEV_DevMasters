package controllers.yas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Demande;
import models.User;
import services.yas.ServiceDemande;
import utils.JWTUtils;
import utils.SessionManager;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class AjoutDemandeController {

    @FXML
    private TextField cv;

    @FXML
    private TextField lettre;

    @FXML
    private ChoiceBox<String> type_contrat;

    private int idOffre;

    @FXML
    private AnchorPane anchorPane;

    private final ServiceDemande serviceDemande = new ServiceDemande();

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
        System.out.println("ID de l'offre reçu : " + idOffre);
    }

    @FXML
    public void initialize() {
        URL imageUrl = getClass().getResource("/images/backgound.jpg");
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
        type_contrat.getItems().addAll("CDI", "CDD", "Stage", "Freelance");
    }

    @FXML
    void ajouter_demande(ActionEvent event) {
        String cv_d = cv.getText().trim();
        String lettre_d = lettre.getText().trim();
        String type = type_contrat.getValue();

        if (cv_d.isEmpty() || lettre_d.isEmpty() || type == null || type.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        int idUser = getCurrentUserId();
        if (idUser == -1) {
            showAlert("Erreur", "Vous devez être connecté pour soumettre une demande !");
            return;
        }

        try {
            Path destinationFolder = Path.of("C:/xampp/htdocs");
            Files.createDirectories(destinationFolder);

            Path cvSource = Path.of(cv_d);
            Path lettreSource = Path.of(lettre_d);

            Path cvDestination = destinationFolder.resolve(cvSource.getFileName());
            Path lettreDestination = destinationFolder.resolve(lettreSource.getFileName());

            Files.copy(cvSource, cvDestination, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(lettreSource, lettreDestination, StandardCopyOption.REPLACE_EXISTING);

            cv_d = cvDestination.toString();
            lettre_d = lettreDestination.toString();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la copie des fichiers : " + e.getMessage());
            return;
        }

        Demande demande = new Demande(0, idUser, idOffre, type, cv_d, lettre_d);

        try {
            serviceDemande.ajouter(demande);
            showAlert("Succès", "Demande ajoutée avec succès !");

            Stage stage = (Stage) cv.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur SQL : " + e.getMessage());
        }
    }

    private int getCurrentUserId() {
        String accessToken = SessionManager.getAccessToken();
        if (accessToken != null) {
            User loggedInUser = JWTUtils.validateAccessToken(accessToken);
            return (loggedInUser != null) ? loggedInUser.getId() : -1;
        }
        return -1;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void cv_pdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            cv.setText(selectedFile.getAbsolutePath());
        } else {
            showAlert("Information", "Aucun fichier sélectionné.");
        }
    }

    @FXML
    void lettre_pdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une lettre de motivation");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf"),
                new FileChooser.ExtensionFilter("Fichiers Texte (*.txt)", "*.txt")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            lettre.setText(selectedFile.getAbsolutePath());
        } else {
            showAlert("Information", "Aucun fichier sélectionné.");
        }
    }
}
