package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectMap extends AppCompatActivity {
    private static final String TAG = "SelectMap";
    private RadioGroup radioGroup;
    private Button submitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);

        radioGroup = findViewById(R.id.RadioGroup);
        submitButton = findViewById(R.id.SubmitButton);

        submitButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a parking map", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedMap = getMapFileName(selectedId);
            Log.d(TAG, "Selected map: " + selectedMap);
            
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedMap", selectedMap);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private String getMapFileName(int radioButtonId) {
        String mapFile;
        if (radioButtonId == R.id.radioButton1) {
            mapFile = ParkingConfig.MAP_1.getSvgFileName();
            Log.d(TAG, "Selected MAP1: " + mapFile);
        } else if (radioButtonId == R.id.radioButton2) {
            mapFile = ParkingConfig.MAP_2.getSvgFileName();
            Log.d(TAG, "Selected MAP2: " + mapFile);
        } else if (radioButtonId == R.id.radioButton3) {
            mapFile = ParkingConfig.MAP_3.getSvgFileName();
            Log.d(TAG, "Selected MAP3: " + mapFile);
        } else {
            Log.e(TAG, "Unknown radio button ID: " + radioButtonId);
            mapFile = ParkingConfig.MAP_1.getSvgFileName();
        }
        Log.d(TAG, "Returning map file: " + mapFile + " for radio button ID: " + radioButtonId);
        return mapFile;
    }
}

