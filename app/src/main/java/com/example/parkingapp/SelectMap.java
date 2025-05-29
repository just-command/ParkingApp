package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;


import com.example.parkingapp.databinding.ActivitySelectMapBinding;

public class SelectMap extends AppCompatActivity {

    private ActivitySelectMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectMapBinding.inflate(getLayoutInflater());

        setContentView(binding.SelectMap);

        Intent intent = new Intent(this, MainActivity.class);

        binding.SubmitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                }
        );

        };
    }