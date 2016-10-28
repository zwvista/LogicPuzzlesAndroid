package com.zwstudio.logicgamesandroid.games.bridges.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zwstudio.logicgamesandroid.games.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.games.bridges.domain.BridgesIslandInfo;
import com.zwstudio.logicgamesandroid.games.bridges.domain.BridgesIslandObject;
import com.zwstudio.logicgamesandroid.main.domain.HintState;
import com.zwstudio.logicgamesandroid.common.domain.Position;

import java.util.Map;

import fj.function.Effect0;

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
    private Paint bridgePaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    private Position pLast;

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
        bridgePaint.setColor(Color.YELLOW);
        bridgePaint.setStyle(Paint.Style.STROKE);
        bridgePaint.setStrokeWidth(5);
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
                    o.state == HintState.Complete ? Color.GREEN :
                    o.state == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            String text = String.valueOf(info.bridges);
            textPaint.setTextSize(cellHeight);
            drawTextCentered(text, c * cellWidth + 1, r * cellHeight + 1, canvas);
            int[] dirs = {1, 2};
            for (int dir : dirs) {
                Position p2 = info.neighbors[dir];
                if (p2 == null) continue;
                int r2 = p2.row, c2 = p2.col;
                int b = o.bridges[dir];
                if (dir == 1 && b == 1)
                    canvas.drawLine(c * cellWidth + 1 + cellWidth, r * cellHeight + 1 + cellHeight / 2, c2 * cellWidth + 1, r2 * cellHeight + 1 + cellHeight / 2, bridgePaint);
                else if (dir == 1 && b == 2) {
                    canvas.drawLine(c * cellWidth + 1 + cellWidth, r * cellHeight + 1 + cellHeight / 2 - 10, c2 * cellWidth + 1, r2 * cellHeight + 1 + cellHeight / 2 - 10, bridgePaint);
                    canvas.drawLine(c * cellWidth + 1 + cellWidth, r * cellHeight + 1 + cellHeight / 2 + 10, c2 * cellWidth + 1, r2 * cellHeight + 1 + cellHeight / 2 + 10, bridgePaint);
                }
                else if (dir == 2 && b == 1)
                    canvas.drawLine(c * cellWidth + 1 + cellWidth / 2, r * cellHeight + 1 + cellHeight, c2 * cellWidth + 1 + cellWidth / 2, r2 * cellHeight + 1, bridgePaint);
                else if (dir == 2 && b == 2) {
                    canvas.drawLine(c * cellWidth + 1 + cellWidth / 2 - 10, r * cellHeight + 1 + cellHeight, c2 * cellWidth + 1 + cellWidth / 2 - 10, r2 * cellHeight + 1, bridgePaint);
                    canvas.drawLine(c * cellWidth + 1 + cellWidth / 2 + 10, r * cellHeight + 1 + cellHeight, c2 * cellWidth + 1 + cellWidth / 2 + 10, r2 * cellHeight + 1, bridgePaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (game().isSolved()) return true;
        int col = (int)(event.getX() / cellWidth);
        int row = (int)(event.getY() / cellHeight);
        if (col >= cols() || row >= rows()) return true;
        Position p = new Position(row, col);
        boolean isI = game().isIsland(p);
        Effect0 f = () -> activity().app.soundManager.playSoundTap();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (!isI) break;
            pLast = p; f.f();
            break;
        case MotionEvent.ACTION_MOVE:
            if (pLast == null) break;
            if (isI) {
                if (!p.equals(pLast)) {
                    game().switchBridges(pLast, p);
                    pLast = p; f.f();
                }
            }
            break;
        default:
            return true;
        }
        return true;
    }

}
