package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Equipe;
import servise.EquipeServise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Afficher_equipe_controller {
    private final EquipeServise ps = new EquipeServise();

    @FXML
    private VBox equipesContainer;
    private models.Equipe Equipe;

    public void initialize() throws SQLException {
        // Simulate fetching team data
        List<Equipe> equipes = ps.getAll(new Equipe());

        for (Equipe equipe : equipes) {
            HBox teamBox = new HBox(10); // Spacing between elements
            Label nameLabel = new Label("Nom: " + equipe.getNomEquipe());
            Label employeesLabel = new Label("Employés: " + equipe.getNbrEmployee());
            Label techleadLabel = new Label("Techlead: " + equipe.getNomTeqlead());

            teamBox.getChildren().addAll(nameLabel, employeesLabel, techleadLabel);
            equipesContainer.getChildren().add(teamBox);
        }
    }
    @FXML
    public void retourVersAjouter(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ajoutEquipe.fxml")); // Assurez-vous que le chemin est correct
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void effacerEquipe(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/DeleteEquipe.fxml")); // Assurez-vous que le chemin est correct
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
