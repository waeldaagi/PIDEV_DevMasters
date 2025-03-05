package controllers.wael;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.ProjetService;

import java.sql.SQLException;

public class DeleteProjetController {
    private final ProjetService ps = new ProjetService();

    @FXML
    private TextField idProjetTextField;

    @FXML
    void supprimerProjet(ActionEvent event) throws SQLException {
        ps.supprimer(Integer.parseInt(idProjetTextField.getText()));
    }

}
