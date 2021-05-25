//package com.example.myapplication;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.myapplication.Fragments.EntryFragment;
//import com.example.myapplication.Fragments.LoadingFragment;
//
//import java.util.LinkedList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class Game extends SurfaceView implements SurfaceHolder.Callback {
//    public Game(Context context) {
//        super(context);
//        setZOrderOnTop(false);
//
//        SurfaceHolder surfaceHolder = getHolder();
//        surfaceHolder.addCallback(this);
//    }
//
//    @Override
//    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        float margin = App.getDP(20);
//        float innerRadius = App.getDP(20);
//        float outerRadius = App.getDP(40);
//
//        if (App.q == 0){
//            App.q = getHeight() / 800f;
//        }
//        if (App.joystick == null){
//            App.joystick = new Joystick(margin + outerRadius, getHeight() - margin - outerRadius, outerRadius, innerRadius);
//        }
//        if (App.mainPlayer == null){
//            App.mainPlayer = new MainPlayer();
//        }
//        if (App.gameRoom == null){
//            App.gameRoom = new GameRoom(getHeight(), getWidth());
//        }
//        App.gameLoop = new GameLoop(this, getHolder());
//        App.gameLoop.start();
//
//        // Connect to WebSocket server
//        if (App.api == null){
//            App.api = new API();
//            String token = App.prefs.getString("token", null);
//            if (token == null){
//                App.fm
//                        .beginTransaction()
//                        .add(App.mainView.getId(), new EntryFragment(), App.TAGS.ENTRY_SCREEN)
//                        .commit();
//            } else {
//                App.api.emit("loginUsingToken", new LoginData_token(token));
//            }
//        }
//    }
//
//    @Override
//    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//        App.gameRoom.update(getWidth(), getHeight());
//    }
//
//    @Override
//    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//        boolean retry = true;
//        App.gameLoop.setRunning(false);
//        while (retry) {
//            try {
//                App.gameLoop.join();
//                retry = false;
//            } catch (InterruptedException ignore) {}
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!App.mainPlayer.isPlaying) return true;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
////                if (App.joystick.isPressed((double) event.getX(), (double) event.getY())) {
////                    App.joystick.setIsPressed(true);
////                }
//                // if (!App.buildModeEnabled){
//                    if (event.getX() < getWidth()/2f){
//                        App.joystick.setIsPressed(true);
//                        App.joystick.setPosition(event.getX(), event.getY());
//                    }
////                } else if (Math.pow(getWidth()/2f - event.getX(), 2) +  Math.pow(getHeight()/2f - event.getY(), 2) <= Math.pow(380*App.q, 2)){
////                        // todo
////                } else {
////                    App.joystick.setIsPressed(true);
////                    App.joystick.setPosition(event.getX(), event.getY());
////                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (App.joystick.getIsPressed()) {
//                    App.joystick.setActuator(event.getX(), event.getY());
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                App.joystick.resetActuator();
//                break;
//            default:
//                return super.onTouchEvent(event);
//        }
//        App.joystick.update();
//        return true;
//    }
//
//    public void drawAll(Canvas canvas) {
//        App.gameRoom.draw(canvas);
//    }
//
//    public void endGame(){
//        // Remove interface
//        Fragment gameInterface = App.fm.findFragmentByTag(App.TAGS.GAME_INTERFACE_SCREEN);
//        Fragment inventoryFragment = App.fm.findFragmentByTag(App.TAGS.INVENTORY_SCREEN);
//        Fragment craftsFragment = App.fm.findFragmentByTag(App.TAGS.CRAFTS_SCREEN);
//        FragmentTransaction ft = App.fm.beginTransaction();
//        if (gameInterface != null) ft.remove(gameInterface);
//        if (inventoryFragment != null) ft.remove(inventoryFragment);
//        if (craftsFragment != null) ft.remove(craftsFragment);
//        ft.commit();
//
//        App.players.clear();
//        App.objects.clear();
//        App.inventory = null;
//        App.buildings = null;
//        App.mainPlayer.isPlaying = false;
//    }
//
//    private static class LoginData_token {
//        private final String token;
//        public LoginData_token (String token){
//            this.token = token;
//        }
//    }
//
//}


