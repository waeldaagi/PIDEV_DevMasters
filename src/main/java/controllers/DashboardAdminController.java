package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import models.User;
import models.UserRole;
import services.UserService;

import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardAdminController {

    @FXML private LineChart<String, Number> userChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis numberAxis;

    @FXML private ComboBox<String> filterComboBox;

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        filterComboBox.setItems(FXCollections.observableArrayList("Day", "Month", "Year"));
        filterComboBox.getSelectionModel().select(0);
        loadUserChart("Day");
        filterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadUserChart(newValue);
        });
    }

    private void loadUserChart(String filter) {
        List<User> users = userService.getAllUsers();
        Map<String, Integer> userCountByDate = new TreeMap<>();
        Map<String, Integer> employeeCountByDate = new TreeMap<>();
        Map<String, Integer> rhCountByDate = new TreeMap<>();
        Map<String, Integer> candidateCountByDate = new TreeMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        for (User user : users) {
            String formattedDate;
            if (filter.equals("Day")) {
                formattedDate = dateFormat.format(user.getBirthDate());
            } else if (filter.equals("Month")) {
                formattedDate = monthFormat.format(user.getBirthDate());
            } else {
                formattedDate = yearFormat.format(user.getBirthDate());
            }

            userCountByDate.put(formattedDate, userCountByDate.getOrDefault(formattedDate, 0) + 1);
            if (user.getRole() == UserRole.EMPLOYE) {
                employeeCountByDate.put(formattedDate, employeeCountByDate.getOrDefault(formattedDate, 0) + 1);
            } else if (user.getRole() == UserRole.RH) {
                rhCountByDate.put(formattedDate, rhCountByDate.getOrDefault(formattedDate, 0) + 1);
            } else if (user.getRole() == UserRole.CANDIDATE) {
                candidateCountByDate.put(formattedDate, candidateCountByDate.getOrDefault(formattedDate, 0) + 1);
            }
        }

        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();

        XYChart.Series<String, Number> totalUsersSeries = new XYChart.Series<>();
        totalUsersSeries.setName("Total Users");

        XYChart.Series<String, Number> employeeSeries = new XYChart.Series<>();
        employeeSeries.setName("Employees");

        XYChart.Series<String, Number> rhSeries = new XYChart.Series<>();
        rhSeries.setName("RH");

        XYChart.Series<String, Number> candidateSeries = new XYChart.Series<>();
        candidateSeries.setName("Candidates");

        for (String dateKey : userCountByDate.keySet()) {
            totalUsersSeries.getData().add(new XYChart.Data<>(dateKey, userCountByDate.get(dateKey)));
            employeeSeries.getData().add(new XYChart.Data<>(dateKey, employeeCountByDate.getOrDefault(dateKey, 0)));
            rhSeries.getData().add(new XYChart.Data<>(dateKey, rhCountByDate.getOrDefault(dateKey, 0)));
            candidateSeries.getData().add(new XYChart.Data<>(dateKey, candidateCountByDate.getOrDefault(dateKey, 0)));
        }

        seriesList.add(totalUsersSeries);
        seriesList.add(employeeSeries);
        seriesList.add(rhSeries);
        seriesList.add(candidateSeries);

        userChart.setData(seriesList);

        categoryAxis.setLabel(filter + " (Date)");
        numberAxis.setLabel("Number of Users");

        categoryAxis.setCategories(FXCollections.observableArrayList(userCountByDate.keySet()));

        styleChart();
    }

    private void styleChart() {
        userChart.setStyle("-fx-background-color: #F5F5F5;");

        categoryAxis.setTickLabelFill(javafx.scene.paint.Color.GRAY);
        numberAxis.setTickLabelFill(javafx.scene.paint.Color.GRAY);

        userChart.setCreateSymbols(false);
        for (XYChart.Series<String, Number> series : userChart.getData()) {
            series.getNode().setStyle("-fx-stroke-width: 3; -fx-stroke-line-cap: round;");
        }

        for (XYChart.Series<String, Number> series : userChart.getData()) {
            if (series.getName().equals("Total Users")) {
                series.getNode().setStyle("-fx-stroke: #1E88E5;");
            } else if (series.getName().equals("Employees")) {
                series.getNode().setStyle("-fx-stroke: #388E3C;");
            } else if (series.getName().equals("RH")) {
                series.getNode().setStyle("-fx-stroke: #FBC02D;");
            } else if (series.getName().equals("Candidates")) {
                series.getNode().setStyle("-fx-stroke: #D32F2F;");
            }
        }
    }
}
