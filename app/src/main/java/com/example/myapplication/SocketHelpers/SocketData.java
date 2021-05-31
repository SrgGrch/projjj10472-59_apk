package com.example.myapplication.SocketHelpers;

import com.example.myapplication.helpers.ItemState;

import java.util.ArrayList;
import java.util.List;

// Class made for sending data from JSON
public class SocketData {
    private UseWeapon useWeapon;
    private LocationStates locationStates;
    private LoginData loginData;
    private ErrorData errorData;
    private NewStatesData newStatesData;
    private DamagedData damaged;

    public PlayerSocketData player;
    private UpdateBuilding updateBuilding;

    private ItemState[] itemStates;
    private InventoryItem[] inventory;


    public static class UseWeapon {
        public int useItemTime, length, user_id;
        public float degrees;
    }

    // fix

    public UpdateBuilding getUpdateBuilding() {
        return updateBuilding;
    }

    public LocationStates getLocationStates() {
        return locationStates;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public NewStatesData getNewStatesData() {
        return newStatesData;
    }

    public ItemState[] getItemStates() {
        return itemStates;
    }

    public PlayerSocketData getPlayer() {
        return player;
    }

    public InventoryItem[] getInventory() {
        return inventory;
    }

    public DamagedData getDamaged() {
        return damaged;
    }
}