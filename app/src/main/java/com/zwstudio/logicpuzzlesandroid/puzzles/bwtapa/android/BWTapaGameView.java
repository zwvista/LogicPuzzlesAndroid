package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaHintObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaWallObject;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class BWTapaGameView extends CellsGameView {

    private BWTapaGameActivity activity() {return (BWTapaGameActivity)getContext();}
    private BWTapaGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public BWTapaGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public BWTapaGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BWTapaGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
                BWTapaObject o = game().getObject(r, c);
                if (o instanceof BWTapaWallObject) {
                    BWTapaWallObject o2 = (BWTapaWallObject) o;
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                } else if (o instanceof BWTapaHintObject) {
                    BWTapaHintObject o2 = (BWTapaHintObject) o;
                    List<Integer> hint = game().pos2hint.get(new Position(r, c));
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    switch (hint.size()) {
                    case 1:
                        drawTextCentered(String.valueOf(hint.get(0)), cwc(c), chr(r), canvas, textPaint);
                        break;
                    case 2:
                        drawTextCentered(String.valueOf(hint.get(0)), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(String.valueOf(hint.get(1)), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        break;
                    case 3:
                        drawTextCentered(String.valueOf(hint.get(0)), cwc2(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(String.valueOf(hint.get(1)), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(String.valueOf(hint.get(2)), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        break;
                    case 4:
                        drawTextCentered(String.valueOf(hint.get(0)), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(String.valueOf(hint.get(1)), cwc2(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(String.valueOf(hint.get(2)), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(String.valueOf(hint.get(3)), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        break;
                    }
                } else if (o instanceof BWTapaMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            BWTapaGameMove move = new BWTapaGameMove() {{
                p = new Position(row, col);
                obj = new BWTapaEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
