package com.example.myapplication.SocketHelpers;

public class LoginData {
    private String token, nickname;
    private boolean guest;
    private int id;

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public boolean isGuest() {
        return guest;
    }
}
