package test;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import utils.SessionManager;  // Make sure to import from the utils package
import utils.JWTUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class MainFX extends Application {

    private static final Logger logger = Logger.getLogger(MainFX.class.getName());

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load tokens from session storage
        SessionManager.loadTokens();

        Parent root;

        if (SessionManager.isLoggedIn()) {
            String role = JWTUtils.parseToken(SessionManager.getAccessToken()).get("role").toString();
            if (JWTUtils.isTokenValid(SessionManager.getAccessToken())) {
                logger.info("Session is valid for user with role: " + role);
                String fxmlPath = getDashboardPath(role);
                root = FXMLLoader.load(getClass().getResource(fxmlPath));
            } else {
                logger.warning("Session expired, redirecting to sign-in page.");
                SessionManager.logout();
                root = FXMLLoader.load(getClass().getResource("/signin.fxml"));
            }
        } else {
            logger.info("No valid session found, redirecting to sign-in page.");
            root = FXMLLoader.load(getClass().getResource("/signin.fxml"));
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Utilisateur");
        primaryStage.show();
    }

    private String getDashboardPath(String role) {
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

    public static void main(String[] args) {
        launch(args);
    }
}
