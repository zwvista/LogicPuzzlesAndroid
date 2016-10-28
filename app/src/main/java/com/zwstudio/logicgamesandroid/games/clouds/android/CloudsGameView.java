package com.zwstudio.logicgamesandroid.games.clouds.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zwstudio.logicgamesandroid.games.clouds.data.CloudsGameProgress;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGame;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGameMove;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsMarkerOptions;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsObject;
import com.zwstudio.logicgamesandroid.main.domain.HintState;
import com.zwstudio.logicgamesandroid.common.domain.Position;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class CloudsGameView extends View {

    private CloudsGameActivity activity() {return (CloudsGameActivity)getContext();}
    private CloudsGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private int cellWidth, cellHeight;
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint lightPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public CloudsGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public CloudsGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CloudsGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lightPaint.setColor(Color.YELLOW);
        lightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    // http://stackoverflow.com/questions/11120392/android-center-text-on-canvas
    private void drawTextCentered(String text, int x, int y, Canvas canvas) {
        float xPos = x + (cellWidth - textPaint.measureText(text)) / 2;
        float yPos = y + (cellHeight - textPaint.descent() - textPaint.ascent()) / 2;
        canvas.drawText(text, xPos, yPos, textPaint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(c * cellWidth + 1, r * cellHeight + 1,
                        (c + 1) * cellWidth + 1, (r + 1) * cellHeight + 1,
                        gridPaint);
                if (isInEditMode()) continue;
                CloudsObject o = game().getObject(r, c);
                switch (o) {
                case Cloud:
                    canvas.drawRect(c * cellWidth + 5, r * cellHeight + 5,
                            (c + 1) * cellWidth - 3, (r + 1) * cellHeight - 3,
                            wallPaint);
                    break;
                case Marker:
                    canvas.drawArc(c * cellWidth + cellWidth / 2 - 19, r * cellHeight + cellHeight / 2 - 19,
                            c * cellWidth + cellWidth / 2 + 21, r * cellHeight + cellHeight / 2 + 21,
                            0, 360, true, wallPaint);
                    break;
                }
            }
        for (int r = 0; r < rows(); r++) {
            HintState s = game().getRowState(r);
            textPaint.setColor(
                    s == HintState.Complete ? Color.GREEN :
                    s == HintState.Error ? Color.RED :
                    Color.WHITE
            );
            int n = game().row2hint[r];
            String text = String.valueOf(n);
            textPaint.setTextSize(cellHeight);
            drawTextCentered(text, cols() * cellWidth + 1, r * cellHeight + 1, canvas);
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
            textPaint.setTextSize(cellHeight);
            drawTextCentered(text, c * cellWidth + 1, rows() * cellHeight + 1, canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            CloudsGameProgress rec = activity().doc().gameProgress();
            CloudsGameMove move = new CloudsGameMove();
            move.p = new Position(row, col);
            move.obj = CloudsObject.Empty;
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move, CloudsMarkerOptions.values()[rec.markerOption]))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
