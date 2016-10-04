package com.zwstudio.lightupandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.zwstudio.lightupandroid.data.GameDocument;

/**
 * TODO: document your custom view class.
 */
public class GameView extends PixelGridView {

    GameDocument doc() {return ((GameApplication)getContext()).getGameDocument();}

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
