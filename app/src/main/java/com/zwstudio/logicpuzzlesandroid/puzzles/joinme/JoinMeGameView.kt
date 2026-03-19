package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import kotlin.math.abs

class JoinMeGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as JoinMeGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows + 1
    override val colsInView get() = cols + 1

    private val gridPaint = Paint()
    private val line1Paint = Paint()
    private val line2Paint = Paint()
    private val textPaint = TextPaint()
    private val dWater: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        line1Paint.color = Color.WHITE
        line1Paint.style = Paint.Style.STROKE
        line1Paint.strokeWidth = 20f
        line2Paint.color = Color.YELLOW
        line2Paint.style = Paint.Style.STROKE
        line2Paint.strokeWidth = 20f
        textPaint.isAntiAlias = true
        dWater = fromImageToDrawable("images/sea.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val ch = game.getObject(p)
            }
        for (r in 0 until rows + 1)
            for (c in 0 until cols + 1) {
                if (game.dots[r, c, 1] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), line1Paint)
                if (game.dots[r, c, 2] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), line1Paint)
            }
        if (isInEditMode) return
        for (r in 0 until rows) {
            val s = game.row2state(r)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val n = game.row2hint[r]
            val text = n.toString()
            drawTextCentered(text, cwc(cols), chr(r), canvas, textPaint)
        }
        for (c in 0 until cols) {
            val s = game.col2state(c)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val n = game.col2hint[c]
            val text = n.toString()
            drawTextCentered(text, cwc(c), chr(rows), canvas, textPaint)
        }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val dirs = intArrayOf(1, 2)
                for (dir in dirs) {
                    val b = game.getObject(r, c)[dir]
                    if (!b) continue
                    if (dir == 1)
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c + 1).toFloat(), chr2(r).toFloat(), line2Paint)
                    else
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr2(r + 1).toFloat(), line2Paint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val dx = event.x - (col + 0.5) * cellWidth
            val dy = event.y - (row + 0.5) * cellHeight
            val dx2 = abs(dx)
            val dy2 = abs(dy)
            val move = JoinMeGameMove(Position(row, col), if (dx2 <= dy2) if (dy > 0) 2 else 0 else if (dx > 0) 1 else 3)
            game.setObject(move)
        }
        return true
    }
}