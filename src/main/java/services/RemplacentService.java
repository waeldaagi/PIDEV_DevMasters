package services;

import models.Remplacent;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RemplacentService {

    private final Connection cnx;

    public RemplacentService() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    public boolean addRemplacent(Remplacent remplacent) {
        String query = "INSERT INTO remplacent (idConge, idRemplacent, posteRemplace) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, remplacent.getIdConge());
            preparedStatement.setInt(2, remplacent.getIdRemplacent());
            preparedStatement.setString(3, remplacent.getPosteRemplace().name());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Remplacent> getAllRemplacents() {
        List<Remplacent> remplacents = new ArrayList<>();
        String query = "SELECT * FROM remplacent";

        try (Statement statement = cnx.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Remplacent remplacent = new Remplacent(
                        resultSet.getInt("id"),
                        resultSet.getInt("idConge"),
                        resultSet.getInt("idRemplacent"),
                        models.PosteRemplace.valueOf(resultSet.getString("posteRemplace"))
                );
                remplacents.add(remplacent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remplacents;
    }
    public List<Integer> getAssignedConges() {
        List<Integer> assignedConges = new ArrayList<>();
        String query = "SELECT idConge FROM remplacent";

        try (Statement statement = cnx.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                assignedConges.add(resultSet.getInt("idConge"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignedConges;
    }
}
