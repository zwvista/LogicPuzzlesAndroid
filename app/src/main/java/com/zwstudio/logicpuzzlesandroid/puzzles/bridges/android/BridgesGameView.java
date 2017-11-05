package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesIslandInfo;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesIslandObject;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Map;

import fj.function.Effect0;

/**
 * TODO: document your custom view class.
 */
public class BridgesGameView extends CellsGameView {

    private BridgesGameActivity activity() {return (BridgesGameActivity)getContext();}
    private BridgesGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
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
            canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, islandPaint);
            textPaint.setColor(
                    o.state == HintState.Complete ? Color.GREEN :
                    o.state == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            String text = String.valueOf(info.bridges);
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            int[] dirs = {1, 2};
            for (int dir : dirs) {
                Position p2 = info.neighbors[dir];
                if (p2 == null) continue;
                int r2 = p2.row, c2 = p2.col;
                int b = o.bridges[dir];
                if (dir == 1 && b == 1)
                    canvas.drawLine(cwc(c + 1), chr2(r), cwc(c2), chr2(r2), bridgePaint);
                else if (dir == 1 && b == 2) {
                    canvas.drawLine(cwc(c + 1), chr2(r) - 10, cwc(c2), chr2(r2) - 10, bridgePaint);
                    canvas.drawLine(cwc(c + 1), chr2(r) + 10, cwc(c2), chr2(r2) + 10, bridgePaint);
                }
                else if (dir == 2 && b == 1)
                    canvas.drawLine(cwc2(c), chr(r + 1), cwc2(c2), chr(r2), bridgePaint);
                else if (dir == 2 && b == 2) {
                    canvas.drawLine(cwc2(c) - 10, chr(r + 1), cwc2(c2) - 10, chr(r2), bridgePaint);
                    canvas.drawLine(cwc2(c) + 10, chr(r + 1), cwc2(c2) + 10, chr(r2), bridgePaint);
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
            if (isI) {
                pLast = p; f.f();
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (isI && pLast != null && !p.equals(pLast)) {
                BridgesGameMove move = new BridgesGameMove() {{
                    pFrom = pLast; pTo = p;
                }};
                game().switchBridges(move);
                pLast = p; f.f();
            }
            break;
        case MotionEvent.ACTION_UP:
            pLast = null;
            break;
        }
        return true;
    }

}
