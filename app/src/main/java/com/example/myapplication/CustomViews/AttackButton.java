package com.example.myapplication.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.myapplication.App;
import com.example.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

public class AttackButton extends ImageView {
    private int maxMs = 1, curMs = 0;
    private final Paint percentagePaint = new Paint(), backgroundPaint = new Paint(), ballPaint = new Paint();
    private RectF oval, backgroundOval;
    private final Path path = new Path(), clipPath = new Path();

    public AttackButton(Context context) {
        super(context);
        initButton();
    }

    public AttackButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public AttackButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipPath(clipPath);

        if (curMs > 0){
            float percent = curMs / (float)maxMs;
            float angle = 360 - 360 * percent;

            path.reset();
            path.arcTo(oval, 90 - angle/2f, angle, true);
            backgroundPaint.setAlpha((int) (255*Math.pow(percent, 2)));
            canvas.drawOval(backgroundOval, backgroundPaint);
            canvas.drawPath(path, percentagePaint);

            float r = App.getDP(10);
//            float centerX = (float) Math.sin(percent*Math.PI*1.5)*(
//                    App.getDP(40) - r
//            ) + App.getDP(40);
//            float centerY = r + (getHeight()-r*2)*percent;
            float centerX = (float) (1+Math.sin(Math.pow(percent, 3)*Math.PI))*getWidth()/2f;
            float centerY = (float) (1-Math.cos(Math.pow(percent, 3)*Math.PI))*getHeight()/2f;
            @SuppressLint("DrawAllocation") RadialGradient rg = new RadialGradient(centerX, centerY, r, new int[]{Color.WHITE, 0x00ffffff}, new float[]{0.5f, 1f}, Shader.TileMode.CLAMP);
            ballPaint.setShader(rg);
            canvas.drawCircle(centerX, centerY, r, ballPaint);
        }

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        oval = new RectF(
                percentagePaint.getStrokeWidth()/2f,
                percentagePaint.getStrokeWidth()/2f,
                getWidth() - percentagePaint.getStrokeWidth()/2f,
                getHeight() - percentagePaint.getStrokeWidth()/2f
        );
        backgroundOval = new RectF(
                0,
                0,
                getWidth(),
                getHeight()
        );
        clipPath.addOval(backgroundOval, Path.Direction.CW);
        ballPaint.setStyle(Paint.Style.FILL);
    }

    private void initButton(){
        setOnClickListener(v -> {
            App.api.emit("useCurrentThing", null);
        });
        percentagePaint.setColor(getResources().getColor(R.color.white));
        percentagePaint.setStyle(Paint.Style.STROKE);
        percentagePaint.setStrokeWidth(App.getDP(2));

        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setAlpha(180);
        backgroundPaint.setStyle(Paint.Style.FILL);


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (curMs > 0){
                    curMs -= 20;
                }
            }
        }, 0, 20);
    }

    public void setTime (int ms){
        maxMs = ms;
        curMs = ms;
    }
}
