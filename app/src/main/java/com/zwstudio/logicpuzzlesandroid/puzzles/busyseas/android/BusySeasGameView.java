package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasTowerObject;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class BusySeasGameView extends CellsGameView {

    private BusySeasGameActivity activity() {return (BusySeasGameActivity)getContext();}
    private BusySeasGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint forbiddenPaint = new Paint();
    private Drawable dTree;

    public BusySeasGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public BusySeasGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BusySeasGameView(Context context, AttributeSet attrs, int defStyle) {
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
        dTree = fromImageToDrawable("tree.png");
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
                Position p = new Position(r, c);
                BusySeasObject o = game().getObject(p);
                if (o instanceof BusySeasTowerObject) {
                    BusySeasTowerObject o2 = (BusySeasTowerObject) o;
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    int alpaha = o2.state == AllowedObjectState.Error ? 50 : 0;
                    dTree.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTree.draw(canvas);
                } else if (o instanceof BusySeasMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                else if (o instanceof BusySeasForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
                Integer n = game().pos2hint.get(p);
                if (n != null) {
                    HintState state = game().getHintState(p);
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
            BusySeasGameMove move = new BusySeasGameMove() {{
                p = new Position(row, col);
                obj = new BusySeasEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move, MarkerOptions.values()[activity().doc().getMarkerOption()],
                    activity().doc().isAllowedObjectsOnly()))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
