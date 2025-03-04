package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Projet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Date;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class DetailsProjetController {

    @FXML private Label idLabel;
    @FXML private Label nomLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label managerLabel;
    @FXML private Label clientLabel;
    @FXML private Label equipeLabel;
    @FXML private Label countdownLabel;

    private Projet projet;
    private Timer timer;
    private boolean notificationEnvoyee = false;
    private TrayIcon trayIcon;

    public void setProjet(Projet projet) {
        this.projet = projet;
        afficherDetails();
        demarrerCompteARebours();
        initialiserTrayIcon();
    }

    private void initialiserTrayIcon() {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                
                // Créer une icône simple
                int trayIconWidth = 16;
                int trayIconHeight = 16;
                BufferedImage image = new BufferedImage(trayIconWidth, trayIconHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = (Graphics2D) image.getGraphics();
                
                // Dessiner un cercle bleu comme icône
                g2d.setColor(new Color(52, 152, 219)); // Bleu
                g2d.fillOval(0, 0, trayIconWidth - 1, trayIconHeight - 1);
                g2d.setColor(new Color(41, 128, 185)); // Bleu foncé pour le contour
                g2d.drawOval(0, 0, trayIconWidth - 1, trayIconHeight - 1);
                g2d.dispose();

                trayIcon = new TrayIcon(image, "Gestionnaire de Projets");
                trayIcon.setImageAutoSize(true);
                
                try {
                    tray.add(trayIcon);
                    // Envoyer une notification de test pour confirmer que ça fonctionne
                    trayIcon.displayMessage(
                        "Gestionnaire de Projets",
                        "Surveillance des deadlines activée",
                        TrayIcon.MessageType.INFO
                    );
                } catch (AWTException e) {
                    System.err.println("Impossible d'ajouter l'icône au système tray: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du tray icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherDetails() {
        idLabel.setText(String.valueOf(projet.getId_projet()));
        nomLabel.setText(projet.getNom_projet());
        deadlineLabel.setText(projet.getDeadline().toString());
        managerLabel.setText(projet.getManager());
        clientLabel.setText(projet.getNom_client());
        equipeLabel.setText(projet.getEquipe().getNomEquipe());
    }

    private void demarrerCompteARebours() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDate aujourdhui = LocalDate.now();
                Date sqlDate = projet.getDeadline();
                LocalDate deadline = sqlDate.toLocalDate();
                
                long joursRestants = ChronoUnit.DAYS.between(aujourdhui, deadline);
                
                // Pour tester, on envoie la notification immédiatement
                if (!notificationEnvoyee) {
                    envoyerNotification();
                    notificationEnvoyee = true;
                }

                // Mettre à jour le compte à rebours
                javafx.application.Platform.runLater(() -> {
                    if (joursRestants > 0) {
                        countdownLabel.setText(joursRestants + " jours restants");
                        countdownLabel.setStyle("-fx-text-fill: #e74c3c;");
                    } else if (joursRestants == 0) {
                        countdownLabel.setText("Date limite aujourd'hui!");
                        countdownLabel.setStyle("-fx-text-fill: #e74c3c;");
                    } else {
                        countdownLabel.setText("Projet en retard de " + Math.abs(joursRestants) + " jours");
                        countdownLabel.setStyle("-fx-text-fill: #c0392b;");
                    }
                });
            }
        }, 0, 1000 * 5); // Vérifier toutes les 5 secondes pour le test
    }

    private void envoyerNotification() {
        try {
            String titre = "Rappel de deadline";
            String message = "Le projet '" + projet.getNom_projet() + "' arrive à échéance bientôt!";
            
            if (SystemTray.isSupported() && trayIcon != null) {
                // Utiliser TrayIcon pour la notification
                trayIcon.displayMessage(
                    titre,
                    message,
                    TrayIcon.MessageType.WARNING
                );
            } else {
                // Fallback pour les systèmes qui ne supportent pas TrayIcon
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    Runtime.getRuntime().exec(new String[] {
                        "powershell.exe",
                        "-Command",
                        "Add-Type -AssemblyName System.Windows.Forms; [System.Windows.Forms.MessageBox]::Show('" 
                        + message + "', '" + titre + "', [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Warning)"
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFermerButtonClick() {
        if (timer != null) {
            timer.cancel();
        }
        // Retirer l'icône du system tray
        if (SystemTray.isSupported() && trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
        Stage stage = (Stage) countdownLabel.getScene().getWindow();
        stage.close();
    }
} 