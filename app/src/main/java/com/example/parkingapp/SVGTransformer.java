package com.example.parkingapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;

public class SVGTransformer {

    public static final String SVGT = "SVGT";
    SVG svgDocument = null;
    Context context;
    SVGTransformer(Context context, String filename_asset){
        try{
            svgDocument = SVG.getFromAsset(context.getAssets(), filename_asset);
            this.context = context;
        }catch (SVGParseException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void changeSVG(){
        Log.d(SVGT, "Меняем SVG");

    }

    public void renderToImageView(ImageView view)
    {
            Drawable drawable = new PictureDrawable(svgDocument.renderToPicture());
            view.setImageDrawable(drawable);
        }
    }

