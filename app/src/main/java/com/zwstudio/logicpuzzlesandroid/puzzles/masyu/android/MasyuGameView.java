package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuMarkerOptions;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuObjectOrientation;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import static android.R.transition.move;
import static android.os.Build.VERSION_CODES.M;
import static java.lang.Math.abs;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class MasyuGameView extends CellsGameView {

    private MasyuGameActivity activity() {return (MasyuGameActivity)getContext();}
    private MasyuGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint pearlBlackPaint = new Paint();
    private Paint pearlWhitePaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public MasyuGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public MasyuGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MasyuGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.GREEN);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        pearlBlackPaint.setColor(Color.WHITE);
        pearlBlackPaint.setStyle(Paint.Style.STROKE);
        pearlBlackPaint.setStrokeWidth(5);
        pearlWhitePaint.setColor(Color.WHITE);
        pearlWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (cols() < 1 || rows() < 1) return;
        cellWidth = getWidth() / cols() - 1;
        cellHeight = getHeight() / rows() - 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                char ch = game().get(r, c);
                if (ch != ' ')
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true,
                            ch == 'B' ? pearlBlackPaint : pearlWhitePaint);
            }
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                int[] dirs = {1, 2};
                for (int dir : dirs) {
                    boolean b = game().getObject(r, c)[dir];
                    if (!b) continue;
                    if (dir == 1)
                        canvas.drawLine(cwc2(c), chr2(r), cwc2(c + 1), chr2(r), linePaint);
                    else
                        canvas.drawLine(cwc2(c), chr2(r), cwc2(c), chr2(r + 1), linePaint);
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            double dx = event.getX() - (col + 0.5) * cellWidth;
            double dy = event.getY() - (row + 0.5) * cellHeight;
            double dx2 = abs(dx), dy2 = abs(dy);
            MasyuGameMove move = new MasyuGameMove();
            move.p = new Position(row, col);
            move.dir = -dy2 <= dx && dx <= dy2 ? dy > 0 ? 2 : 0 :
                -dx2 <= dy && dy <= dx2 ? dx > 0 ? 1 : 3 : 0;
            MasyuGameProgress rec = activity().doc().gameProgress();
            if (game().setObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
