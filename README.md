# Askeler Android App Documentation

## Overview
Askeler is an Android application that allows users to search for establishments in Lappeenranta, Finland using natural language queries. The app leverages the Gemini AI model to analyze user queries and match them with appropriate establishments from a local database. Users can view the results in a list, see details about each place, and view locations on a map with routing capabilities.

## Features
- Natural language search for establishments in Lappeenranta
- AI-powered matching of queries to relevant places
- List view of search results with detailed information
- Map view with markers for selected locations
- Route creation from user's current location to selected establishments


## Technical Architecture

### Project Structure
```
com.askeler
├── activities
│   ├── MainActivity.java - Main screen with search functionality
│   ├── GoogleMapActivity.java - Map screen for viewing locations
│   └── PlaceAdapter.java - Adapter for displaying places in RecyclerView
├── models
│   └── Place.java - Data model for establishments
├── utils
│   └── GeminiHelper.java - Integration with Gemini AI model
├── data
│   └── DatabaseHelper.java - JSON data loading and parsing
└── AskelerApplication.java - Application class for global initialization
```

### Key Components

#### Place Model
The `Place` class represents an establishment with the following properties:
- name: Name of the establishment
- address: Physical address in Lappeenranta
- latitude/longitude: Geographic coordinates
- type: Type of establishment (e.g., Restaurant, Cafe)
- description: Detailed description of the establishment

#### Database Helper
The `DatabaseHelper` class handles loading and parsing the JSON data file containing establishment information. It provides methods for:
- Loading all places from JSON
- Filtering places by type
- Finding places by name

#### Gemini Helper
The `GeminiHelper` class manages the integration with the Gemini AI model. It:
- Formats user queries and place data for the AI model
- Sends requests to the Gemini API
- Processes responses to identify matching establishments

#### Main Activity
The `MainActivity` is the entry point of the application. It features:
- Search input field for user queries
- Search button to trigger the AI-powered search
- RecyclerView to display search results
- Integration with DeepSeekHelper for AI processing

#### Google Map Activity
The `GoogleMapActivity` displays selected establishments on a map using Google maps API. It provides:
- Map view with markers for establishments
- User location tracking
- Route creation from user location to selected establishment
- Permission handling for location services

## User Guide

### Searching for Places
1. Launch the Askeler app
2. Enter your query in the search field (e.g., "where to eat pasta at night")
3. Tap the "Search" button
4. View the list of matching establishments

### Viewing Place Details
Each place in the search results displays:
- Name of the establishment
- Type (Restaurant, Cafe, etc.)
- Address in Lappeenranta
- Description with details about offerings and atmosphere

### Using the Map
1. Tap "Show on map" for any establishment in the results list
2. The map will open showing the selected location with a marker
3. Your current location will be displayed (if location permissions are granted)
4. A route will be created from your location to the selected establishment
5. Use the floating action button to center the map on your location

## Technical Requirements

### Minimum Requirements
- Android 7.0 (API level 24) or higher
- Internet connection for Gemini API access
- Location services for mapping functionality

## Implementation Details

### Gemini Integration
The app integrates with the Gemini AI model through the API. The integration works as follows:
1. User query and place data are formatted into a prompt
2. The prompt is sent to the Gemini model via API
3. The model analyzes the query and place descriptions
4. The response is parsed to extract matching place names
5. Matching places are displayed in the UI


### Map Implementation
The map functionality is implemented using Google maps. Key features include:
- Marker overlays for places and user location
- Polyline overlay for route display

### Data Management
Place data is stored in a JSON file in the app's assets folder. The data includes establishments in Lappeenranta with details relevant for search matching. The DatabaseHelper class provides an interface for accessing this data throughout the app.


