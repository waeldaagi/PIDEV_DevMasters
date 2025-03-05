package services;

import models.User;
import utils.MyDatabase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class SignUpService {

    private final Connection cnx = MyDatabase.getInstance().getCnx();

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, email, password, phone_number, address, birth_date, role, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, hashedPassword);
            pst.setString(4, user.getPhoneNumber());
            pst.setString(5, user.getAddress());

            java.sql.Date sqlBirthDate = new java.sql.Date(user.getBirthDate().getTime());
            pst.setDate(6, sqlBirthDate);

            pst.setString(7, user.getRole().name());
            pst.setDouble(8, user.getSalary());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailAlreadyUsed(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
