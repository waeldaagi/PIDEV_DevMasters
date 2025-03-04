package services;

import models.User;
import models.UserRole;
import org.mindrot.jbcrypt.BCrypt;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserService {

    private final Connection cnx = MyDatabase.getInstance().getCnx();

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getBoolean("is_active"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("birth_date"),
                        rs.getDouble("salary"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean updateUserProfile(User user) {
        String query = "UPDATE users SET username = ?, email = ?, phone_number = ?, address = ?, birth_date = ? WHERE id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhoneNumber());
            pst.setString(4, user.getAddress());
            pst.setDate(5, new java.sql.Date(user.getBirthDate().getTime()));
            pst.setInt(6, user.getId());

            int rowsAffected = pst.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserPassword(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, hashedPassword);
            pst.setString(2, user.getEmail());
            int rowsAffected = pst.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Failed to update password for user: " + user.getEmail() + " | Error: " + e.getMessage());
            return false;
        }
    }
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public boolean doesUsernameExist(String username, int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND id != ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, username);
            pst.setInt(2, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean doesEmailExist(String email, int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, email);
            pst.setInt(2, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getBoolean("is_active"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("birth_date"),
                        rs.getDouble("salary"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
