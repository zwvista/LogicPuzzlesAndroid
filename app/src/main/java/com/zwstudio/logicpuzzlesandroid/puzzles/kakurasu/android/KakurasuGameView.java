package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuObject;

/**
 * TODO: document your custom view class.
 */
public class KakurasuGameView extends CellsGameView {

    private KakurasuGameActivity activity() {return (KakurasuGameActivity)getContext();}
    private KakurasuGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint markerPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public KakurasuGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public KakurasuGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public KakurasuGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.STROKE);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 1; r < rows() - 1; r++)
            for (int c = 1; c < cols() - 1; c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                KakurasuObject o = game().getObject(r, c);
                switch (o) {
                case Cloud:
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                    break;
                case Marker:
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
                    break;
                case Forbidden:
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
                    break;
                }
            }
        if (isInEditMode()) return;
        for (int r = 1; r < rows() - 1; r++)
            for (int i = 0; i < 2; i++) {
                int c = i == 0 ? 0 : cols() - 1;
                HintState s = game().getRowState(r * 2 + i);
                textPaint.setColor(
                        s == HintState.Complete ? Color.GREEN :
                        s == HintState.Error ? Color.RED :
                        Color.WHITE
                );
                int n = game().row2hint[r * 2 + i];
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                if (i == 1)
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, markerPaint);
            }
        for (int c = 1; c < cols() - 1; c++)
            for (int i = 0; i < 2; i++) {
                int r = i == 0 ? 0 : rows() - 1;
                HintState s = game().getColState(c * 2 + i);
                textPaint.setColor(
                        s == HintState.Complete ? Color.GREEN :
                        s == HintState.Error ? Color.RED :
                        Color.WHITE
                );
                int n = game().col2hint[c * 2 + i];
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                if (i == 1)
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, markerPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            KakurasuGameMove move = new KakurasuGameMove() {{
                p = new Position(row, col);
                obj = KakurasuObject.Empty;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
