package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main activity for the parking app that displays and manages interactive SVG maps.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAinActivity";
    private static final String STATES = "STATES";
    private static final float ZOOM_FACTOR = 1.15f;
    private static final float MIN_SCALE = 0.9f;
    private static final float MAX_SCALE = 3.0f;
    private static  float currentScale = 1f;

    private static final String MAP1 = "map1.svg";
    private static final String MAP2 = "map2.svg";
    private static final String MAP3 = "map3.svg";
    
    private final Map<String, SVG> svgCache = new HashMap<>();
    private SVGImageView mapView;
    private long backPressedTime;
    private Toast backToast;

    /**
     * Loads and displays the specified map
     * @param mapName name of the map file to load
     */
    private void loadMap(String mapName) {
        if (mapName == null || mapName.isEmpty()) {
            Log.e(TAG, "Invalid map name");
            return;
        }

        try {
            SVG svg = getSVGFromCache(mapName);
            if (svg != null) {
                svg.setDocumentViewBox(0, 0, svg.getDocumentWidth(), svg.getDocumentHeight());
                svg.setDocumentHeight("100%");
                svg.setDocumentWidth("100%");
                
                mapView.setSVG(svg);
                mapView.setCSS("#C5{fill: #234fd3ff;}");
                mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                
                // Reset scale when loading new map
                currentScale = 1f;
                mapView.setScaleX(currentScale);
                mapView.setScaleY(currentScale);
            }
        } catch (SVGParseException e) {
            Log.e(TAG, "Error loading map: " + mapName, e);
            Toast.makeText(this, "Error loading map", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets SVG from cache or loads it from assets
     * @param mapName name of the map file
     * @return SVG object or null if loading failed
     */
    private SVG getSVGFromCache(String mapName) {
        if (!svgCache.containsKey(mapName)) {
            try {
                SVG svg = SVG.getFromAsset(getAssets(), mapName);
                svgCache.put(mapName, svg);
            } catch (SVGParseException | IOException e) {
                Log.e(TAG, "Error loading SVG from assets: " + mapName, e);
                return null;
            }
        }
        return svgCache.get(mapName);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(STATES, "Создается Main Activity");
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
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
        loadMap(MAP3); // Load default map

        mapView.setOnTouchListener(createListenerForMoveMap());
        setZooming(mapView);

        ImageButton selectMap = findViewById(R.id.SelectMapButton);

        Intent intent = new Intent(this, SelectMap.class);
        selectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }


    private void setZooming(ImageView mapView) {
        ImageButton zoomInButton =  findViewById(R.id.ZoomInButton);
        zoomInButton.setOnClickListener(v -> zoomIn(mapView));

        ImageButton zoomOutButton = findViewById(R.id.ZoomOutButton);
        zoomOutButton.setOnClickListener(v -> zoomOut(mapView));
    }


    private void zoomIn(ImageView v){
        if (currentScale < MAX_SCALE) {
            currentScale *= ZOOM_FACTOR;
            v.setScaleX(currentScale);
            v.setScaleY(currentScale);
        }
    }

    private void zoomOut(ImageView v){
        if (currentScale > MIN_SCALE) {
            currentScale /= ZOOM_FACTOR;
            v.setScaleX(currentScale);
            v.setScaleY(currentScale);
        }
    }

    private View.OnTouchListener createListenerForMoveMap(){
        return new View.OnTouchListener(){
            float dX, dY;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
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
                return true;}
                 };}


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(STATES, "Activity on pause");
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
    }
}