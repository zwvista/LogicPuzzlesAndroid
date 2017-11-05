package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
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
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint line1Paint = new Paint();
    private Paint line2Paint = new Paint();
    private Paint markerPaint = new Paint();
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
        line1Paint.setColor(Color.WHITE);
        line1Paint.setStyle(Paint.Style.STROKE);
        line1Paint.setStrokeWidth(20);
        line2Paint.setColor(Color.GREEN);
        line2Paint.setStyle(Paint.Style.STROKE);
        line2Paint.setStrokeWidth(20);
        markerPaint.setColor(Color.YELLOW);
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(5);
        dotPaint.setColor(Color.WHITE);
        dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dotPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
        if (isInEditMode()) return;
        int markerOffset = 20;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                GridLineObject[] dotObj = game().getObject(r, c);
                switch (dotObj[1]){
                case Line:
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r),
                            game().get(r, c)[1] == GridLineObject.Line ? line1Paint : line2Paint);
                    break;
                case Marker:
                    canvas.drawLine(cwc2(c) - markerOffset, chr(r) - markerOffset, cwc2(c) + markerOffset, chr(r) + markerOffset, markerPaint);
                    canvas.drawLine(cwc2(c) - markerOffset, chr(r) + markerOffset, cwc2(c) + markerOffset, chr(r) - markerOffset, markerPaint);
                    break;
                }
                switch (dotObj[2]){
                case Line:
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1),
                            game().get(r, c)[2] == GridLineObject.Line ? line1Paint : line2Paint);
                    break;
                case Marker:
                    canvas.drawLine(cwc(c) - markerOffset, chr2(r) - markerOffset, cwc(c) + markerOffset, chr2(r) + markerOffset, markerPaint);
                    canvas.drawLine(cwc(c) - markerOffset, chr2(r) + markerOffset, cwc(c) + markerOffset, chr2(r) - markerOffset, markerPaint);
                    break;
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
                obj = GridLineObject.Empty;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
