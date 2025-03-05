package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import services.OpenAIService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChatAssistantController {

    @FXML private ListView<String> chatListView;
    @FXML private TextField messageField;
    @FXML private Button sendButton;

    private Timeline typingAnimation;
    private final Map<String, Image> imageCache = new HashMap<>();

    @FXML
    private void initialize() {
        chatListView.setCellFactory(param -> new ChatCell());
        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String userMessage = messageField.getText().trim();

        if (!userMessage.isEmpty()) {
            System.out.println("📤 [INFO] User sent message: " + userMessage);


            Platform.runLater(() -> {
                chatListView.getItems().add("USER: " + userMessage);
                chatListView.scrollTo(chatListView.getItems().size() - 1);
                System.out.println("🟢 [UI] Added user message to chat list.");
            });

            messageField.clear();


            Platform.runLater(() -> {
                chatListView.getItems().add("BOT: Typing...");
                chatListView.scrollTo(chatListView.getItems().size() - 1);
                applyTypingAnimation();
            });

            CompletableFuture.supplyAsync(() -> OpenAIService.askGPT(userMessage))
                    .thenAccept(botResponse -> Platform.runLater(() -> {
                        if (typingAnimation != null) {
                            typingAnimation.stop();
                        }
                        chatListView.getItems().remove(chatListView.getItems().size() - 1); // Remove typing indicator

                        if (botResponse != null && !botResponse.isEmpty()) {
                            chatListView.getItems().add("BOT: " + botResponse);
                            chatListView.scrollTo(chatListView.getItems().size() - 1);
                            System.out.println("🟢 [UI] Added bot response to chat list.");
                        } else {
                            chatListView.getItems().add("BOT: ❌ No response from AI.");
                            chatListView.scrollTo(chatListView.getItems().size() - 1);
                            System.out.println("🔴 [UI] No response received from AI.");
                        }

                        System.out.println("📜 [DEBUG] Current Chat Messages: " + chatListView.getItems());
                    }));
        } else {
            System.out.println("⚠️ [WARNING] Empty message. Ignoring.");
        }
    }

    private void applyTypingAnimation() {
        typingAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    int lastIndex = chatListView.getItems().size() - 1;
                    String lastMessage = chatListView.getItems().get(lastIndex);
                    if (lastMessage.equals("BOT: Typing...")) {
                        chatListView.getItems().set(lastIndex, "BOT: Typing.");
                    } else if (lastMessage.equals("BOT: Typing.")) {
                        chatListView.getItems().set(lastIndex, "BOT: Typing..");
                    } else if (lastMessage.equals("BOT: Typing..")) {
                        chatListView.getItems().set(lastIndex, "BOT: Typing...");
                    }
                }),
                new KeyFrame(Duration.seconds(0.3))
        );
        typingAnimation.setCycleCount(Timeline.INDEFINITE);
        typingAnimation.play();
    }

    private class ChatCell extends ListCell<String> {
        @Override
        protected void updateItem(String message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox messageContainer = new VBox();
                messageContainer.setSpacing(5);

                if (message.contains("🖼️ Here is your generated image: ")) {
                    String prefix = "🖼️ Here is your generated image: ";
                    String url = message.substring(message.indexOf(prefix) + prefix.length()).trim();
                    Text text = new Text("Generated Image:");
                    text.setStyle("-fx-font-size: 14px;");
                    messageContainer.getChildren().add(text);

                    Image cachedImage = imageCache.get(url);
                    if (cachedImage != null) {
                        ImageView imageView = new ImageView(cachedImage);
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);
                        messageContainer.getChildren().add(imageView);
                        System.out.println("DEBUG: Used cached image for: " + url);
                    } else {
                        ImageView imageView = new ImageView();
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);
                        messageContainer.getChildren().add(imageView);

                        CompletableFuture.supplyAsync(() -> loadImage(url))
                                .thenAccept(image -> Platform.runLater(() -> {
                                    if (image != null) {
                                        imageCache.put(url, image);
                                        imageView.setImage(image);
                                        System.out.println("DEBUG: Loaded and cached image from: " + url);
                                    } else {
                                        Text errorText = new Text("Image failed to load. Click here to view: " + url);
                                        errorText.setStyle("-fx-font-size: 14px; -fx-fill: #0066cc; -fx-underline: true;");
                                        errorText.setOnMouseClicked(event -> {
                                            try {
                                                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                                            } catch (Exception ex) {
                                                System.err.println("ERROR: Could not open URL: " + ex.getMessage());
                                            }
                                        });
                                        messageContainer.getChildren().set(1, errorText);
                                    }
                                }));
                    }
                } else {
                    Text text = new Text(message.replace("USER: ", "").replace("BOT: ", ""));
                    text.setWrappingWidth(450);
                    text.setStyle("-fx-font-size: 14px;");
                    messageContainer.getChildren().add(text);
                }

                messageContainer.setStyle(message.startsWith("USER:")
                        ? "-fx-background-color: #0078FF; -fx-padding: 12px; -fx-background-radius: 20px 20px 0 20px; -fx-text-fill: white;"
                        : "-fx-background-color: #E1E1E1; -fx-padding: 12px; -fx-background-radius: 20px 20px 20px 0; -fx-text-fill: black;");

                HBox messageBox = new HBox(messageContainer);
                messageBox.setAlignment(message.startsWith("USER:") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                messageBox.setSpacing(10);
                messageBox.setMinWidth(500);
                messageBox.setMaxWidth(500);

                setGraphic(messageBox);
            }
        }

        private Image loadImage(String url) {
            try {
                System.out.println("DEBUG: Fetching image from: " + url);
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.connect();
                int responseCode = connection.getResponseCode();
                System.out.println("DEBUG: Image fetch response code: " + responseCode);

                if (responseCode == 200) {
                    try (InputStream inputStream = connection.getInputStream()) {
                        return new Image(inputStream);
                    }
                } else {
                    System.err.println("ERROR: Failed to fetch image, HTTP " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                System.err.println("ERROR: Failed to load image from " + url + ": " + e.getMessage());
                return null;
            }
        }
    }
}