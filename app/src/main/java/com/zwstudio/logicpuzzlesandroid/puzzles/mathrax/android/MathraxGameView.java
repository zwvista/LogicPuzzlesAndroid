package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxHint;

import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class MathraxGameView extends CellsGameView {

    private MathraxGameActivity activity() {return (MathraxGameActivity)getContext();}
    private MathraxGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint hintPaint = new Paint();
    private Paint mathPaint1 = new Paint();
    private Paint mathPaint2 = new Paint();

    public MathraxGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public MathraxGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MathraxGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(5);
        mathPaint1.setStyle(Paint.Style.STROKE);
        mathPaint1.setColor(Color.WHITE);
        mathPaint2.setStyle(Paint.Style.FILL);
        mathPaint2.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                int n = game().getObject(r, c);
                if (n == 0) continue;
                String text = String.valueOf(n);
                textPaint.setColor(game().get(r, c) == n ? Color.GRAY : Color.WHITE);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r++) {
            HintState s = game().getRowState(r);
            if (s == HintState.Normal) continue;
            hintPaint.setColor(s == HintState.Complete ? Color.GREEN : Color.RED);
            int c = cols() - 1;
            canvas.drawArc(cwc(c + 1) - 20, chr2(r) - 20, cwc(c + 1) + 20, chr2(r) + 20, 0, 360, true, hintPaint);
        }
        for (int c = 0; c < cols(); c++) {
            HintState s = game().getColState(c);
            if (s == HintState.Normal) continue;
            hintPaint.setColor(s == HintState.Complete ? Color.GREEN : Color.RED);
            int r = rows() - 1;
            canvas.drawArc(cwc2(c) - 20, chr(r + 1) - 20, cwc2(c) + 20, chr(r + 1) + 20, 0, 360, true, hintPaint);
        }
        for (Map.Entry<Position, MathraxHint> entry : game().pos2hint.entrySet()) {
            Position p = entry.getKey();
            int r = p.row + 1, c = p.col + 1;
            canvas.drawArc(cwc(c) - cellWidth / 4, chr(r) - cellHeight / 4, cwc(c) + cellWidth / 4, chr(r) + cellHeight / 4, 0, 360, true, mathPaint1);
            canvas.drawArc(cwc(c) - cellWidth / 4, chr(r) - cellHeight / 4, cwc(c) + cellWidth / 4, chr(r) + cellHeight / 4, 0, 360, true, mathPaint2);
            String text = entry.getValue().toString();
            HintState s = game().getPosState(p);
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            drawTextCentered(text, cwc(c) - cellWidth / 4, chr(r) - cellHeight / 4, cellWidth / 2, cellHeight / 2, canvas, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            MathraxGameMove move = new MathraxGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
