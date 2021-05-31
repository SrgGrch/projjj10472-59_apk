package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {

    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private float outerCircleRadius;
    private float innerCircleRadius;
    private float outerCircleCenterPositionX;
    private float outerCircleCenterPositionY;
    private float innerCircleCenterPositionX;
    private float innerCircleCenterPositionY;
    private double joystickCenterToTouchDistance;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    private double outerX, outerY;

    public Joystick(float centerPositionX, float centerPositionY, float outerCircleRadius, float innerCircleRadius) {
        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;

        outerX = centerPositionX;
        outerY = centerPositionY;

        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;

        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setAlpha(150);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.DKGRAY);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(
                (float) outerX,
                (float) outerY,
                outerCircleRadius,
                outerCirclePaint
        );

        canvas.drawCircle(
                innerCircleCenterPositionX,
                innerCircleCenterPositionY,
                innerCircleRadius,
                innerCirclePaint
        );
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = (int) (outerX + actuatorX * outerCircleRadius);
        innerCircleCenterPositionY = (int) (outerY + actuatorY * outerCircleRadius);
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joystickCenterToTouchDistance = Math.sqrt(
                Math.pow(outerCircleCenterPositionX - touchPositionX, 2) + Math.pow(outerCircleCenterPositionY - touchPositionY, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        double deltaX = touchPositionX - outerX;
        double deltaY = touchPositionY - outerY;

        double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY,2));

        if (deltaDistance < outerCircleRadius){
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        } else {
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }

        // API
        App.api.emit("updateJoystick", new JoystickPosition(actuatorX, actuatorY));
    }

    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
        this.setIsPressed(false);
        outerX = outerCircleCenterPositionX;
        outerY = outerCircleCenterPositionY;

        // API
        App.api.emit("updateJoystick", new JoystickPosition(actuatorX, actuatorY));
    }

    public void setPosition(double x, double y) {
        this.outerX = x;
        this.outerY = y;
    }


    // JSON
    class JoystickPosition {
        private double kX, kY;
        public JoystickPosition (double kX, double kY){
            this.kX = kX;
            this.kY = kY;
        }

        public double getkX() {
            return kX;
        }

        public double getkY() {
            return kY;
        }
    }
}
