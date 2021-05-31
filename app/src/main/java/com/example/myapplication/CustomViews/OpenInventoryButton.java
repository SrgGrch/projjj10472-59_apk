package com.example.myapplication.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.myapplication.App;
import com.example.myapplication.R;

public class OpenInventoryButton extends View {

    public OpenInventoryButton(Context context) {
        super(context);
        initButton();
    }

    public OpenInventoryButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public OpenInventoryButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(
                App.getBitmapFromVectorDrawable(R.drawable.open_inventory_button, (int)App.getDP(50), (int)App.getDP(50)), new Matrix(), null
        );
    }

    private void initButton(){
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                (int) App.getDP(50),
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
    }
}
