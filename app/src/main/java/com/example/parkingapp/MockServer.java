package com.example.parkingapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.util.Log;

public class MockServer {
    private static final String TAG = "MockServer";
    private static MockServer instance;
    private List<ParkingSpot> parkingSpots;
    private Random random;
    private String currentMap;

    private MockServer() {
        Log.d(TAG, "Initializing MockServer");
        random = new Random();
        parkingSpots = new ArrayList<>();
    }

    public static MockServer getInstance() {
        if (instance == null) {
            instance = new MockServer();
        }
        return instance;
    }

    public void setCurrentMap(String mapName) {
        Log.d(TAG, "Setting current map to: " + mapName);
        this.currentMap = mapName;
        initializeParkingSpots();
    }

    private void initializeParkingSpots() {
        Log.d(TAG, "Initializing parking spots for map: " + currentMap);
        parkingSpots.clear();

        switch (currentMap) {
            case "map1.svg":
                // Парковка 1: A1-A9, B1-B7, C1-C7, D1-D7
                String[] rows1 = {"A", "B", "C", "D"};
                int[] spotsPerRow1 = {9, 7, 7, 7};
                for (int i = 0; i < rows1.length; i++) {
                    for (int j = 1; j <= spotsPerRow1[i]; j++) {
                        String spotId = rows1[i] + j;
                        ParkingSpot spot = new ParkingSpot(spotId, random.nextBoolean());
                        parkingSpots.add(spot);
                        Log.d(TAG, "Created spot " + spot.getSpotNumber() + " with occupied state: " + spot.isOccupied());
                    }
                }
                break;

            case "map2.svg":
                // Парковка 2: A1-A6, B1-B6, C1-C6, D1-D6, E1-E6
                String[] rows2 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
                int spotsPerRow2 = 6;
                for (String row : rows2) {
                    for (int i = 1; i <= spotsPerRow2; i++) {
                        String spotId = row + i;
                        ParkingSpot spot = new ParkingSpot(spotId, random.nextBoolean());
                        parkingSpots.add(spot);
                        Log.d(TAG, "Created spot " + spot.getSpotNumber() + " with occupied state: " + spot.isOccupied());
                    }
                }
                break;

            case "map3.svg":
                // Парковка 3: A1-A15, B1-B12, C1-C6
                // A ряд: 15 мест
                for (int i = 1; i <= 15; i++) {
                    String spotId = "A" + i;
                    ParkingSpot spot = new ParkingSpot(spotId, random.nextBoolean());
                    parkingSpots.add(spot);
                    Log.d(TAG, "Created spot " + spot.getSpotNumber() + " with occupied state: " + spot.isOccupied());
                }
                // B ряд: 12 мест
                for (int i = 1; i <= 12; i++) {
                    String spotId = "B" + i;
                    ParkingSpot spot = new ParkingSpot(spotId, random.nextBoolean());
                    parkingSpots.add(spot);
                    Log.d(TAG, "Created spot " + spot.getSpotNumber() + " with occupied state: " + spot.isOccupied());
                }
                // C ряд: 6 мест
                for (int i = 1; i <= 12; i++) {
                    String spotId = "C" + i;
                    ParkingSpot spot = new ParkingSpot(spotId, random.nextBoolean());
                    parkingSpots.add(spot);
                    Log.d(TAG, "Created spot " + spot.getSpotNumber() + " with occupied state: " + spot.isOccupied());
                }

               for (int i = 1; i <= 17; i++) {
                    String spotId = "D" + i;
                    ParkingSpot spot = new ParkingSpot(spotId, random.nextBoolean());
                    parkingSpots.add(spot);
                    Log.d(TAG, "Created spot " + spot.getSpotNumber() + " with occupied state: " + spot.isOccupied());
                }


                break;
        }
    }

    public List<ParkingSpot> getParkingSpots() {
        Log.d(TAG, "Getting parking spots");
        // Имитируем случайное изменение состояния парковочных мест
        for (ParkingSpot spot : parkingSpots) {
            if (random.nextDouble() < 0.1) { // 10% шанс изменения состояния
                boolean newState = !spot.isOccupied();
                spot.setOccupied(newState);
                Log.d(TAG, "Changed spot " + spot.getSpotNumber() + " state to: " + newState);
            }
        }
        return new ArrayList<>(parkingSpots);
    }

    public ParkingSpot getParkingSpot(String id) {
        Log.d(TAG, "Getting parking spot with id: " + id);
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getSpotNumber().equals(id)) {
                Log.d(TAG, "Found spot " + id + " with occupied state: " + spot.isOccupied());
                return spot;
            }
        }
        Log.d(TAG, "Spot not found: " + id);
        return null;
    }
} 