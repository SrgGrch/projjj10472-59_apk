package com.example.myapplication.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.myapplication.App;
import com.example.myapplication.SocketHelpers.InventoryItem;

public class MainInventoryButton extends View {
    private final Paint backgroundPaint = new Paint(), borderPaint = new Paint(), textPaint = new Paint();
    private InventoryItem item;
    private Bitmap b;
    private boolean selected;
    private Canvas canvas;
    private int slot;

    public MainInventoryButton(Context context) {
        super(context);
        initButton();
    }

    public MainInventoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public MainInventoryButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton();
    }

    public void setItemInInventory(InventoryItem item){
        if (item == null){
            this.item = null;
            this.b = null;
        } else {
            this.item = item;
            this.b = Bitmap.createScaledBitmap(
                    App.Images.getImage(item.getName()),
                    (int)App.getDP(40), (int)App.getDP(40),
                    true
            );
        }

        postInvalidate();
    }

    public InventoryItem getItem() {
        return item;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(0, 0, getWidth(), getHeight(), (int)App.getDP(10), (int)App.getDP(10), backgroundPaint);
        canvas.drawRoundRect(
                borderPaint.getStrokeWidth() / 2,
                borderPaint.getStrokeWidth() / 2,
                getWidth() - borderPaint.getStrokeWidth() / 2,
                getHeight() - borderPaint.getStrokeWidth() / 2,
                (int)App.getDP(9),
                (int)App.getDP(9),
                borderPaint);
        if (b != null){
            canvas.drawBitmap(b, App.getDP(5), App.getDP(5), null);
            if (item.getCount() != 1){
                canvas.drawText("" + item.getCount(), App.getDP(27), App.getDP(45), textPaint);
            }
        }
    }

    public void setSelected (boolean b){
        this.selected = b;

        if (b){
            backgroundPaint.setColor(Color.GRAY);
        } else {
            backgroundPaint.setColor(Color.LTGRAY);
        }

        postInvalidate();
    }
    public boolean isSelected(){
        return selected;
    }

    protected void initButton(){
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                (int)App.getDP(50),
                (int)App.getDP(50)
        );
        float margin = 2;
        p.setMargins(
                (int)App.getDP(margin),
                (int)App.getDP(margin),
                (int)App.getDP(margin),
                (int)App.getDP(margin)
        );

        setLayoutParams(p);

        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setAlpha(200);

        borderPaint.setColor(Color.BLACK);
        borderPaint.setAlpha(140);
        borderPaint.setStrokeWidth(App.getDP(1.5f));
        borderPaint.setStyle(Paint.Style.STROKE);

        textPaint.setTextSize(App.getDP(15));
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public int getSlot() {
        return slot;
    }
    public void setSlot(int slot){
        this.slot = slot;
    }
}
