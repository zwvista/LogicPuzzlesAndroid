package com.zwstudio.logicpuzzlesandroid.common.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public abstract class CellsGameView extends View {

    protected int cellWidth, cellHeight;
    protected abstract int rowsInView();
    protected abstract int colsInView();

    public CellsGameView(Context context) {
        super(context);
    }

    public CellsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight()) / Math.max(rowsInView(), colsInView());
        setMeasuredDimension(size * colsInView(), size * rowsInView());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (colsInView() < 1 || rowsInView() < 1) return;
        cellWidth = getWidth() / colsInView() - 1;
        cellHeight = getHeight() / rowsInView() - 1;
        invalidate();
    }

    // http://stackoverflow.com/questions/11120392/android-center-text-on-canvas
    protected void drawTextCentered(String text, int x, int y, Canvas canvas, TextPaint textPaint) {
        drawTextCentered(text, x, y, cellWidth, cellHeight, canvas, textPaint);
    }

    protected void drawTextCentered(String text, int x, int y, int wd, int ht, Canvas canvas, TextPaint textPaint) {
        textPaint.setTextSize(ht);
        int txtWd = (int)textPaint.measureText(text);
        int txtSize = ht >= txtWd ? ht : ht * ht / txtWd;
        textPaint.setTextSize(txtSize);
        float xPos = x + (wd - textPaint.measureText(text)) / 2;
        float yPos = y + (ht - textPaint.descent() - textPaint.ascent()) / 2;
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    protected int cwc(int c) {return c * cellWidth + 1;}
    protected int chr(int r) {return r * cellHeight + 1;}
    protected int cwc2(int c) {return c * cellWidth + 1 + cellWidth / 2;}
    protected int chr2(int r) {return r * cellHeight + 1 + cellHeight / 2;}

    protected Drawable fromImageToDrawable(String imageFile) {
        Drawable result = null;
        try {
            InputStream is = getContext().getApplicationContext().getAssets().open(imageFile);
            Bitmap bmpLightbulb = BitmapFactory.decodeStream(is);
            result = new BitmapDrawable(bmpLightbulb);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
