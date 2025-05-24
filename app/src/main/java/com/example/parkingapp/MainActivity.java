package com.example.parkingapp;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ImageView imgV = findViewById(R.id.MapVeiw);
        imgV.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        try{
            SVG svg = SVG.getFromAsset(this.getAssets(), "map1.svg");
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            imgV.setImageDrawable(drawable);
        }catch (SVGParseException | IOException e){
            System.out.println(e.getMessage());
        }
    }
}