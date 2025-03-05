package services.yas;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenRouterService {
    private static final String API_KEY = "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"; // Your API key
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions"; // Correct URL

    private final OkHttpClient client = new OkHttpClient();

    public String sendMessageToOpenRouter(String userMessage) throws IOException {
        // Create JSON body for the API request
        JsonObject jsonBody = new JsonObject();
        JsonArray messages = new JsonArray();

        // Add user message to the "messages" array
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");  // Set the role to "user"
        message.addProperty("content", userMessage);  // Add the user input as content
        messages.add(message);

        // Add the messages array to the JSON body
        jsonBody.add("messages", messages);
        jsonBody.addProperty("model", "openai/gpt-3.5-turbo-0613");

        // Open the connection to the API
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Send the JSON data
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read the response from the API
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            try (InputStreamReader reader = new InputStreamReader(connection.getErrorStream())) {
                JsonElement response = JsonParser.parseReader(reader);
                System.out.println("Error Response: " + response.toString());
            }
            throw new IOException("Failed to communicate with API. Response code: " + responseCode);
        }

        // If the response is okay, process it
        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            JsonElement response = JsonParser.parseReader(reader);
            System.out.println("Full Response: " + response.toString());  // Log the full response

            // Safely handle empty or malformed responses
            if (response.isJsonObject() && response.getAsJsonObject().has("choices")) {
                JsonArray choicesArray = response.getAsJsonObject().getAsJsonArray("choices");
                if (choicesArray != null && choicesArray.size() > 0) {
                    JsonObject choicesObject = choicesArray.get(0).getAsJsonObject();

                    // Ensure the "message" field exists and is not null
                    if (choicesObject != null && choicesObject.has("message")) {
                        return choicesObject.getAsJsonObject("message").get("content").getAsString();
                    } else {
                        return "Message field missing in the response.";
                    }
                } else {
                    return "No choices found in the response.";
                }
            } else {
                return "Unexpected response format or no choices found.";
            }
        }
    }
}