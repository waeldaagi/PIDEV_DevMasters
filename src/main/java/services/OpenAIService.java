package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class OpenAIService {
    private static final String API_KEY = "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";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String DALL_E_URL = "https://api.openai.com/v1/images/generations";

    private static final String[] GREETINGS = {
            "Hello! How can I assist you with HR, business, or finance today?",
            "Hi there! Ready to boost your business or workforce?",
            "Greetings! What HR or business questions do you have today?",
            "Hey! Looking to improve your company's growth?",
            "Good day! How can I help with your HR and business strategies?"
    };

    private static boolean isGreeting(String message) {
        String lowerMessage = message.toLowerCase();
        return lowerMessage.matches(".*\\b(hello|hi|hey|greetings|good morning|good evening|good afternoon|yo)\\b.*");
    }

    private static boolean isImageRequest(String message) {
        String lowerMessage = message.toLowerCase();
        System.out.println("DEBUG: Checking if '" + lowerMessage + "' is an image request");
        boolean isImage = (lowerMessage.contains("image") || lowerMessage.contains("picture") ||
                lowerMessage.contains("photo") || lowerMessage.contains("illustration") ||
                lowerMessage.contains("visual") || lowerMessage.contains("draw") ||
                lowerMessage.contains("render")) &&
                (lowerMessage.contains("generate") || lowerMessage.contains("create") ||
                        lowerMessage.contains("make")) ||
                (lowerMessage.contains("more realistic") || lowerMessage.contains("better") ||
                        lowerMessage.contains("improved") || lowerMessage.contains("real")) &&
                        (lowerMessage.contains("it") || lowerMessage.contains("image"));
        System.out.println("DEBUG: isImageRequest result: " + isImage);
        return isImage;
    }

    public static String askGPT(String userMessage) {
        System.out.println("DEBUG: Processing message: " + userMessage);
        if (isGreeting(userMessage)) {
            System.out.println("DEBUG: Detected greeting");
            return GREETINGS[new Random().nextInt(GREETINGS.length)];
        }


        boolean isValidTopic = isTopicRelevant(userMessage);
        if (!isValidTopic) {
            return "⚠️ I can only answer questions related to Human Resources, business, or financial growth.";
        }


        if (isImageRequest(userMessage)) {
            System.out.println("DEBUG: Image request detected");
            String prompt = cleanImagePrompt(userMessage);
            return generateImage(prompt);
        }

        System.out.println("DEBUG: Routing to GPT for text response");
        return fetchGPTResponse(userMessage);
    }

    private static boolean isTopicRelevant(String userMessage) {
        try {
            URL url = new URL(OPENAI_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInput = "{"
                    + "\"model\": \"gpt-4\","
                    + "\"messages\": ["
                    + "{\"role\": \"system\", \"content\": \"You are a classifier. Determine if the following user input is relevant to HR, business, or financial growth, including requests to generate images related to these topics. Respond with 'yes' or 'no' only.\"},"
                    + "{\"role\": \"user\", \"content\": \"" + userMessage + "\"}"
                    + "],"
                    + "\"temperature\": 0.0"
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            int responseCode = connection.getResponseCode();
            System.out.println("GPT TOPIC CHECK RESPONSE CODE: " + responseCode);
            if (responseCode != 200) {
                System.err.println("ERROR: Topic check failed with HTTP " + responseCode);
                return false;
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            String result = jsonResponse.getAsJsonArray("choices").get(0)
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("content").getAsString().trim().toLowerCase();

            System.out.println("DEBUG: GPT-4 topic check result: " + result);
            return "yes".equals(result);
        } catch (Exception e) {
            System.err.println("GPT TOPIC CHECK ERROR: " + e.getMessage());
            return false;
        }
    }

    private static String cleanImagePrompt(String userMessage) {
        String lowerMessage = userMessage.toLowerCase().trim();
        String cleaned = lowerMessage
                .replace("generate image of", "")
                .replace("create an image of", "")
                .replace("make an image of", "")
                .replace("draw", "")
                .replace("illustration of", "")
                .replace("visual representation of", "")
                .replace("picture of", "")
                .replace("photo of", "")
                .replace("make me", "")
                .replace("it doesnt look real so", "")
                .replace("more realistic please", "realistic")
                .replace("more realistic", "realistic")
                .replace("better", "high-quality")
                .replace("improved", "detailed")
                .replace("more real", "realistic")
                .trim();
        if (cleaned.isEmpty() || cleaned.equals("realistic") || cleaned.equals("high-quality") || cleaned.equals("detailed") || cleaned.equals("it")) {
            cleaned = "a business man";
        }
        return cleaned + " in a realistic style";
    }

    private static String fetchGPTResponse(String userMessage) {
        try {
            URL url = new URL(OPENAI_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInput = "{"
                    + "\"model\": \"gpt-4\","
                    + "\"messages\": [{\"role\": \"system\", \"content\": \"You are an AI assistant specializing in HR, business growth, hiring employees, maximizing income, and resource management. Ignore unrelated topics.\"},"
                    + "{\"role\": \"user\", \"content\": \"" + userMessage + "\"}],"
                    + "\"temperature\": 0.7"
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            int responseCode = connection.getResponseCode();
            System.out.println("GPT RESPONSE CODE: " + responseCode);
            if (responseCode != 200) {
                Scanner errorScanner = new Scanner(connection.getErrorStream());
                StringBuilder errorResponse = new StringBuilder();
                while (errorScanner.hasNext()) {
                    errorResponse.append(errorScanner.nextLine());
                }
                errorScanner.close();
                return "❌ Error contacting GPT-4 API: HTTP " + responseCode + " " + errorResponse.toString();
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonResponse.getAsJsonArray("choices").get(0)
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("content").getAsString();

        } catch (Exception e) {
            System.err.println("GPT ERROR: " + e.getMessage());
            e.printStackTrace();
            return "❌ Error contacting OpenAI API. Please try again.";
        }
    }

    private static String generateImage(String prompt) {
        try {
            URL url = new URL(DALL_E_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInput = "{"
                    + "\"model\": \"dall-e-3\","
                    + "\"prompt\": \"" + prompt + "\","
                    + "\"n\": 1,"
                    + "\"size\": \"1024x1024\""
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            int responseCode = connection.getResponseCode();
            System.out.println("DALL-E RESPONSE CODE: " + responseCode);
            if (responseCode != 200) {
                Scanner errorScanner = new Scanner(connection.getErrorStream());
                StringBuilder errorResponse = new StringBuilder();
                while (errorScanner.hasNext()) {
                    errorResponse.append(errorScanner.nextLine());
                }
                errorScanner.close();
                return "❌ Error generating image: HTTP " + responseCode + " " + errorResponse.toString();
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            String imageUrl = jsonResponse.getAsJsonArray("data").get(0)
                    .getAsJsonObject().get("url").getAsString();

            return "🖼️ Here is your generated image: " + imageUrl;

        } catch (Exception e) {
            System.err.println("DALL-E ERROR: " + e.getMessage());
            e.printStackTrace();
            return "❌ Error generating image: " + e.getMessage();
        }
    }
}