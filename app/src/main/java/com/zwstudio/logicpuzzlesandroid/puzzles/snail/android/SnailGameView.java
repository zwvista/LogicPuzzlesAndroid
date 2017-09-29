package com.zwstudio.logicpuzzlesandroid.puzzles.snail.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class SnailGameView extends CellsGameView {

    private SnailGameActivity activity() {return (SnailGameActivity)getContext();}
    private SnailGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public SnailGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public SnailGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SnailGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        markerPaint.setColor(Color.GREEN);
        markerPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (cols() < 1 || rows() < 1) return;
        cellWidth = getWidth() / (cols() + 1) - 1;
        cellHeight = getHeight() / (rows() + 1) - 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 1; r < rows() - 1; r++)
            for (int c = 1; c < cols() - 1; c++)
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
        for (int i = 1; i < game().snailPathLine.size(); i++) {
            Position p1 = game().snailPathLine.get(i - 1), p2 = game().snailPathLine.get(i);
            canvas.drawLine(cwc(p1.col), chr(p1.row), cwc(p2.col), chr(p2.row), linePaint);
        }
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                if (game().getPositionState(r, c) == HintState.Complete)
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, markerPaint);
                char ch = game().getObject(r, c);
                if (ch == ' ') continue;
                textPaint.setColor(
                    game().get(r, c) == ch ? Color.GRAY : Color.WHITE
                );
                String text = String.valueOf(ch);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        textPaint.setColor(Color.RED);
        for (int r = 0; r < rows(); r++)
            if (game().getRowState(r) == HintState.Error)
                drawTextCentered("123", cwc(cols()), chr(r), canvas, textPaint);
        for (int c = 0; c < cols(); c++)
            if (game().getColState(c) == HintState.Error)
                drawTextCentered("123", cwc(c), chr(rows()), canvas, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            SnailGameMove move = new SnailGameMove() {{
                p = new Position(row, col);
                obj = ' ';
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
