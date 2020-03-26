package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeObject;

public class PaintTheNurikabeGameView extends CellsGameView {

    private PaintTheNurikabeGameActivity activity() {return (PaintTheNurikabeGameActivity)getContext();}
    private PaintTheNurikabeGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint filledPaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public PaintTheNurikabeGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public PaintTheNurikabeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PaintTheNurikabeGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        filledPaint.setColor(Color.rgb(128, 0, 128));
        filledPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Position p = new Position(r, c);
                PaintTheNurikabeObject o = game().getObject(p);
                switch (o) {
                case Painted:
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, filledPaint);
                    break;
                case Marker:
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                    break;
                }
                Integer n = game().pos2hint.get(p);
                if (n != null) {
                    HintState state = game().pos2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    String text = String.valueOf(n);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                }
            }
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                if (game().dots.get(r, c, 1) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), linePaint);
                if (game().dots.get(r, c, 2) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), linePaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            PaintTheNurikabeGameMove move = new PaintTheNurikabeGameMove() {{
                p = new Position(row, col);
                obj = PaintTheNurikabeObject.Empty;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
