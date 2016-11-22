package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android;

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

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpLightbulbObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpLightbulbState;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpMarkerOptions;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpWallObject;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class LightenUpGameView extends CellsGameView {

    private LightenUpGameActivity activity() {return (LightenUpGameActivity)getContext();}
    private LightenUpGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint lightPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Drawable dLightbulb;

    public LightenUpGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public LightenUpGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LightenUpGameView(Context context, AttributeSet attrs, int defStyle) {
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
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                LightenUpObject o = game().getObject(r, c);
                if (o.lightness > 0)
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, lightPaint);
                if (o instanceof LightenUpWallObject) {
                    LightenUpWallObject o2 = (LightenUpWallObject) o;
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                    int n = game().pos2hint.get(new Position(r, c));
                    if (n >= 0) {
                        textPaint.setColor(
                                o2.state == HintState.Complete ? Color.GREEN :
                                o2.state == HintState.Error ? Color.RED :
                                Color.BLACK
                        );
                        String text = String.valueOf(n);
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                    }
                } else if (o instanceof LightenUpLightbulbObject) {
                    LightenUpLightbulbObject o2 = (LightenUpLightbulbObject) o;
                    dLightbulb.setBounds(c * cellWidth + 1, r * cellHeight + 1,
                            (c + 1) * cellWidth + 1, (r + 1) * cellHeight + 1);
                    int alpaha = o2.state == LightenUpLightbulbState.Error ? 50 : 0;
                    dLightbulb.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    dLightbulb.draw(canvas);
                } else if (o instanceof LightenUpMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            LightenUpGameMove move = new LightenUpGameMove() {{
                p = new Position(row, col);
                obj = new LightenUpEmptyObject();
            }};
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move, LightenUpMarkerOptions.values()[activity().doc().getMarkerOption()],
                    activity().doc().isNormalLightbulbsOnly()))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
