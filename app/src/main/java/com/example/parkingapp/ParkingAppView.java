package com.example.parkingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ParkingAppView extends View {

    private Paint paint;

    public ParkingAppView(Context context) {
        super(context);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawParkingMap(canvas);
    }

    private void drawParkingMap(Canvas canvas){
        int spaceWidth = 100;
        int spaceHeight = 200;
        int startX = 50;
        int startY = 50;

        for(int i=0; i<10; i++){
            canvas.drawRect(startX + (i* (spaceWidth +20)),
                    startY,
                    startX + (i * (spaceWidth + 20)) + spaceWidth,
                    startY + spaceHeight, paint
                    );
        }

    }
}
