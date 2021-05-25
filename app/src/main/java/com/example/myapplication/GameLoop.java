package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final Game game;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public boolean running = true;

    public void setRunning(boolean s){
        running = s;
    }

    @Override
    public void run() {
        super.run();
        while (running){
            Canvas canvas;
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                game.drawAll(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
