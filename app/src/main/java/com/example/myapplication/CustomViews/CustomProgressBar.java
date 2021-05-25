package com.example.myapplication.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.myapplication.App;
import com.example.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

public class CustomProgressBar extends View {
    private String text = "";
    private float percents = 0, step = 0;
    private final Paint textPaint = new Paint();
    @SuppressLint("UseCompatLoadingForDrawables")
    private final Drawable outer = getResources().getDrawable(R.drawable.progress_outer),
    inner = getResources().getDrawable(R.drawable.progress_inner),
    show = getResources().getDrawable(R.drawable.progress_load_show);
    private int steps;

    public CustomProgressBar(Context context) {
        super(context);
        initProgress();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgress();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProgress();
    }

    private void initProgress(){
        textPaint.setTextSize(App.getDP(13));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (steps > 0){
                    steps--;
                    percents += step;
                    postInvalidate();
                }
            }
        }, 0, 10);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        outer.setBounds(0, getHeight()/2 - (int)App.getDP(6), getWidth(), getHeight()/2 + (int)App.getDP(6));
        outer.draw(canvas);

        show.setBounds(
                (int)(getWidth()/2f - (getWidth()/200f*percents)),
                getHeight()/2 - (int)App.getDP(4),
                (int)(getWidth()/2f + (getWidth()/200f*percents)),
                getHeight()/2 + (int)App.getDP(4)
        );
        show.draw(canvas);

        inner.setBounds(
                (int) (getWidth()/2f - App.getDP(12)),
                getHeight()/2 - (int)App.getDP(12),
                (int) (getWidth()/2f + App.getDP(12)),
                getHeight()/2 + (int)App.getDP(12)
        );
        inner.draw(canvas);

        canvas.drawText(text, getWidth()/2f, getHeight()/2f + App.getDP(5), textPaint);
    }

    public void update (float p, String text){
        this.text = text;
        this.step = (p - percents) / 100f;
        this.steps = 100;
    }
}
