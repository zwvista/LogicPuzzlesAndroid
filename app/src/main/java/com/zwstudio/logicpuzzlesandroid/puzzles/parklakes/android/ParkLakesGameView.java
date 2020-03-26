package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android;

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
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesHintObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesTreeObject;

public class ParkLakesGameView extends CellsGameView {

    private ParkLakesGameActivity activity() {return (ParkLakesGameActivity)getContext();}
    private ParkLakesGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Drawable dTree;

    public ParkLakesGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public ParkLakesGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ParkLakesGameView(Context context, AttributeSet attrs, int defStyle) {
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
                ParkLakesObject o = game().getObject(p);
                if (o instanceof ParkLakesTreeObject) {
                    ParkLakesTreeObject o2 = (ParkLakesTreeObject) o;
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1));
                    int alpaha = o2.state == AllowedObjectState.Error ? 50 : 0;
                    dTree.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dTree.draw(canvas);
                } else if (o instanceof ParkLakesHintObject) {
                    ParkLakesHintObject o2 = (ParkLakesHintObject)o;
                    textPaint.setColor(
                            o2.state == HintState.Complete ? Color.GREEN :
                            o2.state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    String text = o2.tiles == -1 ? "?" : String.valueOf(o2.tiles);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof ParkLakesMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            ParkLakesGameMove move = new ParkLakesGameMove() {{
                p = new Position(row, col);
                obj = new ParkLakesEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
