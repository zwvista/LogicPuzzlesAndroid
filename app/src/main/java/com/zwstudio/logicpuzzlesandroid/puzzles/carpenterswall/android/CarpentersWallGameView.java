package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallDownObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallCornerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallLeftObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallRightObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallUpObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallWallObject;

/**
 * TODO: document your custom view class.
 */
public class CarpentersWallGameView extends CellsGameView {

    private CarpentersWallGameActivity activity() {return (CarpentersWallGameActivity)getContext();}
    private CarpentersWallGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint fixedPaint = new Paint();

    public CarpentersWallGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public CarpentersWallGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CarpentersWallGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        fixedPaint.setColor(Color.WHITE);
        fixedPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                CarpentersWallObject o = game().getObject(r, c);
                if (o instanceof CarpentersWallCornerObject) {
                    CarpentersWallCornerObject o2 = (CarpentersWallCornerObject) o;
                    int n = o2.tiles;
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    String text = n == 0 ? "?" : String.valueOf(n);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, fixedPaint);
                } else if (o instanceof CarpentersWallLeftObject) {
                    CarpentersWallLeftObject o2 = (CarpentersWallLeftObject) o;
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered("<", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersWallRightObject) {
                    CarpentersWallRightObject o2 = (CarpentersWallRightObject) o;
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered(">", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersWallUpObject) {
                    CarpentersWallUpObject o2 = (CarpentersWallUpObject) o;
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered("^", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersWallDownObject) {
                    CarpentersWallDownObject o2 = (CarpentersWallDownObject) o;
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered("v", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersWallWallObject)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                else if (o instanceof CarpentersWallMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            CarpentersWallGameMove move = new CarpentersWallGameMove() {{
                p = new Position(row, col);
                obj = new CarpentersWallEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
