package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAinActivity";
    private static final float ZOOM_FACTOR = 1.15f;
    private static  float currentScale = 1f;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ImageView mapView = findViewById(R.id.MapVeiw);
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        drawSVG(mapView);
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
        currentScale *= ZOOM_FACTOR;
        v.setScaleX(currentScale);
        v.setScaleY(currentScale);
    }

    private void zoomOut(ImageView v){
        currentScale /= ZOOM_FACTOR;
        v.setScaleX(currentScale);
        v.setScaleY(currentScale);
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


    private void drawSVG(ImageView imgV) {
        try{
            SVG svg = SVG.getFromAsset(this.getAssets(), "map1.svg");
            String l = svg.getDocumentDescription();
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            imgV.setImageDrawable(drawable);
        }catch (SVGParseException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Activity on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Activity on Stop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Activity on Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Activity on Resume");
    }
}