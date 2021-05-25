package com.example.myapplication.SocketHelpers;

import java.util.List;

public class LocationStates {
    private Building[][] buildings;
    private List<MapObject> objects;
    private List<PlayerSocketData> players;
    private int width, height, size;

    public Building[][] getBuildings() {
        return buildings;
    }

    public List<MapObject> getObjects() {
        return objects;
    }

    public List<PlayerSocketData> getPlayers() {
        return players;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }
}
