package com.example.project.models;

import java.io.Serializable;

/**
 * Model class representing a place in Lappeenranta.
 * This class is used for storing and managing place data loaded from JSON.
 */
public class Place implements Serializable {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String type;
    private String description;

    // Default constructor required for Gson
    public Place() {
    }

    /**
     * Constructor with all fields
     */
    public Place(String name, String address, double latitude, double longitude, String type, String description) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
