package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperMineObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperObject;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class MinesweeperGameView extends CellsGameView {

    private MinesweeperGameActivity activity() {return (MinesweeperGameActivity)getContext();}
    private MinesweeperGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint forbiddenPaint = new Paint();
    private Drawable dTree;

    public MinesweeperGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public MinesweeperGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MinesweeperGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
        dTree = fromImageToDrawable("images/tree.png");
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Position p = new Position(r, c);
                MinesweeperObject o = game().getObject(p);
                if (o instanceof MinesweeperMineObject) {
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    dTree.setColorFilter(Color.argb(0, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTree.draw(canvas);
                } else if (o instanceof MinesweeperMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                else if (o instanceof MinesweeperForbiddenObject)
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
            MinesweeperGameMove move = new MinesweeperGameMove() {{
                p = new Position(row, col);
                obj = new MinesweeperEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
