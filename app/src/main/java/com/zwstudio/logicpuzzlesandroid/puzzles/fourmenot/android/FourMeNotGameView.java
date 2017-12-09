package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotBlockObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotTreeObject;

/**
 * TODO: document your custom view class.
 */
public class FourMeNotGameView extends CellsGameView {

    private FourMeNotGameActivity activity() {return (FourMeNotGameActivity)getContext();}
    private FourMeNotGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint markerPaint = new Paint();
    private Paint fixedPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private Drawable dTree;

    public FourMeNotGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public FourMeNotGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FourMeNotGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setStrokeWidth(5);
        fixedPaint.setColor(Color.WHITE);
        fixedPaint.setStyle(Paint.Style.STROKE);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
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
                FourMeNotObject o = game().getObject(p);
                if (o instanceof FourMeNotTreeObject) {
                    FourMeNotTreeObject o2 = (FourMeNotTreeObject) o;
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    int alpaha = o2.state == AllowedObjectState.Error ? 50 : 0;
                    dTree.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTree.draw(canvas);
                    if (game().get(p) instanceof FourMeNotTreeObject)
                        canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, fixedPaint);
                } else if (o instanceof FourMeNotMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                else if (o instanceof FourMeNotForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
                else if (o instanceof FourMeNotBlockObject)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            FourMeNotGameMove move = new FourMeNotGameMove() {{
                p = new Position(row, col);
                obj = new FourMeNotEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
