package com.example.myapplication.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.App;
import com.example.myapplication.R;

public class BuildModeEnableButton extends View {
    private Bitmap b;
    private Paint backgroundPaint;

    public BuildModeEnableButton(Context context) {
        super(context);
        init();
    }

    public BuildModeEnableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BuildModeEnableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        b = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.build_mode),
                (int)App.getDP(26),
                (int)App.getDP(26),
                false
        );
        backgroundPaint = new Paint();
        backgroundPaint.setAlpha(200);
        backgroundPaint.setColor(Color.parseColor("#AAAAAA"));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (b != null){
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), App.getDP(8), App.getDP(8), backgroundPaint);
            canvas.drawBitmap(b, getWidth()/2f-b.getWidth()/2f, getHeight()/2f-b.getHeight()/2f, backgroundPaint);
        }
    }
}
