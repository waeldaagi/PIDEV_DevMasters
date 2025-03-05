package services.sahar;

import models.Evennement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvennement implements IServices<Evennement> {
    private Connection cnx;

    public ServiceEvennement() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Evennement e) throws SQLException {
        String sql = "INSERT INTO evennement (nom_event, description, date_event, lieu_event, organisateur, statut, img_event) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            cnx.setAutoCommit(false);  // Disable auto-commit

            ste.setString(1, e.getNom_event());
            ste.setString(2, e.getDescription());
            ste.setDate(3, new Date(e.getDate_event().getTime()));
            ste.setString(4, e.getLieu_event());
            ste.setString(5, e.getOrganisateur());
            ste.setString(6, e.getStatut());
            ste.setString(7, e.getImg_event());
            ste.executeUpdate();

            cnx.commit();  // Commit changes
            System.out.println("Événement ajouté avec succès !");
        } catch (SQLException e1) {
            cnx.rollback();  // Rollback on error
            System.err.println("Erreur lors de l'ajout de l'événement : " + e1.getMessage());
            throw e1;
        } finally {
            cnx.setAutoCommit(true);  // Restore auto-commit mode
        }
    }

    public void supprimer(int idEvent) throws SQLException {
        try {
            cnx.setAutoCommit(false); // Disable auto-commit

            String query = "DELETE FROM evennement WHERE id_event = ?";
            try (PreparedStatement statement = cnx.prepareStatement(query)) {
                statement.setInt(1, idEvent);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    cnx.commit(); // Explicit commit if successful
                    System.out.println("Événement supprimé avec succès !");
                } else {
                    cnx.rollback(); // Rollback if nothing was deleted
                    System.out.println("Aucun événement trouvé pour suppression.");
                }
            }
        } catch (SQLException e) {
            cnx.rollback(); // Rollback on error
            System.err.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
        } finally {
            cnx.setAutoCommit(true); // Restore auto-commit mode
        }
    }

    @Override
    public void modifier(int id, String nom_event, String description, String statut) throws SQLException {
        String sql = "UPDATE evennement SET nom_event=?, description=?, statut=? WHERE id_event=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            cnx.setAutoCommit(false);

            ste.setString(1, nom_event);
            ste.setString(2, description);
            ste.setString(3, statut);
            ste.setInt(4, id);
            ste.executeUpdate();

            cnx.commit();
            System.out.println("Événement modifié avec succès !");
        } catch (SQLException e) {
            cnx.rollback();
            System.err.println("Erreur lors de la modification de l'événement : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    @Override
    public List<Evennement> recuperer() throws SQLException {
        String sql = "SELECT * FROM evennement";
        List<Evennement> evenements = new ArrayList<>();
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {

            while (rs.next()) {
                Evennement e = new Evennement(
                        rs.getInt("id_event"),
                        rs.getString("nom_event"),
                        rs.getString("description"),
                        rs.getDate("date_event"),
                        rs.getString("lieu_event"),
                        rs.getString("organisateur"),
                        rs.getString("statut"),
                        rs.getString("img_event")
                );
                evenements.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
            throw e;
        }
        return evenements;
    }

    public Evennement getById(int idEvent) throws SQLException {
        String sql = "SELECT * FROM evennement WHERE id_event = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, idEvent);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Evennement(
                        rs.getInt("id_event"),
                        rs.getString("nom_event"),
                        rs.getString("description"),
                        rs.getDate("date_event"),
                        rs.getString("lieu_event"),
                        rs.getString("organisateur"),
                        rs.getString("statut"),
                        rs.getString("img_event")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'événement : " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void modifierEvent(int id, String description, Date date_event, String lieu_event, String organisateur) throws SQLException {
        String sql = "UPDATE evennement SET description=?, date_event=?, lieu_event=?, organisateur=? WHERE id_event=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            cnx.setAutoCommit(false);

            ste.setString(1, description);
            ste.setDate(2, new Date(date_event.getTime()));
            ste.setString(3, lieu_event);
            ste.setString(4, organisateur);
            ste.setInt(5, id);
            ste.executeUpdate();

            cnx.commit();
            System.out.println("Événement mis à jour avec succès !");
        } catch (SQLException e) {
            cnx.rollback();
            System.err.println("Erreur lors de la mise à jour de l'événement : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    public void annulerEvenement(int idEvent) throws SQLException {
        String query = "UPDATE evennement SET statut = 'annulé' WHERE id_event = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            cnx.setAutoCommit(false);

            pstmt.setInt(1, idEvent);
            pstmt.executeUpdate();

            cnx.commit();
            System.out.println("Événement annulé avec succès !");
        } catch (SQLException e) {
            cnx.rollback();
            System.err.println("Erreur lors de l'annulation de l'événement : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    public List<String> getParticipantContacts(int eventId) throws SQLException {
        List<String> contacts = new ArrayList<>();
        String query = "SELECT contact FROM participation WHERE id_event = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String contact = resultSet.getString("contact");
                if (contact != null && !contact.isEmpty()) {
                    contacts.add(contact);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des contacts : " + e.getMessage());
            throw e;
        }
        return contacts;
    }
}
