package com.example.myapplication.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.SocketHelpers.InventoryItem;

public class InventoryButton extends View {
    protected Paint backgroundPaint, borderPaint, textPaint, stkTextPaint;
    private InventoryItem item;
    private Bitmap b;
    protected boolean selected;
    private int slot, size, margin;

    public InventoryButton(Context context) {
        super(context);
        initButton();
    }

    public InventoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public InventoryButton(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public InventoryButton (Context context, Class parentClass){
        super(context);
        initButton(parentClass);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backgroundPaint.setAlpha(200);
        borderPaint.setAlpha(140);
        textPaint.setAlpha(200);
        stkTextPaint.setAlpha(200);

        canvas.drawRoundRect(0, 0, size, size, (int) App.getDP(10), (int) App.getDP(10), backgroundPaint);
        canvas.drawRoundRect(
                borderPaint.getStrokeWidth() / 2,
                borderPaint.getStrokeWidth() / 2,
                size - borderPaint.getStrokeWidth() / 2,
                size - borderPaint.getStrokeWidth() / 2,
                (int) App.getDP(9),
                (int) App.getDP(9),
                borderPaint);
        if (b != null) {
            canvas.drawBitmap(b, App.getDP(5), App.getDP(5), null);
            if (item.getCount() != 1) {
                canvas.drawText("" + item.getCount(), App.getDP(46), App.getDP(45), textPaint);
            }
        }
    }

    public void setSelected (boolean b){
        this.selected = b;

        if (b){
            borderPaint.setColor(Color.GREEN);
        } else {
            borderPaint.setColor(Color.GRAY);
        }

        postInvalidate();
    }
    public boolean isSelected(){
        return selected;
    }

    public void setDragEntered(boolean b) {
        if (b){
            backgroundPaint.setColor(Color.GRAY);
        } else {
            backgroundPaint.setColor(Color.LTGRAY);
        }

        postInvalidate();
    }

    public void updateText(View v){
        TextView item_name = v.findViewById(R.id.item_name);
        TextView item_description = v.findViewById(R.id.item_description);
        if (item_name != null) item_name.setText(item != null && isSelected() ? item.getState().visibleName : "");
        if (item_description != null) item_description.setText(item != null && isSelected() ? item.getState().getFullDescription() : "");
    }

    private void updateText(){
        this.updateText(App.mainView);
    }

    protected void initButton(Class parentClass){
        setWillNotDraw(false);
        margin = (int)App.getDP(1.5f);
        size = (int)App.getDP(50);

        backgroundPaint = new Paint();
        borderPaint = new Paint();
        textPaint = new Paint();
        stkTextPaint = new Paint();

        if (parentClass.getName().equals(LinearLayout.class.getName())){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
            lp.setMargins(margin, margin, margin, margin);
            setLayoutParams(lp);
        } else if (parentClass.getName().equals(GridLayout.class.getName())) {
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new LinearLayout.LayoutParams(size, size));
            lp.setMargins(margin, margin, margin, margin);
            setLayoutParams(lp);
        }

        backgroundPaint.setColor(Color.LTGRAY);

        borderPaint.setColor(Color.GRAY);
        borderPaint.setStrokeWidth(App.getDP(2));
        borderPaint.setStyle(Paint.Style.STROKE);

        textPaint.setTextSize(App.getDP(13));
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setAlpha(FOCUS_RIGHT);
        textPaint.setTextAlign(Paint.Align.RIGHT);
    }

    protected void initButton(){
        this.initButton(LinearLayout.class);
    }

    public void setSlot(int i) {
        slot = i;
    }

    public int getSlot() {
        return slot;
    }

    public Bitmap getBitmap() {
        return b;
    }

    public InventoryItem getItem() {
        return item;
    }

    public int getFullSize(){
        return size + margin * 2;
    }
}