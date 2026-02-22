package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class SnakeMazeGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as SnakeMazeGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint = TextPaint()
    private val dUp: Drawable
    private val dRight: Drawable
    private val dDown: Drawable
    private val dLeft: Drawable
    private val dSnake: Drawable

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.STROKE
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        dUp = fromImageToDrawable("images/arrow_cyan_up.png")
        dRight = fromImageToDrawable("images/arrow_cyan_right.png")
        dDown = fromImageToDrawable("images/arrow_cyan_down.png")
        dLeft = fromImageToDrawable("images/arrow_cyan_left.png")
        dSnake = fromImageToDrawable("images/scales.png")
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    SnakeMazeObject.Marker ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
                    SnakeMazeObject.Forbidden ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
                    SnakeMazeObject.Hint -> {
                        val hint = game.pos2hint[p]!!
                        val s = game.pos2StateHint(p)
                        textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (!game.isValid(r, c)) Color.GRAY else Color.WHITE
                        val text = hint.num.toString()
                        val dObject = when (hint.dir) {
                            0 -> dUp
                            1 -> dRight
                            2 -> dDown
                            3 -> dLeft
                            else -> continue
                        }
                        when (hint.dir) {
                            0 ->
                                drawTextCentered(text, cwc(c) + cellWidth / 4, chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            1 ->
                                drawTextCentered(text, cwc(c), chr(r) + cellHeight / 4, cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            2 ->
                                drawTextCentered(text, cwc(c) + cellWidth / 4, chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            3 ->
                                drawTextCentered(text, cwc2(c), chr(r) + cellHeight / 4, cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        }
                        dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dObject.draw(canvas)
                    }
                    else -> if (o.isSnake) {
                        if (game.snakes().any { it.contains(p) }) {
                            dSnake.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                            val s = game.pos2StateAllowed(p)
                            val alpha = if (s == AllowedObjectState.Error) 50 else 0
                            dSnake.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                            dSnake.draw(canvas)
                        }
                        textPaint.color = Color.WHITE
                        val text = o.value.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = SnakeMazeGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
