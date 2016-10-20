package com.zwstudio.logicgamesandroid.lightup.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zwstudio.logicgamesandroid.lightup.data.LightUpGameProgress;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpEmptyObject;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGame;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGameMove;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpLightbulbObject;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpLightbulbState;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpMarkerObject;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpMarkerOptions;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpObject;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpWallObject;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class LightUpGameView extends View {

    private LightUpGameActivity activity() {return (LightUpGameActivity)getContext();}
    private LightUpGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private int cellWidth, cellHeight;
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint lightPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Drawable dLightbulb;

    public LightUpGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public LightUpGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LightUpGameView(Context context, AttributeSet attrs, int defStyle) {
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
        try {
            InputStream is = getContext().getApplicationContext().getAssets().open("lightbulb.png");
            Bitmap bmpLightbulb = BitmapFactory.decodeStream(is);
            dLightbulb = new BitmapDrawable(bmpLightbulb);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                LightUpObject o = game().getObject(r, c);
                if (o.lightness > 0)
                    canvas.drawRect(c * cellWidth + 5, r * cellHeight + 5,
                            (c + 1) * cellWidth - 3, (r + 1) * cellHeight - 3,
                            lightPaint);
                if (o instanceof LightUpWallObject) {
                    LightUpWallObject o2 = (LightUpWallObject) o;
                    canvas.drawRect(c * cellWidth + 5, r * cellHeight + 5,
                            (c + 1) * cellWidth - 3, (r + 1) * cellHeight - 3,
                            wallPaint);
                    int n = game().pos2hint.get(new Position(r, c));
                    if (n >= 0) {
                        textPaint.setColor(
                                o2.state == LogicGamesHintState.Complete ? Color.GREEN :
                                o2.state == LogicGamesHintState.Error ? Color.RED :
                                Color.BLACK
                        );
                        String text = String.valueOf(n);
                        textPaint.setTextSize(cellHeight);
                        drawTextCentered(text, c * cellWidth + 1, r * cellHeight + 1, canvas);
                    }
                } else if (o instanceof LightUpLightbulbObject) {
                    LightUpLightbulbObject o2 = (LightUpLightbulbObject) o;
                    dLightbulb.setBounds(c * cellWidth + 1, r * cellHeight + 1,
                            (c + 1) * cellWidth + 1, (r + 1) * cellHeight + 1);
                    int alpaha = o2.state == LightUpLightbulbState.Error ? 50 : 0;
                    dLightbulb.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dLightbulb.draw(canvas);
                } else if (o instanceof LightUpMarkerObject) {
                    canvas.drawArc(c * cellWidth + cellWidth / 2 - 19, r * cellHeight + cellHeight / 2 - 19,
                            c * cellWidth + cellWidth / 2 + 21, r * cellHeight + cellHeight / 2 + 21,
                            0, 360, true, wallPaint);
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            LightUpGameProgress rec = activity().doc().gameProgress();
            LightUpGameMove move = new LightUpGameMove();
            move.p = new Position(row, col);
            move.obj = new LightUpEmptyObject();
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move, LightUpMarkerOptions.values()[rec.markerOption],
                    rec.normalLightbulbsOnly))
                activity().app().playSoundTap();
        }
        return true;
    }

}
