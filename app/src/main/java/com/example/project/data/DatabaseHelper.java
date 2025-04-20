package com.example.project.data;

import android.content.Context;
import android.util.Log;


import com.example.project.models.Place;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for loading and parsing the places.json data file.
 * Uses Gson for JSON parsing.
 */
public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String PLACES_JSON_FILE = "places.json";
    
    private final Context context;
    private List<Place> allPlaces;
    
    /**
     * Constructor that initializes the DatabaseHelper with a context.
     * @param context The application context
     */
    public DatabaseHelper(Context context) {
        this.context = context;
        this.allPlaces = new ArrayList<>();
        loadPlacesFromJson();
    }
    
    /**
     * Loads all places from the JSON file in assets.
     */
    private void loadPlacesFromJson() {
        try {
            String jsonString = readJsonFromAssets();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Place>>(){}.getType();
            allPlaces = gson.fromJson(jsonString, listType);
            Log.d(TAG, "Loaded " + allPlaces.size() + " places from JSON");
        } catch (IOException e) {
            Log.e(TAG, "Error loading places from JSON", e);
        }
    }
    
    /**
     * Reads the JSON file from assets as a string.
     * @return The JSON string
     * @throws IOException If the file cannot be read
     */
    private String readJsonFromAssets() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = context.getAssets().open(PLACES_JSON_FILE);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        
        bufferedReader.close();
        return stringBuilder.toString();
    }
    
    /**
     * Gets all places from the database.
     * @return List of all places
     */
    public List<Place> getAllPlaces() {
        return allPlaces;
    }
    
    /**
     * Gets places filtered by type.
     * @param type The type to filter by
     * @return List of places of the specified type
     */
    public List<Place> getPlacesByType(String type) {
        List<Place> filteredPlaces = new ArrayList<>();
        for (Place place : allPlaces) {
            if (place.getType().equalsIgnoreCase(type)) {
                filteredPlaces.add(place);
            }
        }
        return filteredPlaces;
    }
    
    /**
     * Gets a place by its name.
     * @param name The name to search for
     * @return The place with the specified name, or null if not found
     */
    public Place getPlaceByName(String name) {
        for (Place place : allPlaces) {
            if (place.getName().equalsIgnoreCase(name)) {
                return place;
            }
        }
        return null;
    }
}
