package com.example.myapplication.SocketHelpers;

public class PlayerSocketData {
    protected int id;
    protected double x,y, kX, kY;
    protected String nickname, holdingItem;
    protected int[] health;
    protected int[] exp;
    protected int level;

    public int[] getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public int[] getHealth() {
        return health;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getkY() {
        return kY;
    }

    public double getkX() {
        return kX;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHoldingItem() {
        return holdingItem;
    }

    public void setHoldingItem(String holdingItem) {
        this.holdingItem = holdingItem;
    }
}