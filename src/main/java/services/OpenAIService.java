package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class OpenAIService {
    // 🔴 Do NOT hardcode API keys in the code. Use environment variables or a config file instead.
    private static final String API_KEY = "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"; // Replace this with a secure way to store your API key
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();

    public String sendMessageToGPT(String userMessage) throws IOException {
        if (API_KEY == null || API_KEY.isEmpty()) {
            return "❌ Error: API key is missing! Please provide a valid API key.";
        }

        // ✅ Construct request body
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "gpt-3.5-turbo"); // Change if needed

        JsonArray messagesArray = new JsonArray();
        JsonObject userMessageObj = new JsonObject();
        userMessageObj.addProperty("role", "user");
        userMessageObj.addProperty("content", userMessage);
        messagesArray.add(userMessageObj);

        jsonBody.add("messages", messagesArray);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        // ✅ Build request
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // ✅ Execute request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Handle response errors
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject error = jsonResponse.getAsJsonObject("error");

                if (error != null && error.has("code") && error.get("code").getAsString().equals("insufficient_quota")) {
                    return "❌ Error: You have exceeded your quota. Please check your plan and billing details.";
                }

                return "❌ Error: " + response.code() + " - " + response.message() + "\nResponse: " + responseBody;
            }

            // ✅ Parse response from GPT
            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = jsonResponse.getAsJsonArray("choices");

            if (choices != null && choices.size() > 0) {
                return choices.get(0).getAsJsonObject().getAsJsonObject("message").get("content").getAsString();
            } else {
                return "⚠️ No response from GPT.";
            }
        } catch (IOException e) {
            return "❌ Error: Something went wrong while sending the request. " + e.getMessage();
        }
    }}
