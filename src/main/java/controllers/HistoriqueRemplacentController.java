package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Remplacent;
import models.User;
import models.Conge;
import models.PosteRemplace;
import services.RemplacentService;
import services.UserService;
import services.CongeService;

import java.util.List;
import java.util.stream.Collectors;

public class HistoriqueRemplacentController {

    @FXML private ListView<String> remplacentListView;
    @FXML private TextField searchField;

    private final RemplacentService remplacentService = new RemplacentService();
    private final UserService userService = new UserService();
    private final CongeService congeService = new CongeService();

    private List<Remplacent> remplacentList;
    private ObservableList<String> observableList;

    @FXML
    public void initialize() {
        loadRemplacentData();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterRemplacentList(newValue);
        });
    }

    private void loadRemplacentData() {
        remplacentList = remplacentService.getAllRemplacents();
        observableList = FXCollections.observableArrayList();

        for (Remplacent remplacent : remplacentList) {
            User user = userService.getUserById(remplacent.getIdRemplacent());
            Conge conge = congeService.getCongeById(remplacent.getIdConge());

            String displayText = "🔹 " + user.getUsername() + " (" + user.getEmail() + ") a remplacé " +
                    conge.getTypeConge().name() + " | " + conge.getDateDebut() + " → " + conge.getDateFin() +
                    " au poste de " + remplacent.getPosteRemplace();

            observableList.add(displayText);
        }

        remplacentListView.setItems(observableList);
    }

    private void filterRemplacentList(String searchText) {
        List<String> filteredList = remplacentList.stream()
                .map(remplacent -> {
                    User user = userService.getUserById(remplacent.getIdRemplacent());
                    Conge conge = congeService.getCongeById(remplacent.getIdConge());

                    return "🔹 " + user.getUsername() + " (" + user.getEmail() + ") a remplacé " +
                            conge.getTypeConge().name() + " | " + conge.getDateDebut() + " → " + conge.getDateFin() +
                            " au poste de " + remplacent.getPosteRemplace();
                })
                .filter(item -> item.toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        observableList.setAll(filteredList);
    }
}
