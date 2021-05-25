package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.myapplication.SocketHelpers.PlayerSocketData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Player {
    protected final Paint paint, nicknamePaint, playerBorderPaint, moveToPaint, healthBarBorderPaint, healthBarInnerPaint, healthBarText, messagePaint;
    protected double angle = 1;
    protected final Bitmap bitmap = App.getBitmapFromVectorDrawable(R.drawable.move_to, (int)(App.q*50), (int)(App.q*60));
    protected Bitmap holdingItemBitmap;
    protected final Matrix itemMatrix = new Matrix();
    protected final List<TextMessage> messages;

    private String nickname, holdingItem;
    private int id, usePercents = 0, weaponSize = 1, health = 100, maxHealth = 100, level;
    private boolean isUsingWeapon = false;
    private float weaponAngle = 1;
    private int[] exp;
    private double kX, kY, x, y;
    private final Matrix matrix = new Matrix();

    public Player (){
        this.x = 0;
        this.y = 0;

        messages = new ArrayList<>();

        paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));

        nicknamePaint = new Paint();;
        nicknamePaint.setTextAlign(Paint.Align.CENTER);
        nicknamePaint.setTextSize(27*App.q);
        nicknamePaint.setColor(Color.WHITE);

        playerBorderPaint = new Paint();
        playerBorderPaint.setStyle(Paint.Style.STROKE);
        playerBorderPaint.setColor(Color.BLACK);
        playerBorderPaint.setStrokeWidth(5*App.q);

        moveToPaint = new Paint();
        moveToPaint.setColor(Color.WHITE);

        healthBarBorderPaint = new Paint();
        healthBarBorderPaint.setStrokeWidth(2*App.q);
        healthBarBorderPaint.setStyle(Paint.Style.STROKE);
        healthBarBorderPaint.setColor(Color.WHITE);

        healthBarInnerPaint = new Paint();
        healthBarInnerPaint.setColor(App.mainView.getResources().getColor(R.color.material_red));
        healthBarInnerPaint.setStyle(Paint.Style.FILL);

        healthBarText = new Paint();
        healthBarText.setTextSize(App.q*17);
        healthBarText.setColor(Color.WHITE);
        healthBarText.setTextAlign(Paint.Align.CENTER);

        messagePaint = new Paint();
        messagePaint.setTextSize(32*App.q);
        messagePaint.setTextAlign(Paint.Align.CENTER);
        messagePaint.setStrokeWidth(App.q*2);
    }
    public Player (PlayerSocketData p){
        this();
        this.nickname = new String(p.getNickname());
        this.holdingItem = p.getHoldingItem();
        if (holdingItem != null) holdingItemBitmap = Bitmap.createScaledBitmap(App.Images.getImage(holdingItem), (int) (App.q * 90), (int) (App.q * 90), false);
        this.id = p.getId();
        this.level = p.getLevel();
        this.exp = Arrays.copyOf(p.getExp(), 2);
    }

    public void draw(Canvas canvas, float x, float y) {
//        int size = (int)(80*App.q);
//        int startX = (int)(x + App.RADIUS*App.q);
//        int startY = (int)(y - size/2);
//        _moveTo.setBounds(startX, startY, startX + size/2, startY + size);;
//
//        canvas.rotate((float)-angle, x, y);
//        _moveTo.draw(canvas);
//        canvas.rotate((float)angle, x, y);

        matrix.setRotate((float)-angle, -App.q*15, bitmap.getHeight()/2f);
        matrix.postTranslate(x + App.q*15 , y - bitmap.getHeight()/2f);
        canvas.drawBitmap(bitmap, matrix, null);

        if (isUsingWeapon){
            Bitmap bb = Bitmap.createScaledBitmap(App.Images.getImage(holdingItem), (int)(App.q*weaponSize*1.2f), (int)(App.q*weaponSize*1.2f), false);
            itemMatrix.setRotate((float) (-15 -angle + (weaponAngle*Math.pow(usePercents/100f, 1.5f))), 5*App.q, 95*App.q);
            itemMatrix.postTranslate(x-5*App.q, y-95*App.q);
            canvas.drawBitmap(bb, itemMatrix, null);
        } else if (holdingItemBitmap != null){
            float g = 8;
            itemMatrix.setRotate((float)-angle, holdingItemBitmap.getWidth()/g, holdingItemBitmap.getHeight()/g);
            itemMatrix.postTranslate(x-holdingItemBitmap.getWidth()/g, y-holdingItemBitmap.getHeight()/g);
            canvas.drawBitmap(holdingItemBitmap, itemMatrix, null);
        }

        canvas.drawCircle(x, y, App.RADIUS*App.q, paint);
        canvas.drawCircle(x, y, App.RADIUS*App.q - playerBorderPaint.getStrokeWidth()/2, playerBorderPaint);


        float healthBarWidth = App.q*100;

        canvas.drawRect(x-healthBarWidth/2f, y-App.q*App.RADIUS - App.q*19, x+healthBarWidth/2f - healthBarWidth*(1 - health/(float)maxHealth), y-App.q*App.RADIUS, healthBarInnerPaint);
        canvas.drawRect(x-healthBarWidth/2f, y-App.q*App.RADIUS - App.q*19, x+healthBarWidth/2f, y-App.q*App.RADIUS, healthBarBorderPaint);
        canvas.drawText(String.valueOf(this.health), x, y - App.q*App.RADIUS-App.q*3, healthBarText);
        canvas.drawText(nickname, x, y - App.RADIUS*App.q - 18*App.q, nicknamePaint);

        for (int i = 0; i < messages.size(); i++){
            if (messages.get(i).getState() >= 1){
                messages.remove(i);
                i--;
            }
        }

        for (int i = 0; i < messages.size(); i++){
            TextMessage msg = messages.get(i);

            messagePaint.setStyle(Paint.Style.STROKE);
            messagePaint.setColor(Color.WHITE);
            messagePaint.setAlpha(msg.getAlpha());
            canvas.drawText(msg.getMessage(), x - App.q*msg.getX(), y - App.q*msg.getY(), messagePaint);

            messagePaint.setStyle(Paint.Style.FILL);
            messagePaint.setColor(msg.getColor());
            messagePaint.setAlpha(msg.getAlpha());
            canvas.drawText(msg.getMessage(), x - App.q*msg.getX(), y - App.q*msg.getY(), messagePaint);
        }
    }

    public void useWeapon(int t, float d, int l) {
        usePercents = 0;
        isUsingWeapon = true;
        weaponSize = l;
        weaponAngle = d*2f;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                usePercents += 1;
                if (usePercents >= 100) {
                    this.cancel();
                    isUsingWeapon = false;
                }
            }
        }, 0, 5);
    }

    public void attackAnimation(){}

    // public void update(double x, double y, double kX, double kY, String holdingItem, int[] health) {
    public void update(PlayerSocketData p) {
        this.x = p.getX();
        this.y = p.getY();

        this.angle =  Math.atan(p.getkX()/(p.getkY() == 0 ? Double.MIN_VALUE : p.getkY())) * 180 / Math.PI;
        if (p.getkY() < 0){
            this.angle += 180;
        }

        this.angle -= 90;

        if (p.getHoldingItem() == null) {
            holdingItemBitmap = null;
        } else if ((p.getHoldingItem() != null && p.getHoldingItem() == null) || !p.getHoldingItem().equals(this.holdingItem)) {
            Log.d("HOLDING", p.getHoldingItem());
            this.holdingItem = p.getHoldingItem();
            holdingItemBitmap = Bitmap.createScaledBitmap(App.Images.getImage(holdingItem), (int) (App.q * 90), (int) (App.q * 90), false);
        }
        this.holdingItem = p.getHoldingItem();

        this.maxHealth = p.getHealth()[1];
//        if (this.health > p.getHealth()[0]){
//            damaged();
//        } else if (this.health < p.getHealth()[0]){
//            healed();
//        }
        this.health = p.getHealth()[0];

        this.exp = Arrays.copyOf(p.getExp(), 2);
        this.level = p.getLevel();
    }

    protected void damaged(int damage){
        TextMessage td = new TextMessage("-" + damage, 2000, Color.parseColor("#ff2400"));
        messages.add(td);
    }
    protected void healed(){
        // todo
    }

    public void setId(int id){
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public int[] getExp() {
        return exp;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    protected static class TextMessage {
        private final float duration;
        private final long startedAt;
        private final int color;
        private final String message;
        private long current;
        private int x;
        private int y;

        public TextMessage(String message, int duration){
            this(message, duration, Color.WHITE);
        }
        public TextMessage(String message, int duration, int color){
            this.message = message;
            this.duration = (float) duration;
            this.startedAt = System.currentTimeMillis();
            this.color = color;
            x = new Random().nextInt(100) - 50;
        }

        public String getMessage() {
            return message;
        }

        public int getX(){
            return  x;
        }

        public int getY(){
            this.current = System.currentTimeMillis();
            return (int) ( App.RADIUS + 30 + 100 * Math.pow( (current - startedAt) / duration, 2) );
        }

        public int getColor(){
            return color;
        }

        public float getState(){
            return (current - startedAt) / duration;
        }

        public int getAlpha(){
            float state = getState();
            if (state < 0.5){
                return 255;
            } else {
                return (int) ((1 - 2 * state) * 255);
            }
        }
    }
}
