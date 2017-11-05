package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove;

import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class KakuroGameView extends CellsGameView {

    private KakuroGameActivity activity() {return (KakuroGameActivity)getContext();}
    private KakuroGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint hintPaint = new TextPaint();
    private TextPaint textPaint = new TextPaint();

    public KakuroGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public KakuroGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public KakuroGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.GRAY);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setColor(Color.BLACK);
        hintPaint.setAntiAlias(true);
        hintPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Integer n = game().getObject(new Position(r, c));
                if (n == null) {
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r + 1), linePaint);
                } else if (n != 0) {
                    String text = String.valueOf(n);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                }
            }
        if (isInEditMode()) return;
        for (Map.Entry<Position, Integer> entry : game().pos2horzHint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            HintState s = game().getHorzState(p);
            hintPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.BLACK
            );
            String text = String.valueOf(n);
            drawTextCentered(text, cwc2(p.col), chr(p.row), cellWidth / 2, cellHeight / 2, canvas, hintPaint);
        }
        for (Map.Entry<Position, Integer> entry : game().pos2vertHint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            HintState s = game().getVertState(p);
            hintPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.BLACK
            );
            String text = String.valueOf(n);
            drawTextCentered(text, cwc(p.col), chr2(p.row), cellWidth / 2, cellHeight / 2, canvas, hintPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            KakuroGameMove move = new KakuroGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
