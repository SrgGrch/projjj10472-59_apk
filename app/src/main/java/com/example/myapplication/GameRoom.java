package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.Log;

import com.example.myapplication.SocketHelpers.Building;
import com.example.myapplication.SocketHelpers.MapObject;
import com.example.myapplication.SocketHelpers.PlayerSocketData;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class GameRoom {
    private float width, height;
    private final MainPlayer mainPlayer;
    private final Paint gridPaint, playerPaint, borderPaint, cordsPaint, circlePaint, backgroundBuildPaint;
    private final Path path;
    private final float buildRadius;

    public GameRoom (float height, float width){
        this.mainPlayer = App.mainPlayer;
        this.width = width;
        this.height = height;

        gridPaint = new Paint();
        playerPaint = new Paint();
        borderPaint = new Paint();
        cordsPaint = new Paint();
        circlePaint = new Paint();
        backgroundBuildPaint = new Paint();

        gridPaint.setStyle(Paint.Style.FILL);
        gridPaint.setColor(Color.parseColor("#5e7f3a"));
        gridPaint.setStrokeWidth(2*App.q);

        playerPaint.setStyle(Paint.Style.FILL);
        playerPaint.setColor(Color.GREEN);

        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(Color.parseColor("#5e7f3a"));
        gridPaint.setStrokeWidth(4*App.q);

        cordsPaint.setColor(Color.WHITE);
        cordsPaint.setTextSize(30*App.q);

        circlePaint.setStrokeWidth(App.q*5);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setPathEffect(new DashPathEffect(new float[] {App.q*13, App.q*13}, 0f));
        circlePaint.setARGB(190, 255, 255, 255);
        circlePaint.setStrokeMiter(10f);

        backgroundBuildPaint.setARGB(50, 0, 0, 0);

        path = new Path();

        buildRadius = App.q*380;
    }

    public void draw(Canvas canvas){
        canvas.clipRect(0,0,canvas.getWidth(), canvas.getHeight());
        canvas.drawColor(Color.parseColor("#698d41"));
        
        float x = (float) mainPlayer.getX(), y = (float) mainPlayer.getY();

        float mapHeight = App.height * App.size;
        float mapWidth = App.width * App.size;

        float zeroY = height / 2 - y*App.q;
        float zeroX = width / 2 - x*App.q;

        for (float i = 0; i <= mapHeight; i += App.size){
            canvas.drawLine((zeroX), (zeroY+i*App.q), (zeroX+mapWidth*App.q), (zeroY+i*App.q), gridPaint);
        }
        for (float i = 0; i <= mapWidth; i += App.size){
            canvas.drawLine((zeroX+i*App.q), (zeroY), (zeroX+i*App.q), (zeroY+mapHeight*App.q), gridPaint);
        }

        canvas.drawLine((zeroX), (zeroY), (zeroX+mapWidth*App.q), (zeroY), borderPaint);
        canvas.drawLine((zeroX), (zeroY), (zeroX), (zeroY+mapHeight*App.q), borderPaint);
        canvas.drawLine((zeroX), (zeroY+mapHeight*App.q), (zeroX+mapWidth*App.q), (zeroY+mapHeight*App.q), borderPaint);
        canvas.drawLine((zeroX+mapWidth*App.q), (zeroY), (zeroX+mapWidth*App.q), (zeroY+mapHeight*App.q), borderPaint);

        List<Building.BuildingToDraw> btd = Building.getBuildingsToDraw();
        for (Building.BuildingToDraw b : btd){
            String name = b.building.getName();
            float _x = zeroX + b.x*App.q, _y = zeroY + b.y*App.q;
            canvas.drawBitmap(Building.getBitmap(name), _x, _y, null);
        }

        try {
            for (Player p : App.players.values()) {
                p.draw(canvas, zeroX + (float) (p.getX() * App.q), zeroY + (float) (p.getY() * App.q));
            }
        } catch (Exception ignore){}

        if (App.mainPlayer.isPlaying){
            App.mainPlayer.draw(canvas, width/2, height/2);
        }

        try {
            for (MapObject o : App.objects.values()) {
                o.draw(canvas, zeroX + o.getX() * App.q, zeroY + o.getY() * App.q);
            }
        } catch (Exception ignore){}

        // canvas.drawText("FPS: " + (int) fps(), 200, 200, cordsPaint);

        if (App.mainPlayer.isPlaying){
            App.joystick.draw(canvas);
            // drawCords(canvas);
        }
    }

    private void drawCords(Canvas canvas){
        canvas.drawText("X: " + (int) (mainPlayer.getX()), width - 125*App.q, 30*App.q, cordsPaint);
        canvas.drawText("Y: " + (int) (mainPlayer.getY()), width - 125*App.q, 65*App.q, cordsPaint);
    }


    public void update(int width, int height) {
        this.width = (float)width;
        this.height = (float)height;
    }
    LinkedList<Long> times = new LinkedList<Long>(){{
        add(System.nanoTime());
    }};
    private final int MAX_SIZE = 100;
    private final double NANOS = 1000000000.0;
    private double fps() {
        long lastTime = System.nanoTime();
        double difference = (lastTime - times.getFirst()) / NANOS;
        times.addLast(lastTime);
        int size = times.size();
        if (size > MAX_SIZE) {
            times.removeFirst();
        }
        return difference > 0 ? times.size() / difference : 0.0;
    }
}

//                        if (name.endsWith("_wall")){
//                            if (i == 0 || (App.buildings[i-1][j] != null && App.buildings[i-1][j].getName().endsWith("_wall"))){
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper_l"), _x, _y, null);
//
////                                Matrix m = new Matrix();
////                                m.setRotate(90, b.getSize()[0]*App.q/2f, b.getSize()[1]*App.q/2f);
////                                m.postTranslate(zeroX + i*App.size*App.q, zeroY + j*App.size*App.q);
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper"), m, null);
//                            }
//                            if (j == 0 || (App.buildings[i][j-1] != null && App.buildings[i][j-1].getName().endsWith("_wall"))){
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper_b"), _x, _y, null);
//
////                                Matrix m = new Matrix();
////                                m.setRotate(180, b.getSize()[0]*App.q/2f, b.getSize()[1]*App.q/2f);
////                                m.postTranslate(zeroX + i*App.size*App.q, zeroY + j*App.size*App.q);
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper"), m, null);
//                            }
//                            if (i+1 == App.width || (App.buildings[i+1][j] != null && App.buildings[i+1][j].getName().endsWith("_wall"))){
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper_r"), _x, _y, null);
//
////                                Matrix m = new Matrix();
////                                m.setRotate(270, b.getSize()[0]*App.q/2f, b.getSize()[1]*App.q/2f);
////                                m.postTranslate(zeroX + i*App.size*App.q, zeroY + j*App.size*App.q);
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper"), m, null);
//                            }
//                            if (j+1 == App.height || (App.buildings[i][j+1] != null && App.buildings[i][j+1].getName().endsWith("_wall"))){
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper_t"), _x, _y, null);
//
////                                Matrix m = new Matrix();
////                                m.setTranslate(zeroX + i*App.size*App.q, zeroY + j*App.size*App.q);
////                                canvas.drawBitmap(Building.getBitmap(b.getName() + "_helper"), m, null);
//                            }
//                        }