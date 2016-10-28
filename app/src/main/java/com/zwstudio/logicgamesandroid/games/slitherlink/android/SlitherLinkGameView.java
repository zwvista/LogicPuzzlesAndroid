package com.zwstudio.logicgamesandroid.games.slitherlink.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zwstudio.logicgamesandroid.main.domain.HintState;
import com.zwstudio.logicgamesandroid.common.domain.Position;
import com.zwstudio.logicgamesandroid.games.slitherlink.data.SlitherLinkGameProgress;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkMarkerOptions;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkObject;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkObjectOrientation;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class SlitherLinkGameView extends View {

    private SlitherLinkGameActivity activity() {return (SlitherLinkGameActivity)getContext();}
    private SlitherLinkGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows() - 1;}
    private int cols() {return isInEditMode() ? 5 : game().cols() - 1;}
    private int cellWidth, cellHeight;
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public SlitherLinkGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public SlitherLinkGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SlitherLinkGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        markerPaint.setColor(Color.YELLOW);
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
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
                Position p = new Position(r, c);
                Integer n = game().pos2hint.get(p);
                if (n != null) {
                    HintState state = game().getHintState(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    String text = String.valueOf(n);
                    textPaint.setTextSize(cellHeight);
                    drawTextCentered(text, c * cellWidth + 1, r * cellHeight + 1, canvas);
                }
            }
        int markerOffset = 20;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                SlitherLinkObject[] dotObj = game().getObject(r, c);
                switch (dotObj[1]){
                case Line:
                    canvas.drawLine(c * cellWidth + 1, r * cellHeight + 1, (c + 1) * cellWidth + 1, r * cellHeight + 1, linePaint);
                    break;
                case Marker:
                    canvas.drawLine(c * cellWidth + 1 + cellWidth / 2 - markerOffset, r * cellHeight + 1 - markerOffset, c * cellWidth + 1 + cellWidth / 2 + markerOffset, r * cellHeight + 1 + markerOffset, markerPaint);
                    canvas.drawLine(c * cellWidth + 1 + cellWidth / 2 - markerOffset, r * cellHeight + 1 + markerOffset, c * cellWidth + 1 + cellWidth / 2 + markerOffset, r * cellHeight + 1 - markerOffset, markerPaint);
                    break;
                }
                switch (dotObj[2]){
                case Line:
                    canvas.drawLine(c * cellWidth + 1, r * cellHeight + 1, c * cellWidth + 1, (r + 1) * cellHeight + 1, linePaint);
                    break;
                case Marker:
                    canvas.drawLine(c * cellWidth + 1 - markerOffset, r * cellHeight + 1 + cellHeight / 2 - markerOffset, c * cellWidth + 1 + markerOffset, r * cellHeight + 1 + cellHeight / 2 + markerOffset, markerPaint);
                    canvas.drawLine(c * cellWidth + 1 - markerOffset, r * cellHeight + 1 + cellHeight / 2 + markerOffset, c * cellWidth + 1 + markerOffset, r * cellHeight + 1 + cellHeight / 2 - markerOffset, markerPaint);
                    break;
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int offset = 30;
            int col = (int)((event.getX() + offset) / cellWidth);
            int row = (int)((event.getY() + offset) / cellHeight);
            int xOffset = (int)event.getX() - col * cellWidth - 1;
            int yOffset = (int)event.getY() - row * cellHeight - 1;
            if (!(xOffset >= -offset && xOffset <= offset || yOffset >= -offset && yOffset <= offset)) return true;
            SlitherLinkGameMove move = new SlitherLinkGameMove();
            move.p = new Position(row, col);
            move.obj = SlitherLinkObject.Empty;
            move.objOrientation = yOffset >= -offset && yOffset <= offset ?
                    SlitherLinkObjectOrientation.Horizontal : SlitherLinkObjectOrientation.Vertical;
            SlitherLinkGameProgress rec = activity().doc().gameProgress();
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move, SlitherLinkMarkerOptions.values()[rec.markerOption]))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
