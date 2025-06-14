package com.example.parkingapp;

public class ParkingSpot {
    private String spotNumber;
    private boolean isOccupied;

    public ParkingSpot(String spotNumber, boolean isOccupied) {
        this.spotNumber = spotNumber;
        this.isOccupied = isOccupied;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
} 