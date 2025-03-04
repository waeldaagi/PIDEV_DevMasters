package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.OTPService;
import utils.BrevoService;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class OTPCodeController {

    private static final Logger logger = Logger.getLogger(OTPCodeController.class.getName());

    @FXML
    private TextField otpField1, otpField2, otpField3, otpField4, otpField5, otpField6;
    @FXML
    private Button verifyButton;
    @FXML
    private Label statusLabel;

    private String userEmail;
    private String generatedEmailOtp;
    private Stage stage;

    public void initialize() {
        setupOtpFields();
    }

    private void setupOtpFields() {
        TextField[] fields = {otpField1, otpField2, otpField3, otpField4, otpField5, otpField6};
        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            fields[i].setOnKeyTyped(event -> {
                if (event.getCharacter().matches("\\d") && fields[index].getText().length() == 1) {
                    if (index < fields.length - 1) {
                        fields[index + 1].requestFocus();
                    }
                } else if (!event.getCharacter().matches("\\d") && !event.getCharacter().equals("\b")) {
                    fields[index].setText(fields[index].getText().replaceAll("[^\\d]", ""));
                }
            });
            fields[i].setOnKeyPressed(event -> {
                if (event.getCode().getName().equals("Backspace") && fields[index].getText().isEmpty() && index > 0) {
                    fields[index - 1].requestFocus();
                }
            });
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void receiveEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    public void sendOtp() {
        if (userEmail == null || userEmail.isEmpty()) {
            statusLabel.setText("Please enter your email.");
            return;
        }

        UserService userService = new UserService();
        User user = userService.getUserByEmail(userEmail);

        if (user != null) {
            OTPService otpService = new OTPService();
            generatedEmailOtp = otpService.generateOtp();

            BrevoService brevoService = new BrevoService();
            if (brevoService.sendOtpEmail(userEmail, generatedEmailOtp)) {
                logger.info("OTP sent via Email to: " + userEmail);
                statusLabel.setText("OTP sent via Email. Check your inbox.");
            } else {
                statusLabel.setText("Failed to send OTP via Email.");
                generatedEmailOtp = null;
            }
        } else {
            statusLabel.setText("Email not found. Please check and try again.");
        }
    }

    @FXML
    private void handleVerifyOtp() {
        String enteredOtp = otpField1.getText() + otpField2.getText() + otpField3.getText() +
                otpField4.getText() + otpField5.getText() + otpField6.getText();

        if (generatedEmailOtp != null && !enteredOtp.isEmpty()) {
            if (enteredOtp.equals(generatedEmailOtp)) {
                statusLabel.setText("Email OTP Verified Successfully!");
                logger.info("✅ Email OTP Verified Successfully.");

                if (stage != null) {
                    stage.close();
                }

                openModifyPasswordWindow();

            } else {
                statusLabel.setText("❌ Invalid OTP. Please try again.");
                logger.warning("❌ Invalid OTP entered: " + enteredOtp);
            }
        } else {
            statusLabel.setText("⚠️ No OTP sent or invalid input. Please try again.");
            logger.warning("⚠️ Attempted verification without OTP or empty input.");
        }
    }


    private void openModifyPasswordWindow() {
        try {
            String fxmlPath = "/security/ModifyPassword.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                logger.severe("⚠️ FXML file not found: " + fxmlPath);
                statusLabel.setText("❌ Error: Modify Password page not found.");
                return;
            }

            Stage modifyStage = new Stage();
            modifyStage.setTitle("🔑 Modify Password");
            modifyStage.setScene(new Scene(loader.load()));
            modifyStage.setWidth(600);
            modifyStage.setHeight(400);
            modifyStage.show();

            ModifyPasswordController modifyController = loader.getController();
            modifyController.setUserEmail(userEmail);
            modifyController.setStage(modifyStage);

            logger.info("✅ Modify Password window opened successfully.");

        } catch (IOException e) {
            logger.severe("❌ Failed to load Modify Password page: " + e.getMessage());
            statusLabel.setText("❌ Error loading password modification page.");
        }
    }
}