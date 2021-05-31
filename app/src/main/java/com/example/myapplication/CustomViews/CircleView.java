package com.example.myapplication.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.App;

public class CircleView extends View {
    private Paint paint, background;
    private final Path path = new Path();

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setARGB(190, 255, 255, 255);

        background = new Paint();
        background.setARGB(50, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.rewind();

        float radius = App.q*380;
        paint.setStrokeWidth(App.q*5);
        path.addCircle(getWidth()/2f, getHeight()/2f, radius, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        canvas.drawCircle(getWidth()/2f, getHeight()/2f, radius, paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
