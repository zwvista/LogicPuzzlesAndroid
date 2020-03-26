package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android;

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
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerForbiddenObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerTreeObject;

import java.util.Map;

import fj.P2;

public class GardenerGameView extends CellsGameView {

    private GardenerGameActivity activity() {return (GardenerGameActivity)getContext();}
    private GardenerGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint markerPaint = new Paint();
    private Paint forbiddenPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint hintPaint = new Paint();
    private Drawable dTree;

    public GardenerGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GardenerGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GardenerGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint.setStrokeWidth(5);
        forbiddenPaint.setColor(Color.RED);
        forbiddenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        forbiddenPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(5);
        hintPaint.setColor(Color.RED);
        dTree = fromImageToDrawable("images/tree.png");
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
        if (isInEditMode()) return;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                if (game().dots.get(r, c, 1) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), linePaint);
                if (game().dots.get(r, c, 2) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), linePaint);
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                GardenerObject o = game().getObject(p);
                if (o instanceof GardenerTreeObject) {
                    GardenerTreeObject o2 = (GardenerTreeObject) o;
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    int alpaha = o2.state == AllowedObjectState.Error ? 50 : 0;
                    dTree.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTree.draw(canvas);
                } else if (o instanceof GardenerMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                else if (o instanceof GardenerForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, forbiddenPaint);
                if (game().invalidSpaces(p, true))
                    canvas.drawArc(cwc(c + 1) - 20, chr2(r) - 20, cwc(c + 1) + 20, chr2(r) + 20, 0, 360, true, hintPaint);
                if (game().invalidSpaces(p, false))
                    canvas.drawArc(cwc2(c) - 20, chr(r + 1) - 20, cwc2(c) + 20, chr(r + 1) + 20, 0, 360, true, hintPaint);
            }
        for (Map.Entry<Position, P2<Integer, Integer>> entry : game().pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue()._1();
            HintState state = game().pos2State(p);
            textPaint.setColor(
                    state == HintState.Complete ? Color.GREEN :
                    state == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            String text = String.valueOf(n);
            int r = p.row, c = p.col;
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            GardenerGameMove move = new GardenerGameMove() {{
                p = new Position(row, col);
                obj = new GardenerEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
