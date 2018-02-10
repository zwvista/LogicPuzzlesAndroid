package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove;

import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class FutoshikiGameView extends CellsGameView {

    private FutoshikiGameActivity activity() {return (FutoshikiGameActivity)getContext();}
    private FutoshikiGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint hintPaint = new Paint();

    public FutoshikiGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public FutoshikiGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FutoshikiGameView(Context context, AttributeSet attrs, int defStyle) {
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
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r += 2)
            for (int c = 0; c < cols(); c += 2) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                char ch = game().getObject(r, c);
                if (ch == ' ') continue;
                String text = String.valueOf(ch);
                textPaint.setColor(game().get(r, c) == ch ? Color.GRAY : Color.WHITE);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r += 2) {
            HintState s = game().getRowState(r);
            if (s == HintState.Normal) continue;
            hintPaint.setColor(s == HintState.Complete ? Color.GREEN : Color.RED);
            int c = cols() - 1;
            canvas.drawArc(cwc(c + 1) - 20, chr2(r) - 20, cwc(c + 1) + 20, chr2(r) + 20, 0, 360, true, hintPaint);
        }
        for (int c = 0; c < cols(); c += 2) {
            HintState s = game().getColState(c);
            if (s == HintState.Normal) continue;
            hintPaint.setColor(s == HintState.Complete ? Color.GREEN : Color.RED);
            int r = rows() - 1;
            canvas.drawArc(cwc2(c) - 20, chr(r + 1) - 20, cwc2(c) + 20, chr(r + 1) + 20, 0, 360, true, hintPaint);
        }
        for (Map.Entry<Position, Character> entry : game().pos2hint.entrySet()) {
            Position p = entry.getKey();
            int r = p.row, c = p.col;
            String text = entry.getValue().toString();
            HintState s = game().getPosState(p);
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            FutoshikiGameMove move = new FutoshikiGameMove() {{
                p = new Position(row, col);
                obj = ' ';
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
