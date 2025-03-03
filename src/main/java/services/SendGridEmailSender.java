//package services;
//
//import com.sendgrid.*;
//import com.sendgrid.helpers.mail.Mail;
//import com.sendgrid.helpers.mail.objects.*;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Base64;
//
//public class SendGridEmailSender {
//
//    public static void sendEmailWithAttachment(String recipientEmail, String subject, String bodyText, String filePath) {
//        Email from = new Email("khiarisahar55@gmail.com"); // Remplace avec ton email
//        Email to = new Email(recipientEmail);
//        Content content = new Content("text/plain", bodyText);
//        Mail mail = new Mail(from, subject, to, content);
//
//        try {
//            // Lire le fichier QR code
//            byte[] fileData = readFileAsBytes(filePath);
//            String encodedFile = Base64.getEncoder().encodeToString(fileData);
//            Attachments attachments = new Attachments();
//            attachments.setFilename("QRCode.png");
//            attachments.setType("image/png");
//            attachments.setDisposition("attachment");
//            attachments.setContent(encodedFile);
//            mail.addAttachments(attachments);
//
//            // Envoi avec SendGrid
//            //SendGrid sg = new SendGrid("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"); // 🔹 Replace with your actual API key
//           // SendGrid sg = new SendGrid("  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");
//            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//
//            // Clé API dans les variables d'environnement
//            Request request = new Request();
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//
//            Response response = sg.api(request);
//            System.out.println("Email envoyé, statut : " + response.getStatusCode());
//        } catch (IOException e) {
//            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
//        }
//    }
//
//    private static byte[] readFileAsBytes(String filePath) throws IOException {
//        File file = new File(filePath);
//        FileInputStream fileInputStream = new FileInputStream(file);
//        byte[] fileData = new byte[(int) file.length()];
//        fileInputStream.read(fileData);
//        fileInputStream.close();
//        return fileData;
//    }
//}
