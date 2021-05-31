package com.example.myapplication.helpers;

import com.example.myapplication.App;
import com.example.myapplication.SocketHelpers.InventoryItem;

import java.util.ArrayList;

public class ItemState {
    public String name, description, visibleName, type;
    public ArrayList<InventoryItem> crafts;
    public ArrayList<CanMune> canMine;
    public int[] damage;
    public int oneUseTime, maxCount;
    public boolean needWorkbench;

    public String getFullDescription() {
        String canMineString = "";
        if (canMine != null){
            for (int i = 0; i < canMine.size(); i++){
                CanMune cm = canMine.get(i);
                canMineString = canMineString + App.inventoryItemStats.get(cm.name).visibleName + " (" + cm.amount + ")";
                if (i + 1 < canMine.size()){
                    canMineString += ", ";
                }
            }
        }
        return description + "\n\nТип предмета: " + (type.equals("resource") ? "расходуемый материал" : type.equals("tool") ? "инструмент" : type.equals("weapon") ? "оружие" : type.equals("building") ? "сооружение": "неизвестно") +
                (type.equals("weapon") || type.equals("tool") ? "\n\nУрон: " + damage[0] + "..." + damage[1] + ".\nВремя перезарядки: " + oneUseTime/1000f + "с." : "") +
                (type.equals("tool") ? "\n\nМожет добывать: " + canMineString : "");
    }

    private class CanMune {
        public String name;
        public int amount;
    }
}
