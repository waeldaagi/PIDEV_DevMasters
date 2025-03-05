package controllers.wael;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Equipe;
import services.EquipeService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierEquipeController implements Initializable {

    @FXML
    private TextField nomEquipeTextField;

    @FXML
    private TextField nbrEmployeeTextField;

    @FXML
    private TextField nomTechLeadTextField;

    private Equipe equipe;
    private final EquipeService equipeService = new EquipeService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // Method to set the selected team
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
        // Populate the fields with the selected team's data
        nomEquipeTextField.setText(equipe.getNomEquipe());
        nbrEmployeeTextField.setText(String.valueOf(equipe.getNbrEmployee()));
        nomTechLeadTextField.setText(equipe.getNomTeqlead());
    }

    @FXML
    private void modifierEquipe() {
        try {
            // Update the team with the new values
            equipe.setNomEquipe(nomEquipeTextField.getText());
            equipe.setNbrEmployee(Integer.parseInt(nbrEmployeeTextField.getText()));
            equipe.setNomTeqlead(nomTechLeadTextField.getText());

            // Call the service to update the team
            equipeService.modifier(equipe);

            System.out.println("Équipe modifiée avec succès !");

            // Close the current window after modification
            Stage stage = (Stage) nomEquipeTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        // Close the current window without saving changes
        Stage stage = (Stage) nomEquipeTextField.getScene().getWindow();
        stage.close();
    }
}