package com.example.myapplication.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

import java.util.Arrays;

public class CustomButton extends androidx.appcompat.widget.AppCompatButton {
    private int background = getResources().getColor(R.color.material_secondary);
    private final Paint paint = new Paint();

    public CustomButton(Context context) {
        super(context);
        initButton(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.backgroundTint});

        try {
            String bg = a.getString(0);
            if (bg != null) background = Color.parseColor(bg);
        } catch (Exception ignore){ }
        a.recycle();
        initButton(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(context);
    }

    private void initButton(Context context){
        // todo
    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                Log.d("TOUCH", "UP");
//                break;
//            case MotionEvent.ACTION_DOWN:
//                Log.d("TOUCH", "DOWN");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d("TOUCH", "CANCEL");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (event.getX() < 0 || event.getY() < 0 || event.getX() > getWidth() || event.getY() > getHeight()){
//                    // Log.d("TOUCH", "OUTSIDE");
//                }
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        int col = background;
//        if (!isEnabled()){
//            int r = Color.red(col), g = Color.green(col), b = Color.blue(col);
//            float f = 0.8f;
//            col = Color.rgb((int)(r*f), (int)(g*f), (int)(b*f));
//            setTextColor(Color.parseColor("#AAAAAA"));
//            paint.setColor(Color.parseColor("#AAAAAA"));
//        } else {
//            setTextColor(Color.WHITE);
//            paint.setColor(Color.WHITE);
//        }
//        super.onDraw(canvas);
//
//        float paintWidth = 3f;
//
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(paintWidth);
//
//        float w = getWidth();
//        float h = getHeight();
//
//        RectF ovalStart = new RectF();
//        ovalStart.set(paintWidth/2, paintWidth/2, h-paintWidth/2, h-paintWidth/2);
//
//        RectF ovalStop = new RectF();
//        ovalStop.set(w-h-paintWidth/2, paintWidth/2, w-paintWidth/2, h-paintWidth/2);
//
//        canvas.drawArc(ovalStart, 90, 180, false, paint);
//        canvas.drawArc(ovalStop, -90, 180, false, paint);
//        canvas.drawLine(h/2, paintWidth/2, w-h/2, paintWidth/2, paint);
//        canvas.drawLine(h/2, h-paintWidth/2, w-h/2, h-paintWidth/2, paint);
//
//        float oneDP = getResources().getDisplayMetrics().density;
//
//        @SuppressLint("DrawAllocation") Shape shape = new RoundRectShape(new float[]{
//                20*oneDP, 20*oneDP, 20*oneDP, 20*oneDP,
//                20*oneDP, 20*oneDP, 20*oneDP, 20*oneDP
//        }, null, null);
//
//        @SuppressLint("DrawAllocation") ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
//
//        shapeDrawable
//                .getPaint()
//                .setColor(col);
//        setBackground(shapeDrawable);
    }
}
