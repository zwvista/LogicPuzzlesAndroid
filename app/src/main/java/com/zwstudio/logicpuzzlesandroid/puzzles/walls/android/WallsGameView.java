package com.zwstudio.logicpuzzlesandroid.puzzles.walls.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsHintObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsHorzObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsVertObject;

/**
 * TODO: document your custom view class.
 */
public class WallsGameView extends CellsGameView {

    private WallsGameActivity activity() {return (WallsGameActivity)getContext();}
    private WallsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Drawable dTree;

    public WallsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public WallsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WallsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        textPaint.setAntiAlias(true);
        dTree = fromImageToDrawable("images/tree.png");
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                WallsObject o = game().getObject(p);
                if (o instanceof WallsHorzObject || o instanceof WallsVertObject) {
                    boolean isHorz = o instanceof WallsHorzObject;
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    dTree.setColorFilter(Color.argb(0, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    if (isHorz) { canvas.save(); canvas.rotate(90, cwc2(c), chr2(r)); }
                    dTree.draw(canvas);
                    if (isHorz) canvas.restore();
                } else if (o instanceof WallsHintObject) {
                    WallsHintObject o2 = (WallsHintObject) o;
                    String text = String.valueOf(o2.walls);
                    HintState s = o2.state;
                    textPaint.setColor(
                            s == HintState.Normal ? Color.WHITE :
                            s == HintState.Complete ? Color.GREEN : Color.RED
                    );
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
            WallsGameMove move = new WallsGameMove() {{
                p = new Position(row, col);
                obj = new WallsEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
