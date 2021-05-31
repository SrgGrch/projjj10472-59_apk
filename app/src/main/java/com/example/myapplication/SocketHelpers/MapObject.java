package com.example.myapplication.SocketHelpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.myapplication.App;
import com.example.myapplication.R;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapObject extends MapObjectSocketData {
    private Bitmap b;
    private int size;
    private int rotation;

    public MapObject(int id, double x, double y, int r, String type){
        this.x = (float)x;
        this.y = (float)y;
        this.id = id;
        this.type = type;
        this.radius = r;

        size = (int)(this.radius * 2.5 * App.q);
        rotation = (int)(Math.random() * 360f);

        int resId = R.drawable.tree;
        if ("stone".equals(type)) {
            resId = R.drawable.stone;
        }

        Matrix m = new Matrix();
        Bitmap temp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(App.mainView.getResources(), resId), size, size, false);
        m.postRotate(rotation, size/2f, size/2f);
        b = Bitmap.createBitmap(temp, 0, 0, size, size, m, false);
    }

    public void draw(Canvas canvas, float x, float y){
        // m.postTranslate(1, 1);

        canvas.drawBitmap(b, x-b.getWidth()/2f, y-b.getHeight()/2f, null);
    }
}
