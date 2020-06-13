package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class KakuroGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as KakuroGameActivity
    private fun game() = activity().game
    private val rows get() = if (isInEditMode) 5 else game().rows
    private val cols get() = if (isInEditMode) 5 else game().cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val linePaint = Paint()
    private val hintPaint = TextPaint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.GRAY
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        linePaint.color = Color.BLACK
        hintPaint.isAntiAlias = true
        hintPaint.style = Paint.Style.FILL
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val n = game().getObject(Position(r, c))
                if (n == null) {
                    canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), linePaint)
                } else if (n != 0) {
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
        if (isInEditMode) return
        for ((p, n) in game().pos2horzHint) {
            val s = game().getHorzState(p)
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.BLACK
            val text = n.toString()
            drawTextCentered(text, cwc2(p.col), chr(p.row), cellWidth / 2, cellHeight / 2, canvas, hintPaint)
        }
        for ((p, n) in game().pos2vertHint) {
            val s = game().getVertState(p)
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.BLACK
            val text = n.toString()
            drawTextCentered(text, cwc(p.col), chr2(p.row), cellWidth / 2, cellHeight / 2, canvas, hintPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = KakuroGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}