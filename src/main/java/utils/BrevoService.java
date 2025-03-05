package utils;

import models.Conge;
import models.StatutConge;
import okhttp3.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Logger;

public class BrevoService {

    private static final Logger logger = Logger.getLogger(BrevoService.class.getName());

    private static final String API_KEY = "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";
    private static final String BASE_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String SENDER_EMAIL = "trikiamine20@gmail.com";
    private static final String SENDER_NAME = "ressourceshumaines";

    public boolean sendOtpEmail(String toEmail, String otpCode) {
        OkHttpClient client = new OkHttpClient();

        JsonObject emailBody = new JsonObject();
        JsonObject sender = new JsonObject();
        sender.addProperty("name", SENDER_NAME);
        sender.addProperty("email", SENDER_EMAIL);
        emailBody.add("sender", sender);

        JsonArray toArray = new JsonArray();
        JsonObject recipient = new JsonObject();
        recipient.addProperty("email", toEmail);
        toArray.add(recipient);
        emailBody.add("to", toArray);

        emailBody.addProperty("subject", "Your OTP Code for Verification");

        // Enhanced HTML email content
        String htmlContent = "<html><head>" +
                "<style>" +
                "body { font-family: 'Arial', sans-serif; background-color: #f4f7fa; margin: 0; padding: 0; }" +
                ".email-container { width: 100%; background-color: #ffffff; max-width: 600px; margin: 30px auto; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                ".email-header { background-color: #1a73e8; color: #ffffff; text-align: center; padding: 30px 0; font-size: 28px; font-weight: bold; border-top-left-radius: 8px; border-top-right-radius: 8px; }" +
                ".email-content { padding: 30px; font-size: 16px; color: #333333; line-height: 1.5; }" +
                ".otp-code { font-size: 24px; font-weight: bold; color: #1a73e8; margin-top: 10px; padding: 10px; background-color: #e1f5fe; border-radius: 6px; display: inline-block; }" +
                ".footer { text-align: center; padding: 20px; font-size: 14px; color: #5f6368; background-color: #f1f3f4; }" +
                ".footer a { color: #1a73e8; text-decoration: none; }" +
                ".button { display: inline-block; background-color: #1a73e8; color: #ffffff; padding: 10px 20px; font-size: 16px; text-align: center; border-radius: 25px; text-decoration: none; margin-top: 20px; }" +
                ".button:hover { background-color: #0d47a1; }" +
                "</style>" +
                "</head><body>" +
                "<div class=\"email-container\">" +
                "<div class=\"email-header\">OTP Verification</div>" +
                "<div class=\"email-content\">" +
                "<p>Hi there,</p>" +
                "<p>We received a request to reset your password. To complete the process, please use the One-Time Password (OTP) below:</p>" +
                "<div class=\"otp-code\">" + otpCode + "</div>" +
                "<p>This OTP is valid for 10 minutes. Please do not share it with anyone.</p>" +
                "<p>If you didn't request this, please ignore this email.</p>" +
                "<a href=\"#\" class=\"button\">Verify OTP</a>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>Thank you for using our service!</p>" +
                "<p><a href=\"#\">Contact Support</a> | <a href=\"#\">Privacy Policy</a></p>" +
                "</div>" +
                "</div>" +
                "</body></html>";

        emailBody.addProperty("htmlContent", htmlContent);

        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"), emailBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("api-key", API_KEY)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (response.isSuccessful()) {
                logger.info("OTP email sent successfully to: " + toEmail + " | OTP: " + otpCode);
                return true;
            } else {
                logger.severe("Failed to send OTP email. Response code: " + response.code() + " | Response: " + responseBody);
                return false;
            }
        } catch (IOException e) {
            logger.severe("Error sending OTP email to: " + toEmail + " | Error: " + e.getMessage());
            return false;
        }
    }
    public boolean sendCongeEmail(String toEmail, String pdfUrl) {
        OkHttpClient client = new OkHttpClient();

        JsonObject emailBody = new JsonObject();
        JsonObject sender = new JsonObject();
        sender.addProperty("name", SENDER_NAME);
        sender.addProperty("email", SENDER_EMAIL);
        emailBody.add("sender", sender);

        JsonArray toArray = new JsonArray();
        JsonObject recipient = new JsonObject();
        recipient.addProperty("email", toEmail);
        toArray.add(recipient);
        emailBody.add("to", toArray);

        emailBody.addProperty("subject", "Confirmation de Demande de Congé");

        // ✅ Email Content with PDF Download Button
        String htmlContent = "<html><body>" +
                "<h2>Confirmation de votre demande de congé</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Votre demande de congé a été enregistrée.</p>" +
                "<p>Téléchargez votre document officiel en cliquant sur le bouton ci-dessous :</p>" +
                "<a href='" + pdfUrl + "' target='_blank' style='background:#1e3c72;color:white;padding:10px 20px;border-radius:5px;text-decoration:none;'>📄 Télécharger le PDF</a>" +
                "<p>Cordialement,</p>" +
                "<p><b>Équipe RH</b></p>" +
                "</body></html>";

        emailBody.addProperty("htmlContent", htmlContent);

        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"), emailBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("api-key", API_KEY)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (response.isSuccessful()) {
                logger.info("✅ Email sent successfully to: " + toEmail);
                return true;
            } else {
                logger.severe("❌ Failed to send email. Response: " + responseBody);
                return false;
            }
        } catch (IOException e) {
            logger.severe("❌ Error sending email: " + e.getMessage());
            return false;
        }
    }
    public boolean sendStatusUpdateEmail(String toEmail, Conge conge, StatutConge newStatus) {
        OkHttpClient client = new OkHttpClient();

        JsonObject emailBody = new JsonObject();
        JsonObject sender = new JsonObject();
        sender.addProperty("name", SENDER_NAME);
        sender.addProperty("email", SENDER_EMAIL);
        emailBody.add("sender", sender);

        JsonArray toArray = new JsonArray();
        JsonObject recipient = new JsonObject();
        recipient.addProperty("email", toEmail);
        toArray.add(recipient);
        emailBody.add("to", toArray);

        emailBody.addProperty("subject", "Mise à jour de votre demande de congé");

        String statusMessage = newStatus == StatutConge.APPROVED ? "Approuvé ✅" : "Refusé ❌";
        String htmlContent = "<html><body>" +
                "<h2>Votre demande de congé a été mise à jour</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Le statut de votre demande de congé est maintenant : <b>" + statusMessage + "</b></p>" +
                "<p>Type de congé : " + conge.getTypeConge().name() + "</p>" +
                "<p>Date de début : " + conge.getDateDebut() + "</p>" +
                "<p>Date de fin : " + conge.getDateFin() + "</p>" +
                "<p>Cordialement,</p>" +
                "<p><b>Équipe RH</b></p>" +
                "</body></html>";

        emailBody.addProperty("htmlContent", htmlContent);

        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"), emailBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("api-key", API_KEY)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (response.isSuccessful()) {
                logger.info("✅ Email sent successfully to: " + toEmail);
                return true;
            } else {
                logger.severe("❌ Failed to send email. Response: " + responseBody);
                return false;
            }
        } catch (IOException e) {
            logger.severe("❌ Error sending email: " + e.getMessage());
            return false;
        }
    }

}
