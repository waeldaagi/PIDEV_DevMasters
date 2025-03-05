package services;

import interfaces.IUserInterface;
import models.User;
import models.UserRole;
import utils.MyDatabase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInService implements IUserInterface {
    private final Connection cnx;

    public SignInService() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public User signIn(String identifier, String password) {
        String query = "SELECT * FROM users WHERE email = ? OR username = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            preparedStatement.setString(1, identifier);
            preparedStatement.setString(2, identifier);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");
                boolean isActive = resultSet.getBoolean("is_active");

                // Check if the user is banned (isActive is false)
                if (!isActive) {
                    System.out.println("❌ User is banned. Please contact the admin.");
                    return null;
                }

                // Debugging logs
                System.out.println("🔍 Entered password: " + password);
                System.out.println("🔍 Stored hashed password: " + storedHashedPassword);

                if (!storedHashedPassword.startsWith("$2a$")) {
                    System.out.println("🚨 Warning: Stored password is NOT hashed! Please re-hash user passwords.");
                    return null;
                }

                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    System.out.println("✅ Password match successful!");

                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            UserRole.valueOf(resultSet.getString("role")),
                            isActive, // This value is now used
                            resultSet.getString("phone_number"),
                            resultSet.getString("address"),
                            resultSet.getDate("birth_date"),
                            resultSet.getDouble("salary"),
                            storedHashedPassword
                    );
                } else {
                    System.out.println("❌ Incorrect password entered.");
                }
            } else {
                System.out.println("❌ User not found.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error during login: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
