package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourObject;

public class DisconnectFourGameView extends CellsGameView {

    private DisconnectFourGameActivity activity() {return (DisconnectFourGameActivity)getContext();}
    private DisconnectFourGame game() {return activity().game;}
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

    public DisconnectFourGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public DisconnectFourGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DisconnectFourGameView(Context context, AttributeSet attrs, int defStyle) {
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
                DisconnectFourObject o = game().getObject(p);
                if (o == DisconnectFourObject.Empty) continue;
                dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                int alpaha = game().pos2State(p) == AllowedObjectState.Error ? 50 : 0;
                dTree.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                if (o == DisconnectFourObject.Red) { canvas.save(); canvas.rotate(180, cwc2(c), chr2(r)); }
                dTree.draw(canvas);
                if (o == DisconnectFourObject.Red) canvas.restore();
                if (game().get(p) != DisconnectFourObject.Empty)
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, fixedPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            DisconnectFourGameMove move = new DisconnectFourGameMove() {{
                p = new Position(row, col);
                obj = DisconnectFourObject.Empty;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
