package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.util.List;

/**
 * Main activity for the parking app that displays and manages interactive SVG maps.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String STATES = "STATES";
    private static final float ZOOM_FACTOR = 1.15f;
    private static final float MIN_SCALE = 0.9f;
    private static final float MAX_SCALE = 3.0f;
    private static float currentScale = 1f;
    private SVGImageView mapView;
    private long backPressedTime;
    private Toast backToast;
    private ParkingMap currentParkingMap;
    private MockServer mockServer;
    private static final long UPDATE_INTERVAL = 5000; // 5 секунд
    private Handler updateHandler;
    private Runnable updateRunnable;

    private final ActivityResultLauncher<Intent> mapSelectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedMap = result.getData().getStringExtra("selectedMap");
                    Log.d(TAG, "Received selected map: " + selectedMap);
                    if (selectedMap != null) {
                        loadMap(selectedMap);
                        initializeParkingMap(selectedMap);
                    }
                }
            });

    private void updateParkingSpotColors() {
        if (mapView != null && currentParkingMap != null) {
            Log.d(TAG, "Updating parking spot colors");
            StringBuilder css = new StringBuilder();
            for (ParkingSpot spot : currentParkingMap.getParkingSpots()) {
                if (!spot.isOccupied()) {
                    // Зеленый цвет для свободных мест
                    css.append("#").append(spot.getSpotNumber()).append("{fill:rgb(41, 122, 68);}");
                    Log.d(TAG, "Setting spot " + spot.getSpotNumber() + " to green (free)");
                } else {
                    // Красный цвет для занятых мест
                    css.append("#").append(spot.getSpotNumber()).append("{fill:rgb(95, 73, 73);}");
                    Log.d(TAG, "Setting spot " + spot.getSpotNumber() + " to red (occupied)");
                }
            }
            String cssString = css.toString();
            Log.d(TAG, "Applying CSS: " + cssString);
            mapView.setCSS(cssString);
        } else {
            Log.e(TAG, "Cannot update colors: mapView or currentParkingMap is null");
        }
    }

    private void initializeParkingMap(String mapName) {
        Log.d(TAG, "Initializing parking map: " + mapName);
        ParkingConfig.MapConfig mapConfig = ParkingConfig.getMapConfig(mapName);
        if (mapConfig == null) {
            Log.e(TAG, "Unknown map configuration for: " + mapName);
            Toast.makeText(this, "Error: Unknown map configuration", Toast.LENGTH_SHORT).show();
            return;
        }

        // Уведомляем MockServer о смене карты
        mockServer.setCurrentMap(mapName);

        currentParkingMap = new ParkingMap(mapConfig.getMapName(), mapConfig.getSvgFileName());
        
        // Get parking spots from MockServer
        List<ParkingSpot> spots = mockServer.getParkingSpots();
        for (ParkingSpot spot : spots) {
            currentParkingMap.addParkingSpot(spot);
        }
        
        Log.d(TAG, "Initialized " + currentParkingMap.getParkingSpots().size() + " parking spots");
        
        // Обновляем цвета парковочных мест
        updateParkingSpotColors();
    }

    /**
     * Loads and displays the specified map
     * @param mapName name of the map file to load
     */
    private void loadMap(String mapName) {
        if (mapName == null || mapName.isEmpty()) {
            Log.e(TAG, "Invalid map name");
            Toast.makeText(this, "Invalid map name", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Loading map: " + mapName);
        try {
            SVG svg = SVG.getFromAsset(getAssets(), mapName);
            if (svg != null) {
                svg.setDocumentViewBox(0, 0, svg.getDocumentWidth(), svg.getDocumentHeight());
                svg.setDocumentHeight("100%");
                svg.setDocumentWidth("100%");
                
                mapView.setSVG(svg);
                
                // Обновляем CSS для окраски парковочных мест
                StringBuilder css = new StringBuilder();
                if (currentParkingMap != null) {
                    for (ParkingSpot spot : currentParkingMap.getParkingSpots()) {
                        if (!spot.isOccupied()) {
                            // Зеленый цвет для свободных мест
                            css.append("#").append(spot.getSpotNumber()).append("{fill:rgb(45, 141, 88);}");
                        } else {
                            // Красный цвет для занятых мест
                            css.append("#").append(spot.getSpotNumber()).append("{fill:rgb(90, 73, 73);}");
                        }
                    }
                }
                mapView.setCSS(css.toString());
                mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                
                currentScale = 1f;
                mapView.setScaleX(currentScale);
                mapView.setScaleY(currentScale);
                Log.d(TAG, "Map loaded successfully: " + mapName);
            } else {
                Log.e(TAG, "Failed to load SVG: " + mapName);
                Toast.makeText(this, "Failed to load map", Toast.LENGTH_SHORT).show();
            }
        } catch (SVGParseException | IOException e) {
            Log.e(TAG, "Error loading map: " + mapName, e);
            Toast.makeText(this, "Error loading map: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(STATES, "Creating Main Activity");
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Initialize MockServer
        mockServer = MockServer.getInstance();
        Log.d(TAG, "MockServer initialized");
        
        // Initialize update handler
        updateHandler = new Handler(Looper.getMainLooper());
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Running update cycle");
                if (currentParkingMap != null) {
                    // Обновляем состояние парковочных мест
                    List<ParkingSpot> updatedSpots = mockServer.getParkingSpots();
                    Log.d(TAG, "Got " + updatedSpots.size() + " updated spots from MockServer");
                    for (ParkingSpot updatedSpot : updatedSpots) {
                        ParkingSpot currentSpot = currentParkingMap.getParkingSpot(updatedSpot.getSpotNumber());
                        if (currentSpot != null) {
                            currentSpot.setOccupied(updatedSpot.isOccupied());
                            Log.d(TAG, "Updated spot " + currentSpot.getSpotNumber() + " to occupied: " + currentSpot.isOccupied());
                        }
                    }
                    // Обновляем цвета
                    updateParkingSpotColors();
                } else {
                    Log.e(TAG, "Cannot update: currentParkingMap is null");
                }
                // Планируем следующее обновление
                updateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
        
        // Setup back press handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    finishAffinity();
                    return;
                } else {
                    backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });

        mapView = findViewById(R.id.MapView);
        String defaultMap = ParkingConfig.MAP_1.getSvgFileName();
        loadMap(defaultMap);
        initializeParkingMap(defaultMap);

        mapView.setOnTouchListener(createListenerForMoveMap());
        setZooming(mapView);

        ImageButton selectMap = findViewById(R.id.SelectMapButton);
        selectMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectMap.class);
            mapSelectionLauncher.launch(intent);
        });
    }

    private void setZooming(ImageView mapView) {
        ImageButton zoomInButton = findViewById(R.id.ZoomInButton);
        zoomInButton.setOnClickListener(v -> zoomIn(mapView));

        ImageButton zoomOutButton = findViewById(R.id.ZoomOutButton);
        zoomOutButton.setOnClickListener(v -> zoomOut(mapView));
    }

    private void zoomIn(ImageView v) {
        if (currentScale < MAX_SCALE) {
            currentScale *= ZOOM_FACTOR;
            v.setScaleX(currentScale);
            v.setScaleY(currentScale);
        }
    }

    private void zoomOut(ImageView v) {
        if (currentScale > MIN_SCALE) {
            currentScale /= ZOOM_FACTOR;
            v.setScaleX(currentScale);
            v.setScaleY(currentScale);
        }
    }

    private View.OnTouchListener createListenerForMoveMap() {
        return new View.OnTouchListener() {
            float dX, dY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        v.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;

                    default:
                        return false;
                }
                return true;
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(STATES, "Activity on pause");
        // Останавливаем обновления при приостановке активности
        updateHandler.removeCallbacks(updateRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(STATES, "Activity on Stop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String mapName = intent != null ? intent.getStringExtra("name") : null;
        if (mapName != null) {
            Log.d(TAG, "Loading map: " + mapName);
            loadMap(mapName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(STATES, "Activity on Resume");
        // Запускаем обновления при возобновлении активности
        updateHandler.post(updateRunnable);
    }
}