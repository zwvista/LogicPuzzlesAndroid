package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsBattleShipBottomObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsBattleShipLeftObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsBattleShipMiddleObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsBattleShipRightObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsBattleShipTopObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsBattleShipUnitObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsObject;

public class MineShipsGameView extends CellsGameView {

    private MineShipsGameActivity activity() {return (MineShipsGameActivity)getContext();}
    private MineShipsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint whitePaint = new Paint();
    private Paint markerPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public MineShipsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public MineShipsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MineShipsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setStrokeWidth(5);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
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
                MineShipsObject o = game().getObject(p);
                Path path = new Path();
                if (o instanceof MineShipsBattleShipUnitObject)
                    canvas.drawArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 0, 360, true, whitePaint);
                else if (o instanceof MineShipsBattleShipMiddleObject)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, whitePaint);
                else if (o instanceof MineShipsBattleShipLeftObject) {
                    path.addRect(cwc2(c), chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 90, 180);
                    canvas.drawPath(path, whitePaint);
                }
                else if (o instanceof MineShipsBattleShipTopObject) {
                    path.addRect(cwc(c) + 4, chr2(r), cwc(c + 1) - 4, chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 180, 180);
                    canvas.drawPath(path, whitePaint);
                }
                else if (o instanceof MineShipsBattleShipRightObject) {
                    path.addRect(cwc(c) + 4, chr(r) + 4, cwc2(c), chr(r + 1) - 4, Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 270, 180);
                    canvas.drawPath(path, whitePaint);
                }
                else if (o instanceof MineShipsBattleShipBottomObject) {
                    path.addRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr2(r), Path.Direction.CW);
                    path.addArc(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, 0, 180);
                    canvas.drawPath(path, whitePaint);
                }
                else if (o instanceof MineShipsMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                else if (o instanceof MineShipsForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
                Integer n = game().pos2hint.get(p);
                if (n != null) {
                    HintState state = game().pos2State(p);
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
            MineShipsGameMove move = new MineShipsGameMove() {{
                p = new Position(row, col);
                obj = new MineShipsEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
