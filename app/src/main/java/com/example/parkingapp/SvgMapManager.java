package com.example.parkingapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;

public class SvgMapManager {
    private static final String TAG = "SvgMapManager";
    private static final float ZOOM_FACTOR = 1.15f;
    private static final float MIN_SCALE = 0.9f;
    private static final float MAX_SCALE = 3.0f;
    
    private final Context context;
    private final SVGImageView mapView;
    private float currentScale = 1f;
    private ParkingMap currentParkingMap;

    public SvgMapManager(Context context, SVGImageView mapView) {
        this.context = context;
        this.mapView = mapView;
    }

    public void loadMap(String mapName) {
        if (mapName == null || mapName.isEmpty()) {
            Log.e(TAG, "Invalid map name");
            Toast.makeText(context, "Invalid map name", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Loading map: " + mapName);
        try {
            SVG svg = SVG.getFromAsset(context.getAssets(), mapName);
            if (svg != null) {
                svg.setDocumentViewBox(0, 0, svg.getDocumentWidth(), svg.getDocumentHeight());
                svg.setDocumentHeight("100%");
                svg.setDocumentWidth("100%");
                
                mapView.setSVG(svg);
                updateParkingSpotColors();
                mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                
                currentScale = 1f;
                mapView.setScaleX(currentScale);
                mapView.setScaleY(currentScale);
                Log.d(TAG, "Map loaded successfully: " + mapName);
            } else {
                Log.e(TAG, "Failed to load SVG: " + mapName);
                Toast.makeText(context, "Failed to load map", Toast.LENGTH_SHORT).show();
            }
        } catch (SVGParseException | IOException e) {
            Log.e(TAG, "Error loading map: " + mapName, e);
            Toast.makeText(context, "Error loading map: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setCurrentParkingMap(ParkingMap parkingMap) {
        this.currentParkingMap = parkingMap;
        updateParkingSpotColors();
    }

    private void updateParkingSpotColors() {
        StringBuilder css = new StringBuilder();
        if (currentParkingMap != null) {
            for (ParkingSpot spot : currentParkingMap.getParkingSpots()) {
                if (!spot.isOccupied()) {
                    // Зеленый цвет для свободных мест
                    css.append("#").append(spot.getSpotNumber()).append("{fill:rgb(45, 141, 88);}");
                } else {
                    // Красный цвет для занятых мест
                    css.append("#").append(spot.getSpotNumber()).append("{fill:rgb(158, 130, 130);}");
                }
            }
        }
        mapView.setCSS(css.toString());
    }

    public void zoomIn() {
        if (currentScale < MAX_SCALE) {
            currentScale *= ZOOM_FACTOR;
            mapView.setScaleX(currentScale);
            mapView.setScaleY(currentScale);
        }
    }

    public void zoomOut() {
        if (currentScale > MIN_SCALE) {
            currentScale /= ZOOM_FACTOR;
            mapView.setScaleX(currentScale);
            mapView.setScaleY(currentScale);
        }
    }

    public float getCurrentScale() {
        return currentScale;
    }
} 