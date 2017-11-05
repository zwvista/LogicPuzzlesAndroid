package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsObject;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class DigitalBattleShipsGameView extends CellsGameView {

    private DigitalBattleShipsGameActivity activity() {return (DigitalBattleShipsGameActivity)getContext();}
    private DigitalBattleShipsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows() + 1;}
    @Override protected int colsInView() {return cols() + 1;}
    private Paint gridPaint = new Paint();
    private Paint grayPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public DigitalBattleShipsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public DigitalBattleShipsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DigitalBattleShipsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        grayPaint.setColor(Color.GRAY);
        grayPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
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
                int n = game().get(r, c);
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                DigitalBattleShipsObject o = game().getObject(r, c);
                Path path = new Path();
                switch (o) {
                case BattleShipUnit:
                    canvas.drawArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 0, 360, true, grayPaint);
                    break;
                case BattleShipMiddle:
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, grayPaint);
                    break;
                case BattleShipLeft:
                    path.addRect(cwc2(c), chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 90, 180);
                    canvas.drawPath(path, grayPaint);
                    break;
                case BattleShipTop:
                    path.addRect(cwc(c) + 4, chr2(r), cwc(c + 1) - 4, chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 180, 180);
                    canvas.drawPath(path, grayPaint);
                    break;
                case BattleShipRight:
                    path.addRect(cwc(c) + 4, chr(r) + 4, cwc2(c), chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 270, 180);
                    canvas.drawPath(path, grayPaint);
                    break;
                case BattleShipBottom:
                    path.addRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr2(r), Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 0, 180);
                    canvas.drawPath(path, grayPaint);
                    break;
                case Marker:
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, grayPaint);
                    break;
                case Forbidden:
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
                    break;
                }
            }
        if (isInEditMode()) return;
        textPaint.setColor(Color.WHITE);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                int n = game().get(r, c);
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        for (int r = 0; r < rows(); r++) {
            HintState s = game().getRowState(r);
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            int n = game().row2hint[r];
            String text = String.valueOf(n);
            drawTextCentered(text, cwc(cols()), chr(r), canvas, textPaint);
        }
        for (int c = 0; c < cols(); c++) {
            HintState s = game().getColState(c);
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            int n = game().col2hint[c];
            String text = String.valueOf(n);
            drawTextCentered(text, cwc(c), chr(rows()), canvas, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            DigitalBattleShipsGameMove move = new DigitalBattleShipsGameMove() {{
                p = new Position(row, col);
                obj = DigitalBattleShipsObject.Empty;
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
