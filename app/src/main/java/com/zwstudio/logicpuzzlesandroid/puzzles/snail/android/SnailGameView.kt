package com.zwstudio.logicpuzzlesandroid.puzzles.snail.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove

class SnailGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as SnailGameActivity
    private fun game() = activity().game
    private val rows get() = if (isInEditMode) 5 else game().rows
    private val cols get() = if (isInEditMode) 5 else game().cols
    override fun rowsInView() = rows + 1
    override fun colsInView() = cols + 1

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        markerPaint.color = Color.GREEN
        markerPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 1 until rows - 1)
            for (c in 1 until cols - 1)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        for (i in 1 until game().snailPathLine.size) {
            val p1 = game().snailPathLine[i - 1]
            val p2 = game().snailPathLine[i]
            canvas.drawLine(cwc(p1.col).toFloat(), chr(p1.row).toFloat(), cwc(p2.col).toFloat(), chr(p2.row).toFloat(), linePaint)
        }
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                if (game().getPositionState(r, c) == HintState.Complete)
                    canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, markerPaint)
                val ch = game().getObject(r, c)
                if (ch == ' ') continue
                textPaint.color = if (game()[r, c] == ch) Color.GRAY else Color.WHITE
                val text = ch.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        textPaint.color = Color.RED
        for (r in 0 until rows)
            if (game().getRowState(r) == HintState.Error) drawTextCentered("123", cwc(cols), chr(r), canvas, textPaint)
        for (c in 0 until cols)
            if (game().getColState(c) == HintState.Error) drawTextCentered("123", cwc(c), chr(rows), canvas, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = SnailGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}