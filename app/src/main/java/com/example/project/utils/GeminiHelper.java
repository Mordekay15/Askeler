package com.example.project.utils;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.project.R;
import com.example.project.models.Place;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiHelper {
    private static final String TAG = "GeminiHelper";
    private String API_URL;//= "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + getString(R.string.gemini_access_token);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final Context context;
    private final OkHttpClient client;
    private final Gson gson;

    public interface GeminiCallback {
        void onSuccess(List<Place> matchingPlaces);
        void onError(String errorMessage);
    }

    public GeminiHelper(Context context) {
        API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + context.getString(R.string.gemini_access_token);
        this.context = context;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    public void findMatchingPlaces(String query, List<Place> allPlaces, GeminiCallback callback) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.e(TAG, "No internet connection");
            callback.onError("No internet connection");
            return;
        }

        String placesJson = gson.toJson(allPlaces);

        String prompt = "Here is a list of places. Find those that match the query: \"" + query + "\".\n" +
                "Analyze the description of each place and return only those that match the query.\n" +
                "Return **only a string** that looks like a list of place names: [\"Place Name 1\", \"Place Name 2\"] — **without any extra comments, explanations, or formatting**.\n\n" +
                placesJson;

        try {
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject textPart = new JSONObject();
            textPart.put("text", prompt);
            JSONObject content = new JSONObject();
            content.put("parts", new JSONArray().put(textPart));
            contents.put(content);
            requestBody.put("contents", contents);

            Request request = new Request.Builder()
                    .url(API_URL) // 🔑 Don't forget to replace with your real API key
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toString(), JSON))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "API call failed", e);
                    callback.onError("Failed to connect to Gemini service: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            callback.onError("Gemini service error: " + response.code());
                            return;
                        }

                        String responseBody = response.body().string();
                        Log.d(TAG, "Raw Gemini response: " + responseBody);  // Log the entire raw response

                        // Parse the response into JSON
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Check if "candidates" exists in the response
                        if (!jsonResponse.has("candidates")) {
                            callback.onError("Response does not contain 'candidates' field");
                            return;
                        }

                        // Extract the content from the first candidate's parts
                        JSONArray candidates = jsonResponse.getJSONArray("candidates");
                        JSONObject candidate = candidates.getJSONObject(0);
                        JSONObject content = candidate.getJSONObject("content");
                        JSONArray parts = content.getJSONArray("parts");

                        // Get the actual text part (assuming the list of places is the first part)
                        String rawContent = parts.getJSONObject(0).getString("text").trim();

                        Log.d(TAG, "Raw Gemini content: " + rawContent);

                        // Clean the response from any extra characters and markdown
                        if (rawContent.startsWith("[") && rawContent.endsWith("]")) {
                            rawContent = rawContent.substring(1, rawContent.length() - 1).trim();  // Remove surrounding brackets
                        }

                        // Split the content into individual place names based on commas
                        String[] placeNamesArray = rawContent.split(",\\s*");

                        List<Place> matchingPlaces = new ArrayList<>();

                        // Match the place names with those from the list of all places
                        for (String placeName : placeNamesArray) {
                            placeName = placeName.replace("\"", "").trim();  // Remove any quotes around the name
                            for (Place place : allPlaces) {
                                if (place.getName().equalsIgnoreCase(placeName)) {
                                    matchingPlaces.add(place);
                                    break;
                                }
                            }
                        }

                        // Return the result via callback
                        callback.onSuccess(matchingPlaces);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing Gemini response", e);
                        callback.onError("Error processing Gemini response: " + e.getMessage());
                    }
                }



            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creating API request", e);
            callback.onError("Error creating Gemini request: " + e.getMessage());
        }
    }
}
