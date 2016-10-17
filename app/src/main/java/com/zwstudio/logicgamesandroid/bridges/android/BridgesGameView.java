package com.zwstudio.logicgamesandroid.bridges.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zwstudio.logicgamesandroid.bridges.data.BridgesGameProgress;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesIslandInfo;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesIslandObject;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.Map;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class BridgesGameView extends View {

    private BridgesGameActivity activity() {return (BridgesGameActivity)getContext();}
    private BridgesGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private int cellWidth, cellHeight;
    private Paint islandPaint = new Paint();
    private Paint lightPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public BridgesGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public BridgesGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BridgesGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        islandPaint.setColor(Color.WHITE);
        islandPaint.setStyle(Paint.Style.STROKE);
        lightPaint.setColor(Color.YELLOW);
        lightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (cols() < 1 || rows() < 1) return;
        cellWidth = getWidth() / cols() - 1;
        cellHeight = getHeight() / rows() - 1;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    // http://stackoverflow.com/questions/11120392/android-center-text-on-canvas
    private void drawTextCentered(String text, int x, int y, Canvas canvas) {
        float xPos = x + (cellWidth - textPaint.measureText(text)) / 2;
        float yPos = y + (cellHeight - textPaint.descent() - textPaint.ascent()) / 2;
        canvas.drawText(text, xPos, yPos, textPaint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        if (isInEditMode()) return;
        for (Map.Entry<Position, BridgesIslandInfo> entry : game().islandsInfo.entrySet()) {
            Position p = entry.getKey();
            int r = p.row, c = p.col;
            BridgesIslandInfo info = entry.getValue();
            BridgesIslandObject o = (BridgesIslandObject) game().getObject(p);
            canvas.drawArc(c * cellWidth + 1, r * cellHeight + 1,
                    (c + 1) * cellWidth + 1, (r + 1) * cellHeight + 1,
                    0, 360, true, islandPaint);
            textPaint.setColor(
                    o.state == BridgesIslandObject.IslandState.Complete ? Color.GREEN :
                    o.state == BridgesIslandObject.IslandState.Error ? Color.RED :
                    Color.WHITE
            );
            String text = String.valueOf(info.bridges);
            textPaint.setTextSize(cellHeight);
            drawTextCentered(text, c * cellWidth + 1, r * cellHeight + 1, canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            BridgesGameProgress rec = activity().doc().gameProgress();
        }
        return true;
    }

}
