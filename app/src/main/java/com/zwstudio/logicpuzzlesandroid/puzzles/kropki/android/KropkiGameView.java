package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiHint;

/**
 * TODO: document your custom view class.
 */
public class KropkiGameView extends CellsGameView {

    private KropkiGameActivity activity() {return (KropkiGameActivity)getContext();}
    private KropkiGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint hintPaint = new Paint();
    private Paint hintPaint2 = new Paint();

    public KropkiGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public KropkiGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public KropkiGameView(Context context, AttributeSet attrs, int defStyle) {
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
        textPaint.setColor(Color.WHITE);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(5);
        hintPaint2.setStyle(Paint.Style.STROKE);
        hintPaint2.setStrokeWidth(5);
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
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        if (isInEditMode()) return;
        if (game().bordered)
            for (int r = 0; r < rows() + 1; r++)
                for (int c = 0; c < cols() + 1; c++) {
                    if (game().dots.get(r, c, 1) == GridLineObject.Line)
                        canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), linePaint);
                    if (game().dots.get(r, c, 2) == GridLineObject.Line)
                        canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), linePaint);
                }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 2; i++) {
                    if (i == 0 && c == cols() -  1 || i == 1 && r == rows() - 1) continue;
                    KropkiHint kh = (i == 0 ? game().pos2horzHint : game().pos2vertHint).get(p);
                    if (kh == KropkiHint.None) continue;
                    HintState s = (i == 0 ? game().getHorzState(p) : game().getVertState(p));
                    if (s == null) s = HintState.Normal;
                    hintPaint.setColor(kh == KropkiHint.Consecutive ? Color.WHITE : Color.BLACK);
                    hintPaint2.setColor(
                            s == HintState.Complete ? Color.GREEN :
                            s == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    if (i == 0) {
                        canvas.drawArc(cwc(c + 1) - 20, chr2(r) - 20, cwc(c + 1) + 20, chr2(r) + 20, 0, 360, true, hintPaint);
                        canvas.drawArc(cwc(c + 1) - 20, chr2(r) - 20, cwc(c + 1) + 20, chr2(r) + 20, 0, 360, true, hintPaint2);
                    }
                    else {
                        canvas.drawArc(cwc2(c) - 20, chr(r + 1) - 20, cwc2(c) + 20, chr(r + 1) + 20, 0, 360, true, hintPaint);
                        canvas.drawArc(cwc2(c) - 20, chr(r + 1) - 20, cwc2(c) + 20, chr(r + 1) + 20, 0, 360, true, hintPaint2);
                    }
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            KropkiGameMove move = new KropkiGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
