package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class CohereService {
    private static final String API_KEY = System.getenv("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"); // Use environment variable for security
    private static final String API_URL = "https://openrouter.ai/settings/keys"; // Cohere Generate API URL

    private final OkHttpClient client = new OkHttpClient();

    public String sendMessageToCohere(String userMessage) throws IOException {
        if (API_KEY == null || API_KEY.isEmpty()) {
            return "❌ Error: API key is missing! Please provide a valid API key.";
        }

        // Construct request body with the correct model name
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "medium"); // Correct model name for your account

        JsonArray promptArray = new JsonArray();
        promptArray.add(userMessage); // Pass user message as prompt
        jsonBody.add("prompts", promptArray);

        // Optional parameters like max tokens and temperature
        jsonBody.addProperty("max_tokens", 100);
        jsonBody.addProperty("temperature", 0.7);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        // Build request
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // Execute request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                return "❌ Error: " + response.code() + " - " + response.message() + "\nResponse: " + responseBody;
            }

            // Parse response
            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray generatedTextArray = jsonResponse.getAsJsonArray("text");

            if (generatedTextArray != null && generatedTextArray.size() > 0) {
                return generatedTextArray.get(0).getAsString(); // Return generated text
            } else {
                return "⚠️ No response from Cohere.";
            }
        }
    }
}
