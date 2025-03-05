package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.SignInService;
import services.UserService;
import utils.JWTUtils;
import utils.SessionManager;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.logging.Logger;

public class SignInController {

    @FXML
    private TextField emailOrUsernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private CheckBox rememberMe;

    private final SignInService signInService = new SignInService();
    private final UserService userService = new UserService();
    private static final Logger logger = Logger.getLogger(SignInController.class.getName());

    @FXML
    private void initialize() {
        // On initializing, check if the user has a valid session.
        if (SessionManager.isLoggedIn()) {
            String role = JWTUtils.parseToken(SessionManager.getAccessToken()).get("role").toString();
            if (JWTUtils.isTokenValid(SessionManager.getAccessToken())) {
                logger.info("Session is valid for user with role: " + role);
                redirectToDashboardBasedOnRole(role);
            } else {
                logger.warning("Session expired or invalid. Redirecting to sign-in page.");
                SessionManager.logout();
            }
        }
    }

    @FXML
    private void handleLogin() {
        String identifier = emailOrUsernameField.getText().trim();
        String password = passwordField.getText().trim();

        logger.info("🔹 Login attempt with identifier: " + identifier);

        if (!validateInputs(identifier, password)) {
            logger.warning("⚠️ Validation failed for input: " + identifier);
            return;
        }

        User user = signInService.signIn(identifier, password);

        if (user != null) {
            logger.info("✅ Login successful for user: " + user.getUsername() + " | Role: " + user.getRole());

            String accessToken = JWTUtils.generateAccessToken(user);
            String refreshToken = JWTUtils.generateRefreshToken(user);

            SessionManager.setTokens(accessToken, refreshToken);

            logger.info("Session created for user: " + user.getUsername() + " with role: " + user.getRole());

            redirectToDashboard(user);
        } else {
            logger.warning("❌ Invalid login credentials for: " + identifier);
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials. Please try again.");
        }
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Account Suspended", "Your account is suspended or banned. Please contact the admin.");
        }
    }

    private boolean validateInputs(String identifier, String password) {
        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return false;
        }

        if (identifier.contains("@")) {
            if (!isValidEmail(identifier)) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid email address.");
                logger.warning("⚠️ Invalid email format entered: " + identifier);
                return false;
            }
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Password must be at least 6 characters long.");
            logger.warning("⚠️ Password too short for user: " + identifier);
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private void redirectToDashboard(User user) {
        String fxmlPath = getDashboardPath(user);
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dashboard - " + user.getRole());
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the dashboard.");
        }
    }

    private String getDashboardPath(User user) {
        switch (user.getRole()) {
            case ADMIN:
                return "/admin/dashboardadmin.fxml";
            case RH:
                return "/rh/rhdashboard.fxml";
            case EMPLOYE:
                return "/employe/employedashboard.fxml";
            case CANDIDATE:
                return "/candidate/AfficherCandidat.fxml";
            default:
                return "";
        }
    }

    private void redirectToDashboardBasedOnRole(String role) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getDashboardPathByRole(role)));
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the dashboard based on session.");
        }
    }

    private String getDashboardPathByRole(String role) {
        switch (role) {
            case "ADMIN":
                return "/admin/dashboardadmin.fxml";
            case "RH":
                return "/rh/rhdashboard.fxml";
            case "EMPLOYE":
                return "/employe/employedashboard.fxml";
            case "CANDIDATE":
                return "/candidate/AfficherCandidat.fxml";
            default:
                return "/signin.fxml";
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void redirectToForgotPassword() {
        String email = emailOrUsernameField.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please enter your email address.");
            return;
        }

        User user = userService.getUserByEmail(email);
        if (user != null) {
            try {
                Stage otpStage = new Stage();
                otpStage.setTitle("OTP Verification");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/security/OTPCode.fxml"));
                otpStage.setScene(new Scene(loader.load()));

                OTPCodeController otpCodeController = loader.getController();
                otpCodeController.receiveEmail(email);
                otpCodeController.sendOtp();
                otpCodeController.setStage(otpStage);

                otpStage.initOwner(loginButton.getScene().getWindow());
                otpStage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load OTP verification page.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No user found with this email address.");
        }
    }

    @FXML
    private void redirectToSignUp() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signup.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the sign up page.");
        }
    }
}
