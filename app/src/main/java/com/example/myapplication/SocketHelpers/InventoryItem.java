package com.example.myapplication.SocketHelpers;

import com.example.myapplication.App;
import com.example.myapplication.helpers.ItemState;

public class InventoryItem {
    private int count;
    private String name;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemState getState(){
        return App.inventoryItemStats.get(name);
    }
}
