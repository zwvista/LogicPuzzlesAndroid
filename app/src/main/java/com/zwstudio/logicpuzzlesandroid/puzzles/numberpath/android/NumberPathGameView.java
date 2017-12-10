package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameMove;

import fj.function.Effect0;

import static fj.data.List.range;
import static java.lang.Math.abs;

/**
 * TODO: document your custom view class.
 */
public class NumberPathGameView extends CellsGameView {

    private NumberPathGameActivity activity() {return (NumberPathGameActivity)getContext();}
    private NumberPathGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    private Position pLastDown, pLastMove;

    public NumberPathGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public NumberPathGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NumberPathGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Integer n = game().get(r, c);
                textPaint.setColor(Color.WHITE);
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                int[] dirs = {1, 2};
                for (int dir : dirs) {
                    boolean b = game().getObject(r, c)[dir];
                    if (!b) continue;
                    if (dir == 1)
                        canvas.drawLine(cwc2(c), chr2(r), cwc2(c + 1), chr2(r), linePaint);
                    else
                        canvas.drawLine(cwc2(c), chr2(r), cwc2(c), chr2(r + 1), linePaint);
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
        Effect0 f = () -> activity().app.soundManager.playSoundTap();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pLastDown = pLastMove = p; f.f();
                break;
            case MotionEvent.ACTION_MOVE:
                if (pLastMove != null && !p.equals(pLastMove)) {
                    int n = range(0, NumberPathGame.offset.length)
                            .filter(i -> NumberPathGame.offset[i].equals(p.subtract(pLastMove)))
                            .orHead(() -> -1);
                    if (n != -1) {
                        NumberPathGameMove move = new NumberPathGameMove() {{
                            p = pLastMove; dir = n;
                        }};
                        if (game().setObject(move)) f.f();
                    }
                    pLastMove = p;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (p.equals(pLastDown)) {
                    double dx = event.getX() - (col + 0.5) * cellWidth;
                    double dy = event.getY() - (row + 0.5) * cellHeight;
                    double dx2 = abs(dx), dy2 = abs(dy);
                    NumberPathGameMove move = new NumberPathGameMove() {{
                        p = new Position(row, col);
                        dir = -dy2 <= dx && dx <= dy2 ? dy > 0 ? 2 : 0 :
                                -dx2 <= dy && dy <= dx2 ? dx > 0 ? 1 : 3 : 0;
                    }};
                    game().setObject(move);
                }
                pLastDown = pLastMove = null;
                break;
        }
        return true;
    }

}