package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.EntryFragment;
import com.example.myapplication.Fragments.LoadingFragment;
import com.example.myapplication.SocketHelpers.Building;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends SurfaceView implements SurfaceHolder.Callback {
    public Game(Context context) {
        super(context);
        setZOrderOnTop(false);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        float margin = App.getDP(20);
        float innerRadius = App.getDP(20);
        float outerRadius = App.getDP(40);

        if (App.q == 0){
            App.q = getHeight() / 800f;
        }
        if (App.joystick == null){
            App.joystick = new Joystick(margin + outerRadius, getHeight() - margin - outerRadius, outerRadius, innerRadius);
        }
        if (App.mainPlayer == null){
            App.mainPlayer = new MainPlayer();
        }
        if (App.gameRoom == null){
            App.gameRoom = new GameRoom(getHeight(), getWidth());
        }
        App.gameLoop = new GameLoop(this, getHolder());
        App.gameLoop.start();

        // Connect to WebSocket server
        if (App.api == null){
            App.api = new API();
            String token = App.prefs.getString("token", null);
            if (token == null){
                App.fm
                        .beginTransaction()
                        .add(App.mainView.getId(), new EntryFragment(), App.TAGS.ENTRY_SCREEN)
                        .commit();
            } else {
                App.api.emit("loginUsingToken", new LoginData_token(token));
            }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        App.gameRoom.update(getWidth(), getHeight());
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        App.gameLoop.setRunning(false);
        while (retry) {
            try {
                App.gameLoop.join();
                retry = false;
            } catch (InterruptedException ignore) {}
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!App.mainPlayer.isPlaying) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (App.joystick.isPressed((double) event.getX(), (double) event.getY())) {
//                    App.joystick.setIsPressed(true);
//                }
                // if (!App.buildModeEnabled){
                if (event.getX() < getWidth()/2f){
                    App.joystick.setIsPressed(true);
                    App.joystick.setPosition(event.getX(), event.getY());
                }
//                } else if (Math.pow(getWidth()/2f - event.getX(), 2) +  Math.pow(getHeight()/2f - event.getY(), 2) <= Math.pow(380*App.q, 2)){
//                        // todo
//                } else {
//                    App.joystick.setIsPressed(true);
//                    App.joystick.setPosition(event.getX(), event.getY());
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (App.joystick.getIsPressed()) {
                    App.joystick.setActuator(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                App.joystick.resetActuator();
                break;
            default:
                return super.onTouchEvent(event);
        }
        App.joystick.update();
        return true;
    }

    public void drawAll(Canvas canvas) {
        App.gameRoom.draw(canvas);
    }

    public void endGame(){
        // Remove interface
        Fragment gameInterface = App.fm.findFragmentByTag(App.TAGS.GAME_INTERFACE_SCREEN);
        Fragment inventoryFragment = App.fm.findFragmentByTag(App.TAGS.INVENTORY_SCREEN);
        Fragment craftsFragment = App.fm.findFragmentByTag(App.TAGS.CRAFTS_SCREEN);
        Fragment circleFragment = App.fm.findFragmentByTag(App.TAGS.CIRCLE_SCREEN);
        FragmentTransaction ft = App.fm.beginTransaction();
        if (gameInterface != null) ft.remove(gameInterface);
        if (inventoryFragment != null) ft.remove(inventoryFragment);
        if (craftsFragment != null) ft.remove(craftsFragment);
        if (circleFragment != null) ft.hide(circleFragment);
        ft.commit();

        App.players.clear();
        App.objects.clear();
        App.inventory = null;
        App.buildings = null;
        App.mainPlayer.isPlaying = false;
        Building.updateBuildingsToDraw();
    }

    private static class LoginData_token {
        private final String token;
        public LoginData_token (String token){
            this.token = token;
        }
    }

}
