package com.example.project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.data.DatabaseHelper;
import com.example.project.models.Place;
import com.example.project.utils.GeminiHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener {
    private EditText editTextQuery;
    private Button buttonSearch;
    private ProgressBar progressBar;
    private TextView textViewNoResults;
    private RecyclerView recyclerViewPlaces;
    
    private DatabaseHelper databaseHelper;
    private GeminiHelper gemini;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize UI components
        editTextQuery = findViewById(R.id.editTextQuery);
        buttonSearch = findViewById(R.id.buttonSearch);
        progressBar = findViewById(R.id.progressBar);
        textViewNoResults = findViewById(R.id.textViewNoResults);
        recyclerViewPlaces = findViewById(R.id.recyclerViewPlaces);

        databaseHelper = new DatabaseHelper(this);
        gemini = new GeminiHelper(this);

        placeList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(placeList, this);
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlaces.setAdapter(placeAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = editTextQuery.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("SearchDebug", "Query: [" + query + "]");
        
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        textViewNoResults.setVisibility(View.GONE);
        placeList.clear();
        placeAdapter.notifyDataSetChanged();
        
        // Get all places from database
        List<Place> allPlaces = databaseHelper.getAllPlaces();
        
        // Use DeepSeek to find matching places
        gemini.findMatchingPlaces(query, allPlaces, new GeminiHelper.GeminiCallback() {
            @Override
            public void onSuccess(List<Place> matchingPlaces) {
                Log.d("SearchDebug", "onSuccess called, matches: " + matchingPlaces.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        
                        if (matchingPlaces.isEmpty()) {
                            textViewNoResults.setVisibility(View.VISIBLE);
                        } else {
                            placeList.addAll(matchingPlaces);
                            placeAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                Log.e("SearchDebug", "onError: " + errorMessage);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    
    @Override
    public void onPlaceClick(Place place) {
        // Open MapBoxActivity with the selected place
        Intent intent = new Intent(this,  GoogleMapActivity.class);
        intent.putExtra("place", place);
        startActivity(intent);
    }
}
