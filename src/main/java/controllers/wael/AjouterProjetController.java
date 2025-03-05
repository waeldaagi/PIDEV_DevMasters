package controllers.wael;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Equipe;
import models.Projet;
import services.EquipeService;
import services.ProjetService;


import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterProjetController implements Initializable {

    @FXML
    private TextField nomProjetField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private TextField managerField;

    @FXML
    private TextField nomClientField;

    @FXML
    private ComboBox<Equipe> equipeComboBox;

    private EquipeService equipeService = new EquipeService();
    private ProjetService projetService = new ProjetService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Définir la date du jour comme valeur par défaut
        deadlinePicker.setValue(LocalDate.now());
        
        // Configurer le ComboBox
        setupEquipeComboBox();
        
        // Charger les équipes
        chargerEquipes();
    }

    private void setupEquipeComboBox() {
        // Configurer comment les équipes sont affichées dans le ComboBox
        equipeComboBox.setConverter(new StringConverter<Equipe>() {
            @Override
            public String toString(Equipe equipe) {
                if (equipe == null) {
                    return "";
                }
                return equipe.getNomEquipe();
            }

            @Override
            public Equipe fromString(String string) {
                return null; // Non nécessaire pour notre cas
            }
        });

        // Ajouter un prompt text
        equipeComboBox.setPromptText("Sélectionner une équipe");
    }

    private void chargerEquipes() {
        try {
            List<Equipe> equipes = equipeService.getAll(new Equipe());
            equipeComboBox.getItems().clear(); // Nettoyer les items existants
            equipeComboBox.getItems().addAll(equipes);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipes : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterProjet() {
        try {
            // Validation des champs
            if (!validateFields()) {
                return;
            }

            LocalDate localDate = deadlinePicker.getValue();
            if (localDate == null) {
                showAlert("Erreur", "Veuillez sélectionner une date limite");
                return;
            }

            Equipe equipeSelectionnee = equipeComboBox.getValue();
            if (equipeSelectionnee == null) {
                showAlert("Erreur", "Veuillez sélectionner une équipe");
                return;
            }

            // Création du projet
            Projet projet = new Projet(
                nomProjetField.getText(),
                Date.valueOf(localDate),
                managerField.getText(),
                nomClientField.getText(),
                equipeSelectionnee
            );

            // Debug: afficher l'équipe sélectionnée
            System.out.println("Équipe sélectionnée : " + equipeSelectionnee.getNomEquipe() + 
                             " (ID: " + equipeSelectionnee.getIdEquipe() + ")");

            // Ajout dans la base de données
            projetService.ajouter(projet);
            
            // Afficher un message de succès
            showAlert("Succès", "Projet ajouté avec succès");
            
            // Fermer la fenêtre
            closeWindow();
            
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du projet : " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (nomProjetField.getText().isEmpty()) {
            showAlert("Erreur", "Le nom du projet est obligatoire");
            return false;
        }
        
        if (managerField.getText().isEmpty()) {
            showAlert("Erreur", "Le manager est obligatoire");
            return false;
        }
        
        if (nomClientField.getText().isEmpty()) {
            showAlert("Erreur", "Le nom du client est obligatoire");
            return false;
        }
        
        if (equipeComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une équipe");
            return false;
        }

        return true;
    }

    @FXML
    private void retour() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomProjetField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}