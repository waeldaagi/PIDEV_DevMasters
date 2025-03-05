package services.sahar;

import models.Participation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipation implements IServices<Participation> {
    private Connection cnx;

    public ServiceParticipation() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Participation p) throws SQLException {
        String sql = "INSERT INTO participation (id_event, id_user, date_participation, role_participant, depart_participant, contact, experience_event, remarque) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            cnx.setAutoCommit(false);

            ste.setInt(1, p.getId_event());
            ste.setInt(2, p.getId_user());
            ste.setDate(3, new Date(p.getDate_participation().getTime()));
            ste.setString(4, p.getRole_participant());
            ste.setString(5, p.getDepart_participant());
            ste.setString(6, p.getContact());
            ste.setString(7, p.getExperience_event());
            ste.setString(8, p.getRemarque());
            ste.executeUpdate();

            cnx.commit();
            System.out.println("Participation ajoutée avec succès !");
        } catch (SQLException e) {
            cnx.rollback();
            System.err.println("Erreur lors de l'ajout de la participation : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM participation WHERE id_participation=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            cnx.setAutoCommit(false);

            ste.setInt(1, id);
            int rowsAffected = ste.executeUpdate();

            if (rowsAffected > 0) {
                cnx.commit();
                System.out.println("Participation supprimée avec succès !");
            } else {
                cnx.rollback();
                System.out.println("Aucune participation trouvée pour suppression.");
            }
        } catch (SQLException e) {
            cnx.rollback();
            System.err.println("Erreur lors de la suppression de la participation : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    @Override
    public void modifier(int id, String role_participant, String depart_participant, String contact) throws SQLException {
        String sql = "UPDATE participation SET role_participant=?, depart_participant=?, contact=? WHERE id_participation=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            cnx.setAutoCommit(false);

            ste.setString(1, role_participant);
            ste.setString(2, depart_participant);
            ste.setString(3, contact);
            ste.setInt(4, id);
            ste.executeUpdate();

            cnx.commit();
            System.out.println("Participation modifiée avec succès !");
        } catch (SQLException e) {
            cnx.rollback();
            System.err.println("Erreur lors de la modification de la participation : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    @Override
    public List<Participation> recuperer() throws SQLException {
        String sql = "SELECT * FROM participation";
        List<Participation> participations = new ArrayList<>();
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {

            while (rs.next()) {
                Participation p = new Participation(
                        rs.getInt("id_participation"),
                        rs.getInt("id_event"),
                        rs.getInt("id_user"),
                        rs.getDate("date_participation"),
                        rs.getString("role_participant"),
                        rs.getString("depart_participant"),
                        rs.getString("contact"),
                        rs.getString("experience_event"),
                        rs.getString("remarque")
                );
                participations.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations : " + e.getMessage());
            throw e;
        }
        return participations;
    }

    public List<Participation> recupererParEvenement(int idEvent) throws SQLException {
        List<Participation> participations = new ArrayList<>();
        String query = "SELECT * FROM participation WHERE id_event = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, idEvent);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Participation participation = new Participation(
                        resultSet.getInt("id_participation"),
                        resultSet.getInt("id_event"),
                        resultSet.getInt("id_user"),
                        resultSet.getDate("date_participation"),
                        resultSet.getString("role_participant"),
                        resultSet.getString("depart_participant"),
                        resultSet.getString("contact"),
                        resultSet.getString("experience_event"),
                        resultSet.getString("remarque")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations : " + e.getMessage());
            throw e;
        }
        return participations;
    }

    public boolean checkParticipation(int idEvent, int idUser) throws SQLException {
        String query = "SELECT COUNT(*) FROM participation WHERE id_event = ? AND id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idEvent);
            stmt.setInt(2, idUser);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public String getUserNameById(int idUser) throws SQLException {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        }
        return "Unknown User";
    }

    public String getEventDetailsById(int idEvent) throws SQLException {
        String sql = "SELECT nom_event, date_event, lieu_event FROM evennement WHERE id_event = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, idEvent);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nom_event") + " | " + rs.getDate("date_event") + " | " + rs.getString("lieu_event");
            }
        }
        return "Unknown Event";
    }

    public String getUserEmailById(int userId) throws SQLException {
        String query = "SELECT email FROM users WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'email : " + e.getMessage());
            throw e;
        }
        return "Unknown Email";
    }
}
