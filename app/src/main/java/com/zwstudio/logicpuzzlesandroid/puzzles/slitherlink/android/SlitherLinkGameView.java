package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGameMove;

/**
 * TODO: document your custom view class.
 */
public class SlitherLinkGameView extends CellsGameView {

    private SlitherLinkGameActivity activity() {return (SlitherLinkGameActivity)getContext();}
    private SlitherLinkGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows() - 1;}
    private int cols() {return isInEditMode() ? 5 : game().cols() - 1;}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public SlitherLinkGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public SlitherLinkGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SlitherLinkGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        markerPaint.setColor(Color.YELLOW);
        markerPaint.setStyle(Paint.Style.STROKE);
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
                Integer n = game().pos2hint.get(p);
                if (n != null) {
                    HintState state = game().hint2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    String text = String.valueOf(n);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                }
            }
        if (isInEditMode()) return;
        int markerOffset = 20;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                GridLineObject[] dotObj = game().getObject(r, c);
                switch (dotObj[1]){
                case Line:
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), linePaint);
                    break;
                case Marker:
                    canvas.drawLine(cwc2(c) - markerOffset, chr(r) - markerOffset, cwc2(c) + markerOffset, chr(r) + markerOffset, markerPaint);
                    canvas.drawLine(cwc2(c) - markerOffset, chr(r) + markerOffset, cwc2(c) + markerOffset, chr(r) - markerOffset, markerPaint);
                    break;
                }
                switch (dotObj[2]){
                case Line:
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), linePaint);
                    break;
                case Marker:
                    canvas.drawLine(cwc(c) - markerOffset, chr2(r) - markerOffset, cwc(c) + markerOffset, chr2(r) + markerOffset, markerPaint);
                    canvas.drawLine(cwc(c) - markerOffset, chr2(r) + markerOffset, cwc(c) + markerOffset, chr2(r) - markerOffset, markerPaint);
                    break;
                }
            }
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
            SlitherLinkGameMove move = new SlitherLinkGameMove() {{
                p = new Position(row, col);
                obj = GridLineObject.Empty;
                dir = yOffset >= -offset && yOffset <= offset ? 1 : 2;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
