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


import com.caverock.androidsvg.SVGImageView;

import java.util.List;


/**
 * Main activity for the parking app that displays and manages interactive SVG maps.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String STATES = "STATES";
    private SVGImageView mapView;
    private long backPressedTime;
    private Toast backToast;
    private ParkingMap currentParkingMap;
    private MockServer mockServer;
    private static final long UPDATE_INTERVAL = 5000; // 5 секунд
    private Handler updateHandler;
    private Runnable updateRunnable;
    private SvgMapManager svgMapManager;

    private final ActivityResultLauncher<Intent> mapSelectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedMap = result.getData().getStringExtra("selectedMap");
                    Log.d(TAG, "Received selected map: " + selectedMap);
                    if (selectedMap != null) {
                        svgMapManager.loadMap(selectedMap);
                        initializeParkingMap(selectedMap);
                    }
                }
            });


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
                if (currentParkingMap != null) {
                    // Обновляем состояние парковочных мест
                    List<ParkingSpot> updatedSpots = mockServer.getParkingSpots();
                    for (ParkingSpot updatedSpot : updatedSpots) {
                        ParkingSpot currentSpot = currentParkingMap.getParkingSpot(updatedSpot.getSpotNumber());
                        if (currentSpot != null) {
                            currentSpot.setOccupied(updatedSpot.isOccupied());
                        }
                    }
                    svgMapManager.setCurrentParkingMap(currentParkingMap);
                }
                updateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };

        mapView = findViewById(R.id.MapView);
        svgMapManager = new SvgMapManager(this, mapView);
        
        String defaultMap = ParkingConfig.MAP_1.getSvgFileName();
        svgMapManager.loadMap(defaultMap);
        initializeParkingMap(defaultMap);

        mapView.setOnTouchListener(createListenerForMoveMap());
        setZooming();

        ImageButton selectMap = findViewById(R.id.SelectMapButton);
        selectMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectMap.class);
            mapSelectionLauncher.launch(intent);
        });

        // Запускаем обновления
        updateHandler.post(updateRunnable);
    }

    private void setZooming() {
        ImageButton zoomInButton = findViewById(R.id.ZoomInButton);
        zoomInButton.setOnClickListener(v -> svgMapManager.zoomIn());

        ImageButton zoomOutButton = findViewById(R.id.ZoomOutButton);
        zoomOutButton.setOnClickListener(v -> svgMapManager.zoomOut());
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
        svgMapManager.setCurrentParkingMap(currentParkingMap);
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
            svgMapManager.loadMap(mapName);
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