package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGameMove

class NoughtsAndCrossesGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as NoughtsAndCrossesGameActivity
    private fun game() = activity().game
    private val rows get() = if (isInEditMode) 5 else game().rows
    private val cols get() = if (isInEditMode) 5 else game().cols
    override fun rowsInView() = rows
    override fun colsInView() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val hintPaint = Paint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
        hintPaint.style = Paint.Style.FILL
        hintPaint.strokeWidth = 5f
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val ch = game().getObject(p)
                val b = game().noughts.contains(p)
                if (!b && ch == ' ') continue
                if (b)
                    canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, markerPaint)
                if (ch != ' ') {
                    val text = ch.toString()
                    val s: HintState = game().getPosState(p)
                    textPaint.color = if (game()[r, c] == ch) Color.GRAY else if (s == HintState.Normal) Color.WHITE else if (s == HintState.Complete) Color.GREEN else Color.RED
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
        if (isInEditMode) return
        for (r in 0 until rows) {
            val s = game().getRowState(r)
            if (s == HintState.Normal) continue
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
            val c = cols - 1
            canvas.drawArc(cwc(c + 1) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc(c + 1) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, hintPaint)
        }
        for (c in 0 until cols) {
            val s = game().getColState(c)
            if (s == HintState.Normal) continue
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
            val r = rows - 1
            canvas.drawArc(cwc2(c) - 20.toFloat(), chr(r + 1) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr(r + 1) + 20.toFloat(), 0f, 360f, true, hintPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = NoughtsAndCrossesGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}