package com.zwstudio.logicpuzzlesandroid.puzzles.underground

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class UndergroundGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as UndergroundGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val markerPaint = Paint()
    private val dUp: Drawable
    private val dRight: Drawable
    private val dDown: Drawable
    private val dLeft: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.STROKE
        dUp = fromImageToDrawable("images/stairs_up.png")
        dRight = fromImageToDrawable("images/stairs_right.png")
        dDown = fromImageToDrawable("images/stairs_down.png")
        dLeft = fromImageToDrawable("images/stairs_left.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)

                fun f(dObject: Drawable) {
                    dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    val alpha = if (game.pos2State(p) == AllowedObjectState.Error) 50 else 0
                    dObject.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                    dObject.draw(canvas)
                }

                when (val o = game.getObject(p)) {
                    UndergroundObject.Marker ->
                        canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, markerPaint)
                    UndergroundObject.Up -> f(dUp)
                    UndergroundObject.Right -> f(dRight)
                    UndergroundObject.Down -> f(dDown)
                    UndergroundObject.Left -> f(dLeft)
                    else -> {}
                }
            }
        for (r in 0 until rows + 1)
            for (c in 0 until cols + 1) {
                if (game.dots[r, c, 1] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
                if (game.dots[r, c, 2] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = UndergroundGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}