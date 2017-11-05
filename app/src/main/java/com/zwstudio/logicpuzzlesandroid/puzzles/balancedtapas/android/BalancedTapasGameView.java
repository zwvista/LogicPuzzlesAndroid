package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasHintObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasWallObject;

import java.util.List;

import fj.F;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class BalancedTapasGameView extends CellsGameView {

    private BalancedTapasGameActivity activity() {return (BalancedTapasGameActivity)getContext();}
    private BalancedTapasGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint wallPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public BalancedTapasGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public BalancedTapasGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BalancedTapasGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                BalancedTapasObject o = game().getObject(r, c);
                if (o instanceof BalancedTapasWallObject) {
                    BalancedTapasWallObject o2 = (BalancedTapasWallObject) o;
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                } else if (o instanceof BalancedTapasHintObject) {
                    BalancedTapasHintObject o2 = (BalancedTapasHintObject) o;
                    List<Integer> hint = game().pos2hint.get(new Position(r, c));
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    F<Integer, String> hint2Str = i -> {
                        int n = hint.get(i);
                        return n == -1 ? "?" : String.valueOf(n);
                    };
                    switch (hint.size()) {
                    case 1:
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), canvas, textPaint);
                        break;
                    case 2:
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(hint2Str.f(1), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        break;
                    case 3:
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), cellWidth, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(hint2Str.f(1), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(hint2Str.f(2), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        break;
                    case 4:
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(hint2Str.f(1), cwc2(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(hint2Str.f(2), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        drawTextCentered(hint2Str.f(3), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint);
                        break;
                    }
                } else if (o instanceof BalancedTapasMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
            }
        for (int r = 0; r < rows(); r++) {
            int c = game().left;
            int x = cwc(c) - (c > game().right ? cellWidth / 2 : 0);
            canvas.drawLine(x, chr(r), x, chr(r + 1), linePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            BalancedTapasGameMove move = new BalancedTapasGameMove() {{
                p = new Position(row, col);
                obj = new BalancedTapasEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
