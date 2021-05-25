package com.example.myapplication.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.example.myapplication.App;

public class CraftInventoryButton extends InventoryButton {
    private boolean canCraft = true;
    private boolean isCountOkay = true;
    private boolean needWorkbench = false;
    private final Paint circlePaint = new Paint();

    public CraftInventoryButton(Context context) {
        super(context);
    }

    public CraftInventoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CraftInventoryButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CraftInventoryButton(Context context, Class parentClass){
        super(context, parentClass);
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (needWorkbench){
            circlePaint.setColor(Color.parseColor("#CCCCCC"));
            circlePaint.setAlpha(200);
            canvas.drawCircle(App.getDP(6), App.getDP(6), App.getDP(15), circlePaint);
            canvas.drawBitmap(Bitmap.createScaledBitmap(App.Images.getImage("workbench"), (int)App.getDP(14), (int)App.getDP(14), false), App.getDP(2), App.getDP(2), null);
        }
    }

    public void setCanCraft(boolean canCraft) {
        this.canCraft = canCraft;
        if (canCraft){
            backgroundPaint.setColor(Color.LTGRAY);
        } else {
            backgroundPaint.setColor(Color.GRAY);
        }
        setSelected(isSelected());
    }

    public boolean canCraft(){
        return canCraft;
    }

    public boolean isCountOkay() {
        return isCountOkay;
    }

    public void setNeedWorkbench(boolean needWorkbench) {
        this.needWorkbench = needWorkbench;
    }

    @Override
    public void setSelected(boolean b) {
        if (canCraft){
            super.setSelected(b);
        } else {
            this.selected = b;
            if (b){
                borderPaint.setColor(Color.LTGRAY);
            } else {
                borderPaint.setColor(Color.GRAY);
            }

            postInvalidate();
        }
    }

    public void setCountOkay(boolean countOkay) {
        isCountOkay = countOkay;
        if (!isCountOkay){
            backgroundPaint.setColor(Color.parseColor("#ff1744"));
            textPaint.setColor(Color.WHITE);
        } else {
            backgroundPaint.setColor(Color.GRAY);
            textPaint.setColor(Color.BLACK);
        }
        postInvalidate();
    }
}
