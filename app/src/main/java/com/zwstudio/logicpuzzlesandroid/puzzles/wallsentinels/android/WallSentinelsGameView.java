package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsHintLandObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsHintWallObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsWallObject;

/**
 * TODO: document your custom view class.
 */
public class WallSentinelsGameView extends CellsGameView {

    private WallSentinelsGameActivity activity() {return (WallSentinelsGameActivity)getContext();}
    private WallSentinelsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint wallPaint = new Paint();

    public WallSentinelsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public WallSentinelsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WallSentinelsGameView(Context context, AttributeSet attrs, int defStyle) {
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
        wallPaint.setColor(Color.rgb(128, 0, 128));
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Position p = new Position(r, c);
                WallSentinelsObject o = game().getObject(p);
                if (o instanceof WallSentinelsMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, markerPaint);
                if (o instanceof WallSentinelsWallObject || o instanceof WallSentinelsHintWallObject)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                if (o instanceof WallSentinelsHintLandObject || o instanceof WallSentinelsHintWallObject) {
                    int n = (o instanceof WallSentinelsHintLandObject) ? ((WallSentinelsHintLandObject)o).tiles : ((WallSentinelsHintWallObject)o).tiles;
                    HintState state = (o instanceof WallSentinelsHintLandObject) ? ((WallSentinelsHintLandObject)o).state : ((WallSentinelsHintWallObject)o).state;
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
            WallSentinelsGameMove move = new WallSentinelsGameMove() {{
                p = new Position(row, col);
                obj = new WallSentinelsEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
