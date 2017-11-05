package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsBattleShipBottomObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsBattleShipLeftObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsBattleShipMiddleObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsBattleShipRightObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsBattleShipTopObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsBattleShipUnitObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsObject;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class LightBattleShipsGameView extends CellsGameView {

    private LightBattleShipsGameActivity activity() {return (LightBattleShipsGameActivity)getContext();}
    private LightBattleShipsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint whitePaint = new Paint();
    private Paint grayPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public LightBattleShipsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public LightBattleShipsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LightBattleShipsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
                Position p = new Position(r, c);
                LightBattleShipsObject o = game().getObject(p);
                Path path = new Path();
                Paint paint = game().pos2obj.containsKey(new Position(r, c)) ? grayPaint : whitePaint;
                if (o instanceof LightBattleShipsBattleShipUnitObject)
                    canvas.drawArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 0, 360, true, paint);
                else if (o instanceof LightBattleShipsBattleShipMiddleObject)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, paint);
                else if (o instanceof LightBattleShipsBattleShipLeftObject) {
                    path.addRect(cwc2(c), chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 90, 180);
                    canvas.drawPath(path, paint);
                }
                else if (o instanceof LightBattleShipsBattleShipTopObject) {
                    path.addRect(cwc(c) + 4, chr2(r), cwc(c + 1) - 4, chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 180, 180);
                    canvas.drawPath(path, paint);
                }
                else if (o instanceof LightBattleShipsBattleShipRightObject) {
                    path.addRect(cwc(c) + 4, chr(r) + 4, cwc2(c), chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 270, 180);
                    canvas.drawPath(path, paint);
                }
                else if (o instanceof LightBattleShipsBattleShipBottomObject) {
                    path.addRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr2(r), Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 0, 180);
                    canvas.drawPath(path, paint);
                }
                else if (o instanceof LightBattleShipsMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, paint);
                else if (o instanceof LightBattleShipsForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            LightBattleShipsGameMove move = new LightBattleShipsGameMove() {{
                p = new Position(row, col);
                obj = new LightBattleShipsEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
