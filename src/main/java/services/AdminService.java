package services;

import models.User;
import models.UserRole;
import utils.MyDatabase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private final Connection cnx;

    public AdminService() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    public List<User> getAllEmployees() {
        List<User> employees = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'EMPLOYE'";

        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                employees.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        UserRole.EMPLOYE,
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getDate("birth_date"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public boolean addEmployee(User user) {
        if (isEmailOrUsernameExists(user.getEmail(), user.getUsername())) {
            System.out.println("❌ Error: Email or Username already exists!");
            return false;
        }

        String query = "INSERT INTO users (username, email, role, is_active, phone_number, address, birth_date, salary, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, "EMPLOYE");
            stmt.setBoolean(4, true);
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAddress());
            stmt.setDate(7, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setDouble(8, user.getSalary());
            stmt.setString(9, user.getPassword());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean isEmailOrUsernameExists(String email, String username) {
        String query = "SELECT * FROM users WHERE email = ? OR username = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
    public boolean deleteEmployee(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean promoteEmployee(int id, double currentSalary) {
        String query = "UPDATE users SET role = 'RH', salary = ? WHERE id = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setDouble(1, currentSalary + 500);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateEmployee(User user) {
        String query = "UPDATE users SET username = ?, email = ?, phone_number = ?, address = ?, birth_date = ?, salary = ? WHERE id = ?";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getAddress());
            stmt.setDate(5, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setDouble(6, user.getSalary());
            stmt.setInt(7, user.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<User> getAllRH() {
        List<User> rhList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'RH'";

        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rhList.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        UserRole.RH,
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getDate("birth_date"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rhList;
    }
    public boolean demoteRH(int id, double currentSalary) {
        String query = "UPDATE users SET role = 'EMPLOYE', salary = ? WHERE id = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setDouble(1, currentSalary - 500);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<User> getAllCandidates() {
        List<User> candidates = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'CANDIDATE'";

        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                candidates.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        UserRole.CANDIDATE,
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getDate("birth_date"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidates;
    }
    public boolean addCandidate(User user) {
        if (isEmailOrUsernameExists(user.getEmail(), user.getUsername())) {
            System.out.println("❌ Error: Email or Username already exists!");
            return false;
        }

        String query = "INSERT INTO users (username, email, role, is_active, phone_number, address, birth_date, salary, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, "CANDIDATE");
            stmt.setBoolean(4, true);
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAddress());
            stmt.setDate(7, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setDouble(8, user.getSalary());
            stmt.setString(9, user.getPassword());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCandidate(User user) {
        String query = "UPDATE users SET username = ?, email = ?, phone_number = ?, address = ?, birth_date = ?, salary = ? WHERE id = ? AND role = 'CANDIDATE'";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getAddress());
            stmt.setDate(5, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setDouble(6, user.getSalary());
            stmt.setInt(7, user.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteCandidate(int id) {
        String query = "DELETE FROM users WHERE id = ? AND role = 'CANDIDATE'";
        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean promoteCandidateToEmployee(int id) {
        String query = "UPDATE users SET role = 'EMPLOYE', salary = 1600 WHERE id = ? AND role = 'CANDIDATE'";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean demoteToCandidate(int id, double currentSalary) {
        String query = "UPDATE users SET role = 'CANDIDATE', salary = 0 WHERE id = ? AND role = 'EMPLOYE'";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean setUserActiveStatus(int userId, boolean isActive) {
        String query = "UPDATE users SET is_active = ? WHERE id = ?";
        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null || conn.isClosed()) {
                System.out.println("❌ Error: Connection is closed, reconnecting...");
                return false;
            }

            stmt.setBoolean(1, isActive);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
