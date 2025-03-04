package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import service.GeminiService;

public class ChatbotController {

    @FXML
    private VBox chatBox;

    @FXML
    private TextField messageInput;

    private GeminiService geminiService;

    @FXML
    public void initialize() {
        geminiService = new GeminiService();
        // Message de bienvenue
        addBotMessage("Bonjour ! Je suis votre assistant IA pour la gestion de projet. Comment puis-je vous aider ?");
    }

    @FXML
    private void handleSendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            // Afficher le message de l'utilisateur
            addUserMessage(message);
            
            // Vider le champ de saisie
            messageInput.clear();
            
            // Obtenir et afficher la réponse du bot
            getBotResponse(message);
        }
    }

    @FXML
    private void handleSuggestion(ActionEvent event) {
        Button button = (Button) event.getSource();
        String question = button.getText();
        messageInput.setText(question);
        handleSendMessage();
    }

    private void addUserMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 10px; -fx-padding: 10px;");
        text.setStyle("-fx-fill: white;");

        messageBox.getChildren().add(textFlow);
        chatBox.getChildren().add(messageBox);
    }

    private void addBotMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 10px; -fx-padding: 10px;");

        messageBox.getChildren().add(textFlow);
        chatBox.getChildren().add(messageBox);
    }

    private void getBotResponse(String message) {
        // Afficher un message "en cours de réponse"
        HBox typingBox = new HBox();
        typingBox.setAlignment(Pos.CENTER_LEFT);
        typingBox.setPadding(new Insets(5, 10, 5, 10));
        Label typingLabel = new Label("En train de répondre...");
        typingBox.getChildren().add(typingLabel);
        chatBox.getChildren().add(typingBox);

        // Appeler Gemini dans un thread séparé
        new Thread(() -> {
            try {
                String response = geminiService.getResponse(message);
                
                // Mettre à jour l'interface utilisateur dans le thread JavaFX
                Platform.runLater(() -> {
                    // Supprimer le message "en cours de réponse"
                    chatBox.getChildren().remove(typingBox);
                    // Ajouter la réponse
                    addBotMessage(response);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    chatBox.getChildren().remove(typingBox);
                    addBotMessage("Désolé, je n'ai pas pu traiter votre demande. Erreur : " + e.getMessage());
                });
            }
        }).start();
    }
} 