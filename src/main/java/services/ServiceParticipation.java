package services;

import models.Participation;
import tools.myDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipation implements IServices<Participation> {
    private Connection cnx;

    public ServiceParticipation() {
        cnx = myDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Participation p) throws SQLException {
        String sql = "INSERT INTO participation (id_event, id_user, date_participation, role_participant, depart_participant, contact, experience_event, remarque) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, p.getId_event());
            ste.setInt(2, p.getId_user());
            ste.setDate(3, new java.sql.Date(p.getDate_participation().getTime()));
            ste.setString(4, p.getRole_participant());
            ste.setString(5, p.getDepart_participant());
            ste.setString(6, p.getContact());
            ste.setString(7, p.getExperience_event());
            ste.setString(8, p.getRemarque());
            ste.executeUpdate();
            System.out.println("Participation ajoutée");
            cnx.commit(); // Engagement de la transaction
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la participation : " + e.getMessage());
            throw e; // Relancer l'exception si nécessaire
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM participation WHERE id_participation=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id);
            ste.executeUpdate();
            System.out.println("Participation supprimée");
            cnx.commit(); // Engagement de la transaction
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la participation : " + e.getMessage());
            throw e; // Relancer l'exception si nécessaire
        }
    }

    @Override
    public void modifier(int id, String role_participant, String depart_participant, String contact) throws SQLException {
        String sql = "UPDATE participation SET role_participant=?, depart_participant=?, contact=? WHERE id_participation=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, role_participant);
            ste.setString(2, depart_participant);
            ste.setString(3, contact);
            ste.setInt(4, id);
            ste.executeUpdate();
            System.out.println("Participation modifiée");
            cnx.commit(); // Engagement de la transaction
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la participation : " + e.getMessage());
            throw e; // Relancer l'exception si nécessaire
        }
    }

    @Override
    public List<Participation> recuperer() throws SQLException {
        String sql = "SELECT * FROM participation";
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {
            List<Participation> participations = new ArrayList<>();

            while (rs.next()) {
                Participation p = new Participation();
                p.setId_participation(rs.getInt("id_participation"));
                p.setId_event(rs.getInt("id_event"));
                p.setId_user(rs.getInt("id_user"));
                p.setDate_participation(rs.getDate("date_participation"));
                p.setRole_participant(rs.getString("role_participant"));
                p.setDepart_participant(rs.getString("depart_participant"));
                p.setContact(rs.getString("contact"));
                p.setExperience_event(rs.getString("experience_event"));
                p.setRemarque(rs.getString("remarque"));
                participations.add(p);
            }
            return participations;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations : " + e.getMessage());
            throw e; // Relancer l'exception si nécessaire
        }
    }

    public List<Participation> recupererParEvenement(int idEvent) throws SQLException {
        List<Participation> participations = new ArrayList<>();
        String query = "SELECT * FROM participation WHERE id_event = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, idEvent);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Participation participation = new Participation();
                participation.setId_participation(resultSet.getInt("id_participation"));
                participation.setId_event(resultSet.getInt("id_event"));
                participation.setId_user(resultSet.getInt("id_user"));
                participation.setDate_participation(resultSet.getDate("date_participation"));
                participation.setRole_participant(resultSet.getString("role_participant"));
                participation.setDepart_participant(resultSet.getString("depart_participant"));
                participation.setContact(resultSet.getString("contact"));
                participation.setExperience_event(resultSet.getString("experience_event"));
                participation.setRemarque(resultSet.getString("remarque"));

                participations.add(participation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations : " + e.getMessage());
            throw e; // Relancer l'exception pour la gérer ailleurs si nécessaire
        }
        return participations;
    }

    public boolean checkParticipation(int idEvent, int idUser) throws SQLException {
        String query = "SELECT COUNT(*) FROM participation WHERE id_event = ? AND id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idEvent);
            stmt.setInt(2, idUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retourne true si une participation existe
            }
        }
        return false;
    }
}
