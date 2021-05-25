package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.GameInterfaceFragment;
import com.example.myapplication.Fragments.LoadingFragment;
import com.example.myapplication.Fragments.MessageFragment;
import com.example.myapplication.SocketHelpers.Building;
import com.example.myapplication.SocketHelpers.InventoryItem;
import com.example.myapplication.SocketHelpers.MapObject;
import com.example.myapplication.helpers.ItemState;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class App {
    public static String PACKAGE_NAME;
    public static API api = null;
    public static MainPlayer mainPlayer = null;
    public static GameRoom gameRoom = null;
    public static GameLoop gameLoop = null;
    public static Joystick joystick = null;
    public static FragmentManager fm = null;

    public static Map<Integer, Player> players = new HashMap<>();
    public static Map<Integer, MapObject> objects = new HashMap<>();
    public static Map<String, ItemState> inventoryItemStats = new LinkedHashMap<>();

    public static float RADIUS = 40;
    public static float q = 0;
    public static int width = 1, height = 1, size = 80;
    public static boolean interfaceReady = false, buildModeEnabled = false;

    // public static String wsHostName = "ws://79.174.12.154";
    public static String wsHostName = "ws://217.71.129.139:4490";
    public static FrameLayout mainView;
    public static SharedPreferences prefs;
    public static InventoryItem[] inventory;

    public static Building[][] buildings;

    public static class TAGS {
        public static final String GAME_SCREEN = "GAME_SCREEN_TAG";
        public static final String LOGIN_SCREEN = "LOGIN_SCREEN_TAG";
        public static final String REGISTRATION_SCREEN = "REGISTRATION_SCREEN_TAG";
        public static final String ENTRY_SCREEN = "ENTRY_SCREEN_TAG";
        public static final String MENU_SCREEN = "MENU_SCREEN_TAG";
        public static final String INVENTORY_SCREEN = "INVENTORY_SCREEN_TAG";
        public static final String GAME_INTERFACE_SCREEN = "GAME_INTERFACE_SCREEN_TAG";
        public static final String LOADING_SCREEN = "LOADING_SCREEN_TAG";
        public static final String CRAFTS_SCREEN = "CRAFTS_SCREEN_TAG";
        public static final String BUILD_MODE_SCREEN = "BUILD_MODE_SCREEN_TAG";
        public static final String CIRCLE_SCREEN = "CIRCLE_SCREEN_FRAGMENT";
    }

    public static class Images {
        private static final Map<String, Bitmap> images = new HashMap<>();

        public static Bitmap getImage(String imgName){
            return images.get(imgName);
        }
        public static void addImage(String imgName, Bitmap b){
            images.put(imgName, b);
        }
    }

    public static void showMessage(String message){
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setMessage(message);
        fm
                .beginTransaction()
                .add(mainView.getId(), messageFragment)
                .commit();
    }
    public static LoadingFragment showLoading(String message){
        LoadingFragment f = LoadingFragment.newInstance(message);
        FragmentTransaction ft = App.fm.beginTransaction()
                 .setCustomAnimations(R.animator.animation_fade_in, R.animator.animation_in_without_effect);
        Fragment exitingFragment = App.fm.findFragmentByTag(TAGS.LOADING_SCREEN);
        if (exitingFragment != null){
            ft.remove(exitingFragment);
        }
        ft.add(App.mainView.getId(), f, TAGS.LOADING_SCREEN);
        ft.commit();
        return f;
    }
    public static LoadingFragment showLoading(){
        return showLoading(null);
    }
    public static void hideLoading(LoadingFragment f){
        App.fm.beginTransaction()
                .remove(f)
                .commit();
    }
    public static void hideLoading(){
        Fragment exitingFragment = App.fm.findFragmentByTag(TAGS.LOADING_SCREEN);
        if (exitingFragment != null){
            App.fm.beginTransaction()
                    .remove(exitingFragment)
                    .commit();
        }
    }

    // bitmapFromResource generator
    public static Bitmap getBitmap (int res){
        return BitmapFactory.decodeResource(App.mainView.getContext().getResources(), res);
    }
    public static Bitmap getScaledBitmap (int res, int width, int height){
        return Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(App.mainView.getContext().getResources(), res),
                width,
                height,
                false);
    }
    /**
     * return mainView.getResources().getDisplayMetrics().density * n;
     */
    public static float getDP(float n){
        return mainView.getResources().getDisplayMetrics().density * n;
    }



    public static Bitmap getBitmapFromVectorDrawable(int drawableId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(mainView.getContext(), drawableId);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (drawable != null) {
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }
    public static void updateInventory() {
        GameInterfaceFragment gameInterface = (GameInterfaceFragment) App.fm.findFragmentByTag(TAGS.GAME_INTERFACE_SCREEN);
        if (gameInterface != null){
            gameInterface.updateInventory();
        }
    }
    public static int inventoryCountItems(String name) {
        if (inventory == null) return 0;
        int count = 0;
        for (InventoryItem ii : inventory) {
            if (ii != null && ii.getName().equals(name)) {
                count += ii.getCount();
            }
        }
        return count;
    }
}
