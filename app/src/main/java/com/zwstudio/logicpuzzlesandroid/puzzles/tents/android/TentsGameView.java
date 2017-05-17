package com.zwstudio.logicpuzzlesandroid.puzzles.tents.android;

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
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsTentObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsTreeObject;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class TentsGameView extends CellsGameView {

    private TentsGameActivity activity() {return (TentsGameActivity)getContext();}
    private TentsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint markerPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Drawable dTree;
    private Drawable dTent;

    public TentsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public TentsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TentsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setStrokeWidth(5);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        dTree = fromImageToDrawable("tree.png");
        dTent = fromImageToDrawable("tent.png");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (cols() < 1 || rows() < 1) return;
        cellWidth = getWidth() / (cols() + 1) - 1;
        cellHeight = getHeight() / (rows() + 1) - 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                TentsObject o = game().getObject(r, c);
                if (o instanceof TentsTreeObject) {
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    dTree.setColorFilter(Color.argb(0, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTree.draw(canvas);
                } else if (o instanceof TentsTentObject) {
                    TentsTentObject o2 = (TentsTentObject) o;
                    dTent.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    int alpaha = o2.state == AllowedObjectState.Error ? 50 : 0;
                    dTent.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTent.draw(canvas);
                } else if (o instanceof TentsMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                else if (o instanceof TentsForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
            }
        if (isInEditMode()) return;
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
            TentsGameMove move = new TentsGameMove() {{
                p = new Position(row, col);
                obj = new TentsEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
