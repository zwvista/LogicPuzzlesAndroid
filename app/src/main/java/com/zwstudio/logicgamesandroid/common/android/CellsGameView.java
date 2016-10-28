package com.zwstudio.logicgamesandroid.common.android;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CellsGameView extends View {

    protected int cellWidth, cellHeight;
    protected TextPaint textPaint = new TextPaint();

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
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    // http://stackoverflow.com/questions/11120392/android-center-text-on-canvas
    protected void drawTextCentered(String text, int x, int y, Canvas canvas) {
        textPaint.setTextSize(cellHeight);
        float xPos = x + (cellWidth - textPaint.measureText(text)) / 2;
        float yPos = y + (cellHeight - textPaint.descent() - textPaint.ascent()) / 2;
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    protected int cwc(int c) {return c * cellWidth + 1;}
    protected int chr(int r) {return r * cellHeight + 1;}
    protected int cwc2(int c) {return c * cellWidth + 1 + cellWidth / 2;}
    protected int chr2(int r) {return r * cellHeight + 1 + cellHeight / 2;}

}
