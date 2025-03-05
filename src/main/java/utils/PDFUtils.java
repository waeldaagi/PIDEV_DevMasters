package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.Conge;
import okhttp3.*;

import java.io.IOException;
import java.util.logging.Logger;

public class PDFUtils {

    private static final Logger logger = Logger.getLogger(PDFUtils.class.getName());
    private static final String API_KEY = "azizkaboudi123@gmail.com_iIOz8jw5Cizt5qPKihIhslvO9FO525kcmJWRsRSS0MxSFtibayAbfx08TZ9KU06Z";
    private static final String BASE_URL = "https://api.pdf.co/v1/pdf/convert/from/html";

    public static String generatePDF(String userEmail, Conge conge, String imageUrl) {
        OkHttpClient client = new OkHttpClient();

        // ✅ Extract only the required fields
        String typeConge = conge.getTypeConge().name();
        String dateDebut = conge.getDateDebut().toString();
        String dateFin = conge.getDateFin().toString();

        // ✅ PDF HTML Content (Styled with CSS)
        String htmlContent = "<html><head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; text-align: center; padding: 30px; background: #f7f7f7; }" +
                ".container { max-width: 600px; margin: auto; padding: 20px; border: 2px solid #2a5298; border-radius: 10px; background: white; box-shadow: 0px 5px 15px rgba(0,0,0,0.2); }" +
                "h1 { color: #2a5298; }" +
                "p { font-size: 16px; color: #333; line-height: 1.5; }" +
                ".details { text-align: left; background: #f4f4f4; padding: 15px; border-radius: 8px; margin-top: 10px; }" +
                ".label { font-weight: bold; color: #1e3c72; }" +
                ".image { margin-top: 15px; border-radius: 10px; max-width: 100%; border: 2px solid #1e3c72; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<h1>Confirmation de Congé</h1>" +
                "<p>Votre demande de congé a été enregistrée avec succès.</p>" +
                "<div class='details'>" +
                "<p><span class='label'>Type de Congé:</span> " + typeConge + "</p>" +
                "<p><span class='label'>Date de Début:</span> " + dateDebut + "</p>" +
                "<p><span class='label'>Date de Fin:</span> " + dateFin + "</p>";

        // ✅ Add Image if available
        if (imageUrl != null && !imageUrl.isEmpty()) {
            htmlContent += "<p><span class='label'>Justificatif:</span></p><img class='image' src='" + imageUrl + "' alt='Justificatif du Congé' />";
        }

        htmlContent += "<p><b>Merci de votre confiance.</b></p></div></div></body></html>";

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("html", htmlContent);
        jsonBody.addProperty("name", "conge_" + userEmail + ".pdf");

        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("x-api-key", API_KEY)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                if (jsonResponse.has("url")) {
                    String pdfUrl = jsonResponse.get("url").getAsString();
                    logger.info("✅ PDF generated successfully: " + pdfUrl);
                    return pdfUrl;
                }
            }
        } catch (IOException e) {
            logger.severe("❌ Error generating PDF: " + e.getMessage());
        }
        return null;
    }
}
