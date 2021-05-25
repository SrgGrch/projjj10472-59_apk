package com.example.myapplication.SocketHelpers;

import java.util.List;

public class NewStatesData {
    private List<PlayerSocketData> players;
    private List<InventoryItem> inventory;
    private SocketData.UseWeapon useWeapon;
    private List<MapObjectSocketData> objects;

    public SocketData.UseWeapon getUseWeapon() {
        return useWeapon;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public List<PlayerSocketData> getPlayers() {
        return players;
    }

    public List<MapObjectSocketData> getObjects() {
        return objects;
    }
}
