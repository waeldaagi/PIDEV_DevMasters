package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import service.EquipeServise;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteEquipeController {
    private final EquipeServise ps = new EquipeServise();

    @FXML
    private TextField idEquipeTextField;

    @FXML
    void supprimerEquipe(ActionEvent event) throws IOException, SQLException {
        ps.supprimer(Integer.parseInt(idEquipeTextField.getText()));
    }
}