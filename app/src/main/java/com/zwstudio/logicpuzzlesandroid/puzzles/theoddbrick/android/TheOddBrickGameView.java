package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGameMove;

public class TheOddBrickGameView extends CellsGameView {

    private TheOddBrickGameActivity activity() {return (TheOddBrickGameActivity)getContext();}
    private TheOddBrickGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint hintPaint = new Paint();

    public TheOddBrickGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public TheOddBrickGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TheOddBrickGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        textPaint.setAntiAlias(true);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Position p = new Position(r, c);
                int n = game().getObject(p);
                if (n == 0) continue;
                String text = String.valueOf(n);
                HintState s = game().getPosState(p);
                textPaint.setColor(
                        game().get(r, c) == n ? Color.GRAY :
                        s == HintState.Normal ? Color.WHITE :
                        s == HintState.Complete ? Color.GREEN : Color.RED
                );
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        if (isInEditMode()) return;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                if (game().dots.get(r, c, 1) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), linePaint);
                if (game().dots.get(r, c, 2) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), linePaint);
            }
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            TheOddBrickGameMove move = new TheOddBrickGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
