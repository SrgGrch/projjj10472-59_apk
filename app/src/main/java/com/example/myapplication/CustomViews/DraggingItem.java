package com.example.myapplication.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.App;
import com.example.myapplication.SocketHelpers.InventoryItem;

public class DraggingItem extends View {
    private InventoryItem item;
    private Bitmap b;
    private Paint textPaint = new Paint();

    public DraggingItem(Context context) {
        super(context);
    }

    public DraggingItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggingItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public void setBitmap(Bitmap bitmap) {
        this.b = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTextSize(App.getDP(16));
        textPaint.setColor(Color.BLACK);

        canvas.drawBitmap(b, (int)App.getDP(10), (int) App.getDP(10), null);
        canvas.drawText("" + item.getCount(), App.getDP(3), App.getDP(19), textPaint);
    }
}
