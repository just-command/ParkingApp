package com.example.parkingapp;

public class ParkingConfig {
    public static class MapConfig {
        private final String mapName;
        private final String svgFileName;
        private final int totalSpots;

        public MapConfig(String mapName, String svgFileName, int totalSpots) {
            this.mapName = mapName;
            this.svgFileName = svgFileName;
            this.totalSpots = totalSpots;
        }

        public String getMapName() {
            return mapName;
        }

        public String getSvgFileName() {
            return svgFileName;
        }

        public int getTotalSpots() {
            return totalSpots;
        }
    }

    public static final MapConfig MAP_1 = new MapConfig("Parking Map 1", "map1.svg", 20);
    public static final MapConfig MAP_2 = new MapConfig("Parking Map 2", "map2.svg", 30);
    public static final MapConfig MAP_3 = new MapConfig("Parking Map 3", "map3.svg", 25);

    public static MapConfig getMapConfig(String svgFileName) {
        if (MAP_1.getSvgFileName().equals(svgFileName)) {
            return MAP_1;
        } else if (MAP_2.getSvgFileName().equals(svgFileName)) {
            return MAP_2;
        } else if (MAP_3.getSvgFileName().equals(svgFileName)) {
            return MAP_3;
        }
        return null;
    }
} 