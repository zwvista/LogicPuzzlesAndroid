package com.zwstudio.lightupandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zwstudio.lightupandroid.data.GameDocument;
import com.zwstudio.lightupandroid.domain.Game;
import com.zwstudio.lightupandroid.domain.GameObject;
import com.zwstudio.lightupandroid.domain.LightbulbObject;
import com.zwstudio.lightupandroid.domain.MarkerObject;
import com.zwstudio.lightupandroid.domain.Position;
import com.zwstudio.lightupandroid.domain.WallObject;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class GameView extends View {

    private GameDocument doc() {return ((GameApplication)getContext().getApplicationContext()).getGameDocument();}
    private Game game() {return ((GameActivity)getContext()).game;}
    private int rows() {return game().size().row;}
    private int cols() {return game().size().col;}
    private int cellWidth, cellHeight;
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint lightPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
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
                GameObject o = game().getObject(r, c);
                if (o.lightness > 0)
                    canvas.drawRect(c * cellWidth + 5, r * cellHeight + 5,
                            (c + 1) * cellWidth - 3, (r + 1) * cellHeight - 3,
                            lightPaint);
                if (o instanceof WallObject) {
                    WallObject o2 = (WallObject) o;
                    canvas.drawRect(c * cellWidth + 5, r * cellHeight + 5,
                            (c + 1) * cellWidth - 3, (r + 1) * cellHeight - 3,
                            wallPaint);
                    if (o2.lightbulbs >= 0) {
                        textPaint.setColor(
                                o2.state == WallObject.WallState.Complete ? Color.GREEN :
                                o2.state == WallObject.WallState.Error ? Color.RED :
                                Color.BLACK
                        );
                        String text = String.valueOf(o2.lightbulbs);
                        textPaint.setTextSize(cellHeight);
                        drawTextCentered(text, c * cellWidth + 1, r * cellHeight + 1, canvas);
                    }
                } else if (o instanceof LightbulbObject) {
                    LightbulbObject o2 = (LightbulbObject) o;
                } else if (o instanceof MarkerObject) {
                    canvas.drawArc(c * cellWidth + cellWidth / 2 - 3, r * cellHeight + cellHeight / 2 - 3,
                            c * cellWidth + cellWidth / 2 + 5, r * cellHeight + cellHeight / 2 + 5,
                            0, 360, true, wallPaint);
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            Integer markerOption = doc().gameProgress().markerOption;
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            game().switchObject(new Position(row, col), Game.MarkerOptions.values()[markerOption]);
        }

        return true;
    }

}
