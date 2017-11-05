package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsArea;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsObject;

/**
 * TODO: document your custom view class.
 */
public class MagnetsGameView extends CellsGameView {

    private MagnetsGameActivity activity() {return (MagnetsGameActivity)getContext();}
    private MagnetsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows() + 2;}
    @Override protected int colsInView() {return cols() + 2;}
    private Paint gridPaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Drawable dPositive, dNegative;

    public MagnetsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public MagnetsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MagnetsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        dPositive = fromImageToDrawable("images/positive.png");
        dNegative = fromImageToDrawable("images/negative.png");
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        if (isInEditMode()) return;
        for (MagnetsArea a : game().areas) {
            int r = a.p.row, c = a.p.col;
            switch (a.type) {
            case Single:
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                canvas.drawLine(cwc(c + 1), chr(r), cwc(c), chr(r + 1), gridPaint);
                break;
            case Horizontal:
                canvas.drawRect(cwc(c), chr(r), cwc(c + 2), chr(r + 1), gridPaint);
                break;
            case Vertical:
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 2), gridPaint);
                break;
            }
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                MagnetsObject o = game().getObject(r, c);
                switch (o) {
                case Positive:
                    dPositive.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    dPositive.draw(canvas);
                    break;
                case Negative:
                    dNegative.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    dNegative.draw(canvas);
                    break;
                case Marker:
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                    break;
                }
            }
        dPositive.setBounds(cwc(cols()), chr(rows()), cwc(cols() + 1), chr(rows() + 1));
        dPositive.setColorFilter(Color.argb(75, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        dPositive.draw(canvas);
        dNegative.setBounds(cwc(cols() + 1), chr(rows() + 1), cwc(cols() + 2), chr(rows() + 2));
        dNegative.setColorFilter(Color.argb(75, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        dNegative.draw(canvas);
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < 2; c++) {
                int id = r * 2 + c;
                HintState s = game().getRowState(id);
                textPaint.setColor(
                        s == HintState.Complete ? Color.GREEN :
                        s == HintState.Error ? Color.RED :
                        Color.WHITE
                );
                int n = game().row2hint[id];
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(cols() + c), chr(r), canvas, textPaint);
            }
        }
        for (int c = 0; c < cols(); c++) {
            for (int r = 0; r < 2; r++) {
                int id = c * 2 + r;
                HintState s = game().getColState(id);
                textPaint.setColor(
                        s == HintState.Complete ? Color.GREEN :
                        s == HintState.Error ? Color.RED :
                        Color.WHITE
                );
                int n = game().col2hint[id];
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(rows() + r), canvas, textPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            MagnetsGameMove move = new MagnetsGameMove() {{
                p = new Position(row, col);
                obj = MagnetsObject.Empty;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
