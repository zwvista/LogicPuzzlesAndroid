package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class LoopyGameView extends CellsGameView {

    private LoopyGameActivity activity() {return (LoopyGameActivity)getContext();}
    private LoopyGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows() - 1;}
    private int cols() {return isInEditMode() ? 5 : game().cols() - 1;}
    private Paint gridPaint = new Paint();
    private Paint linePaint1 = new Paint();
    private Paint linePaint2 = new Paint();
    private Paint dotPaint = new Paint();

    public LoopyGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public LoopyGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoopyGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint1.setColor(Color.WHITE);
        linePaint1.setStyle(Paint.Style.STROKE);
        linePaint1.setStrokeWidth(20);
        linePaint2.setColor(Color.GREEN);
        linePaint2.setStyle(Paint.Style.STROKE);
        linePaint2.setStrokeWidth(20);
        dotPaint.setColor(Color.WHITE);
        dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dotPaint.setStrokeWidth(5);
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
            for (int c = 0; c < cols(); c++)
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
        if (isInEditMode()) return;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                int[] dirs = {1, 2};
                for (int dir : dirs) {
                    boolean b = game().getObject(r, c)[dir];
                    if (!b) continue;
                    Paint paint = game().get(r, c)[dir] ? linePaint1 : linePaint2;
                    if (dir == 1)
                        canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), paint);
                    else
                        canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), paint);
               }
            }
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++)
                canvas.drawArc(cwc(c) - 20, chr(r) - 20, cwc(c) + 20, chr(r) + 20, 0, 360, true, dotPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int offset = 30;
            int col = (int)((event.getX() + offset) / cellWidth);
            int row = (int)((event.getY() + offset) / cellHeight);
            int xOffset = (int)event.getX() - col * cellWidth - 1;
            int yOffset = (int)event.getY() - row * cellHeight - 1;
            if (!(xOffset >= -offset && xOffset <= offset || yOffset >= -offset && yOffset <= offset)) return true;
            LoopyGameMove move = new LoopyGameMove() {{
                p = new Position(row, col);
                dir = yOffset >= -offset && yOffset <= offset ? 1 : 2;
            }};
            if (game().setObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
