package services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import models.Projet;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PDFService {
    
    public void generateProjetPDF(Projet projet) throws Exception {
        // Créer le document PDF
        Document document = new Document(PageSize.A4);
        String fileName = "Projet_" + projet.getId_projet() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Ajouter un en-tête avec logo (optionnel)
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(20);
        
        // Titre principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        titleFont.setColor(new BaseColor(44, 62, 80)); // Couleur bleu foncé
        Paragraph title = new Paragraph("Détails du Projet", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell headerCell = new PdfPCell(title);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setBackgroundColor(new BaseColor(236, 240, 241)); // Fond gris clair
        headerCell.setPadding(20);
        headerTable.addCell(headerCell);
        document.add(headerTable);

        // Tableau des informations du projet
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(20);
        infoTable.setSpacingAfter(20);

        // Style pour les labels
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        labelFont.setColor(new BaseColor(44, 62, 80));

        // Style pour les valeurs
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        valueFont.setColor(new BaseColor(52, 73, 94));

        // Ajouter les informations avec style
        addInfoRow(infoTable, "ID du projet", String.valueOf(projet.getId_projet()), labelFont, valueFont);
        addInfoRow(infoTable, "Nom du projet", projet.getNom_projet(), labelFont, valueFont);
        addInfoRow(infoTable, "Date limite", projet.getDeadline().toString(), labelFont, valueFont);
        addInfoRow(infoTable, "Manager", projet.getManager(), labelFont, valueFont);
        addInfoRow(infoTable, "Client", projet.getNom_client(), labelFont, valueFont);
        addInfoRow(infoTable, "Équipe", projet.getEquipe().getNomEquipe(), labelFont, valueFont);

        document.add(infoTable);

        // Section QR Code
        PdfPTable qrTable = new PdfPTable(1);
        qrTable.setWidthPercentage(100);
        qrTable.setSpacingBefore(20);

        // Titre QR Code
        Font qrTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        qrTitleFont.setColor(new BaseColor(44, 62, 80));
        Paragraph qrTitle = new Paragraph("QR Code du Projet", qrTitleFont);
        qrTitle.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell qrTitleCell = new PdfPCell(qrTitle);
        qrTitleCell.setBorder(Rectangle.NO_BORDER);
        qrTitleCell.setPadding(10);
        qrTable.addCell(qrTitleCell);

        // Générer et ajouter le QR Code
        String qrContent = String.format("ID: %d\nProjet: %s\nManager: %s\nClient: %s\nÉquipe: %s",
            projet.getId_projet(), 
            projet.getNom_projet(),
            projet.getManager(),
            projet.getNom_client(),
            projet.getEquipe().getNomEquipe());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
        
        String qrFileName = "qr_" + projet.getId_projet() + ".png";
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Path.of(qrFileName));

        Image qrImage = Image.getInstance(qrFileName);
        qrImage.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell qrCell = new PdfPCell(qrImage);
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCell.setPadding(20);
        qrTable.addCell(qrCell);

        document.add(qrTable);

        // Pied de page
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        footerTable.setSpacingBefore(20);

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        footerFont.setStyle(Font.ITALIC);
        footerFont.setColor(new BaseColor(127, 140, 141));
        Paragraph footer = new Paragraph("Document généré le " + new java.util.Date(), footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell footerCell = new PdfPCell(footer);
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerCell.setPadding(10);
        footerTable.addCell(footerCell);
        document.add(footerTable);

        // Fermer le document
        document.close();

        // Supprimer le fichier QR temporaire
        Files.deleteIfExists(Path.of(qrFileName));

        // Ouvrir le PDF
        openPDF(fileName);
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(10);
        labelCell.setBackgroundColor(new BaseColor(236, 240, 241));
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(10);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void openPDF(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                System.err.println("Le fichier PDF n'existe pas : " + fileName);
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + file.getAbsolutePath());
            } else {
                Runtime.getRuntime().exec("xdg-open " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture du PDF : " + e.getMessage());
        }
    }

} 