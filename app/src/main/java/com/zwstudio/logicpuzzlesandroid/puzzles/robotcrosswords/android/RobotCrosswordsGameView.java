package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameMove;

import java.util.List;

public class RobotCrosswordsGameView extends CellsGameView {

    private RobotCrosswordsGameActivity activity() {return (RobotCrosswordsGameActivity)getContext();}
    private RobotCrosswordsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint hintPaint = new Paint();

    public RobotCrosswordsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public RobotCrosswordsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RobotCrosswordsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
                int n = game().getObject(r, c);
                if (n == -1)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                else if (n > 0) {
                    String text = String.valueOf(n);
                    textPaint.setColor(game().get(r, c) == n ? Color.GRAY : Color.WHITE);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                }
            }
        if (isInEditMode()) return;
        for (int i = 0; i < game().areas.size(); i++) {
            List<Position> a = game().areas.get(i);
            boolean isHorz = i < game().horzAreaCount;
            for (Position p : a) {
                int r = p.row, c = p.col;
                HintState s = isHorz ? game().getHorzState(p) : game().getVertState(p);
                if (s == HintState.Normal) continue;
                hintPaint.setColor(s == HintState.Complete ? Color.GREEN : Color.RED);
                if (isHorz)
                    canvas.drawArc(cwc(c + 1) - 20, chr2(r) - 20, cwc(c + 1) + 20, chr2(r) + 20, 0, 360, true, hintPaint);
                else
                    canvas.drawArc(cwc2(c) - 20, chr(r + 1) - 20, cwc2(c) + 20, chr(r + 1) + 20, 0, 360, true, hintPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            RobotCrosswordsGameMove move = new RobotCrosswordsGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
