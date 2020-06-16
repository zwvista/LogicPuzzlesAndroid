package com.zwstudio.logicpuzzlesandroid.puzzles.hitori

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HitoriGameView(context: Context) : CellsGameView(context) {
    private val activity get() = context as HitoriGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows + 1
    override val colsInView get() = cols + 1

    private val gridPaint = Paint()
    private val darkenPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        darkenPaint.color = Color.LTGRAY
        darkenPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val o = game.getObject(r, c)
                when (o) {
                    HitoriObject.Darken ->
                        canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), darkenPaint)
                    HitoriObject.Marker ->
                        canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, markerPaint)
                    else -> {}
                }
                textPaint.color = Color.WHITE
                val text = game[r, c].toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        if (isInEditMode) return
        textPaint.color = Color.RED
        for (r in 0 until rows) {
            val text = game.getRowHint(r)
            drawTextCentered(text, cwc(cols), chr(r), canvas, textPaint)
        }
        for (c in 0 until cols) {
            val text = game.getColHint(c)
            drawTextCentered(text, cwc(c), chr(rows), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = HitoriGameMove(Position(row, col))
            if (game.switchObject(move))
                activity.app.soundManager.playSoundTap()
        }
        return true
    }

}
