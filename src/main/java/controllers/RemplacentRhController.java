package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Conge;
import models.Remplacent;
import models.User;
import models.PosteRemplace;
import services.CongeService;
import services.RemplacentService;
import services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RemplacentRhController {

    @FXML private ComboBox<String> employeeComboBox;
    @FXML private ComboBox<String> congeComboBox;
    @FXML private ComboBox<PosteRemplace> posteComboBox;
    @FXML private Button assignButton;
    @FXML private Button historyButton;

    private final UserService userService = new UserService();
    private final CongeService congeService = new CongeService();
    private final RemplacentService remplacentService = new RemplacentService();

    private List<User> employeesList;
    private List<Conge> congeList;

    @FXML
    public void initialize() {
        loadAvailableEmployees();
        loadAvailableConges();
        loadPostes();

        assignButton.setOnAction(event -> assignRemplacent());
        historyButton.setOnAction(event -> openHistoriqueRemplacent());
    }

    private void loadAvailableEmployees() {
        employeesList = userService.getEmployeesWithoutLeave();
        employeeComboBox.getItems().clear();

        for (User employee : employeesList) {
            String displayText = employee.getUsername() + " (" + employee.getEmail() + ") - " +
                    employee.getRole() + " | Active: " + (employee.isActive() ? "Yes" : "No");
            employeeComboBox.getItems().add(displayText);
        }
    }

    private void loadAvailableConges() {
        List<Integer> assignedConges = remplacentService.getAssignedConges();
        congeList = congeService.getAllConges().stream()
                .filter(conge -> !assignedConges.contains(conge.getIdConge())) // Exclude replaced leaves
                .collect(Collectors.toList());

        congeComboBox.getItems().clear();
        for (Conge conge : congeList) {
            String displayText = conge.getTypeConge().name() + " | " + conge.getDateDebut() + " → " + conge.getDateFin();
            congeComboBox.getItems().add(displayText);
        }
    }

    private void loadPostes() {
        posteComboBox.getItems().setAll(PosteRemplace.values());
    }

    private void assignRemplacent() {
        int selectedEmployeeIndex = employeeComboBox.getSelectionModel().getSelectedIndex();
        int selectedCongeIndex = congeComboBox.getSelectionModel().getSelectedIndex();
        PosteRemplace selectedPoste = posteComboBox.getValue();

        if (selectedEmployeeIndex == -1 || selectedCongeIndex == -1 || selectedPoste == null) {
            showAlert("Erreur", "Veuillez sélectionner un employé, un congé et un poste.", Alert.AlertType.ERROR);
            return;
        }

        User selectedEmployee = employeesList.get(selectedEmployeeIndex);
        Conge selectedConge = congeList.get(selectedCongeIndex);

        Remplacent remplacent = new Remplacent(0, selectedConge.getIdConge(), selectedEmployee.getId(), selectedPoste);
        boolean success = remplacentService.addRemplacent(remplacent);

        if (success) {
            showAlert("Succès", "Le remplaçant a été assigné avec succès.", Alert.AlertType.INFORMATION);
            loadAvailableEmployees();
            loadAvailableConges();
        } else {
            showAlert("Erreur", "Échec de l'assignation.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openHistoriqueRemplacent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rh/historiqueRemplacent.fxml"));
            Pane root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Historique des Remplaçants");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'historique.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
