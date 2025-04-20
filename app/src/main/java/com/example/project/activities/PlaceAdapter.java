package com.example.project.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.models.Place;

import java.util.List;

/**
 * Adapter for displaying Place items in a RecyclerView.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> placeList;
    private OnPlaceClickListener listener;

    /**
     * Interface for handling place item clicks.
     */
    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    /**
     * Constructor for the PlaceAdapter.
     * @param placeList List of places to display
     * @param listener Click listener for place items
     */
    public PlaceAdapter(List<Place> placeList, OnPlaceClickListener listener) {
        this.placeList = placeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.bind(place, listener);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    /**
     * ViewHolder for place items.
     */
    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewType;
        private TextView textViewAddress;
        private TextView textViewDescription;
        private Button buttonViewOnMap;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonViewOnMap = itemView.findViewById(R.id.buttonViewOnMap);
        }

        /**
         * Binds a place to the ViewHolder.
         * @param place The place to bind
         * @param listener Click listener for the place
         */
        public void bind(final Place place, final OnPlaceClickListener listener) {
            textViewName.setText(place.getName());
            textViewType.setText(place.getType());
            textViewAddress.setText(place.getAddress());
            textViewDescription.setText(place.getDescription());

            buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlaceClick(place);
                }
            });
        }
    }
}
