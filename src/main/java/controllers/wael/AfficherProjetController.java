package controllers.wael;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Projet;
import services.ProjetService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherProjetController implements Initializable {

    @FXML
    private TableView<Projet> listeProjets;

    @FXML
    private TableColumn<Projet, Integer> idColumn;

    @FXML
    private TableColumn<Projet, String> nomProjetColumn;

    @FXML
    private TableColumn<Projet, java.sql.Date> deadlineColumn;

    @FXML
    private TableColumn<Projet, String> managerColumn;

    @FXML
    private TableColumn<Projet, String> nomClientColumn;

    @FXML
    private TableColumn<Projet, String> equipeColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button ajouterProjetButton;

    @FXML
    private Button modifierProjetButton;

    @FXML
    private Button supprimerProjetButton;

    private ProjetService projetService = new ProjetService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurer les colonnes du TableView
        nomProjetColumn.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("Deadline"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
        equipeColumn.setCellValueFactory(new PropertyValueFactory<>("equipe.nomEquipe"));

        // Charger la liste des projets
        try {
            loadProjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProjects() throws SQLException {
        List<Projet> projets = projetService.getAll(new Projet());
        ObservableList<Projet> observableList = FXCollections.observableArrayList(projets);
        listeProjets.setItems(observableList);
    }

    @FXML
    private void handleSearchButtonClick() {
        try {
            String searchText = searchField.getText();
            List<Projet> projets = projetService.searchProjects(searchText);
            ObservableList<Projet> observableList = FXCollections.observableArrayList(projets);
            listeProjets.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadProjectsButtonClick() {
        try {
            loadProjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterProjet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet/ajouterProjet.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ajouterProjetButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifierButtonClick() {
        Projet selectedProjet = listeProjets.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet/modifierProjet.fxml"));
                Parent root = loader.load();
                ModifierProjetController controller = loader.getController();
                controller.setProjet(selectedProjet);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun projet sélectionné.");
        }
    }

    @FXML
    private void handleSupprimerButtonClick() {
        Projet selectedProjet = listeProjets.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            try {
                projetService.supprimer(selectedProjet.getId_projet());
                loadProjects(); // Recharger la liste après suppression
                System.out.println("Projet supprimé avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun projet sélectionné.");
        }
    }
}