package com.example.parkingapp;

import java.util.ArrayList;
import java.util.List;

public class ParkingMap {
    private String mapName;
    private String svgFileName;
    private List<ParkingSpot> parkingSpots;

    public ParkingMap(String mapName, String svgFileName) {
        this.mapName = mapName;
        this.svgFileName = svgFileName;
        this.parkingSpots = new ArrayList<>();
    }

    public String getMapName() {
        return mapName;
    }

    public String getSvgFileName() {
        return svgFileName;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void addParkingSpot(ParkingSpot spot) {
        parkingSpots.add(spot);
    }

    public ParkingSpot getParkingSpot(String spotNumber) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getSpotNumber().equals(spotNumber)) {
                return spot;
            }
        }
        return null;
    }
} 