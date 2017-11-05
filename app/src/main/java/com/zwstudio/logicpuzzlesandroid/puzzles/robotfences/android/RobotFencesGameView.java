package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesInfo;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class RobotFencesGameView extends CellsGameView {

    private RobotFencesGameActivity activity() {return (RobotFencesGameActivity)getContext();}
    private RobotFencesGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows() + 1;}
    @Override protected int colsInView() {return cols() + 1;}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public RobotFencesGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public RobotFencesGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RobotFencesGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
                int n = game().getObject(r, c);
                if (n == 0) continue;
                String text = String.valueOf(n);
                textPaint.setColor(game().get(r, c) == n ? Color.GRAY : Color.WHITE);
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
            RobotFencesInfo info = game().getRowInfo(r);
            HintState s = info.state;
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            String text = info.nums;
            drawTextCentered(text, cwc(cols()), chr(r), canvas, textPaint);
        }
        for (int c = 0; c < cols(); c++) {
            RobotFencesInfo info = game().getColInfo(c);
            HintState s = info.state;
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            String text = info.nums;
            drawTextCentered(text, cwc(c), chr(rows()), canvas, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            RobotFencesGameMove move = new RobotFencesGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
