package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ScissorsGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ScissorsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()
    private val mathPaint1 = Paint()
    private val mathPaint2 = Paint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.GREEN
        linePaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                fun addSlash(p1: Position, p2: Position) {
                    val (r1, c1) = p1
                    val (r2, c2) = p2
                    canvas.drawLine(cwc(c1).toFloat(), chr(r1).toFloat(), cwc(c2).toFloat(), chr(r2).toFloat(), linePaint)
                }
                when (val o = game.getObject(p)) {
                    ScissorsGame.PUZ_BACK_SLASH -> addSlash(p, p + ScissorsGame.offset2[3])
                    ScissorsGame.PUZ_FRONT_SLASH -> addSlash(p + ScissorsGame.offset2[1], p + ScissorsGame.offset2[2])
                    ' ' -> {}
                    else -> {
                        val text = o.toString()
                        val s = game.pos2state(p)
                        textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
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
            val move = ScissorsGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
