package com.example.myapplication.SocketHelpers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.myapplication.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Building {
    private String name;
    private int[] size;

    public int[] getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    // static
    private static final Map<String, Bitmap> buildingBitmaps = new HashMap<>();
    private static List<BuildingToDraw> buildingsToDraw = new ArrayList<>();

    public static Bitmap getBitmap(String name){
        return buildingBitmaps.get(name);
    }

    public static void addBitmap(String name, Bitmap b){
        buildingBitmaps.put(name, b);
    }

    public static List<BuildingToDraw> getBuildingsToDraw() {
        return buildingsToDraw;
    }
    public static void updateBuildingsToDraw(){
        Building[][] temp = App.buildings;
        List<BuildingToDraw> newB = new ArrayList<>();
        if (temp != null) {
            for (int i = 0; i < temp.length; i++) {
                for (int j = 0; j < temp[i].length; j++) {
                    if (temp[i][j] != null) {
                        newB.add(new BuildingToDraw(temp[i][j], i * App.size, j * App.size));
                    }
                }
            }
        }
        buildingsToDraw = newB;
    }
    public static class BuildingToDraw {
        public Building building;
        public float x, y;
        public BuildingToDraw(Building building, float x, float y){
            this.building = building;
            this.x = x;
            this.y = y;
        }
    }
}
