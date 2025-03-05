package services;

import models.Conge;
import models.StatutConge;
import models.TypeConge;
import utils.CloudinaryUtils;
import utils.PDFUtils;
import utils.BrevoService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CongeService {

    private static final Logger logger = Logger.getLogger(CongeService.class.getName());
    private final Connection cnx;
    private final BrevoService brevoService;

    public CongeService() {
        this.cnx = MyDatabase.getInstance().getCnx();
        this.brevoService = new BrevoService();
    }

    public boolean addConge(Conge conge, String localImagePath, String userEmail) {

        String cloudinaryUrl = CloudinaryUtils.uploadImageToCloudinary(localImagePath);
        if (cloudinaryUrl == null) {
            logger.severe("❌ Image upload failed!");
            return false;
        }


        String query = "INSERT INTO conge (idUser, typeConge, dateDebut, dateFin, dateDemande, statut, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, conge.getIdUser());
            preparedStatement.setString(2, conge.getTypeConge().name());
            preparedStatement.setDate(3, new java.sql.Date(conge.getDateDebut().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(conge.getDateFin().getTime()));
            preparedStatement.setDate(5, new java.sql.Date(conge.getDateDemande().getTime()));
            preparedStatement.setString(6, conge.getStatut().name());
            preparedStatement.setString(7, cloudinaryUrl);

            if (preparedStatement.executeUpdate() > 0) {
                String pdfUrl = PDFUtils.generatePDF(userEmail, conge, cloudinaryUrl);
                if (pdfUrl != null) {
                    brevoService.sendCongeEmail(userEmail, pdfUrl);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateConge(Conge conge) {
        String query = "UPDATE conge SET typeConge=?, dateDebut=?, dateFin=? WHERE idConge=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, conge.getTypeConge().name());
            preparedStatement.setDate(2, new java.sql.Date(conge.getDateDebut().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(conge.getDateFin().getTime()));
            preparedStatement.setInt(4, conge.getIdConge());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteConge(int idConge) {
        String query = "DELETE FROM conge WHERE idConge=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, idConge);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Conge> getAllConges() {
        List<Conge> conges = new ArrayList<>();
        String query = "SELECT * FROM conge";

        try (Statement statement = cnx.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Conge conge = new Conge(
                        resultSet.getInt("idConge"),
                        resultSet.getInt("idUser"),
                        TypeConge.valueOf(resultSet.getString("typeConge")),
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getDate("dateDemande"),
                        StatutConge.valueOf(resultSet.getString("statut")),
                        resultSet.getString("image")
                );
                conges.add(conge);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conges;
    }
    public boolean updateCongeStatus(int idConge, StatutConge newStatus) {
        String query = "UPDATE conge SET statut=? WHERE idConge=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, newStatus.name());
            preparedStatement.setInt(2, idConge);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUserEmailById(int idUser) {
        String email = null;
        String query = "SELECT email FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                email = resultSet.getString("email"); // ✅ Ensure this matches your column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }
    public Conge getCongeById(int id) {
        String query = "SELECT * FROM conge WHERE idConge = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Conge(
                        rs.getInt("idConge"),
                        rs.getInt("idUser"),
                        TypeConge.valueOf(rs.getString("typeConge")),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getDate("dateDemande"),
                        StatutConge.valueOf(rs.getString("statut")),
                        rs.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
