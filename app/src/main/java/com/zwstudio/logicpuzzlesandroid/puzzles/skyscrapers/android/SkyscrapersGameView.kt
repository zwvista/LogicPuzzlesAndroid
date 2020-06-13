package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameMove

class SkyscrapersGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as SkyscrapersGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 1 until rows() - 1)
            for (c in 1 until cols() - 1)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val n = game().getObject(r, c)
                if (n == 0) continue
                val s: HintState = game().getState(r, c)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (!game().isValid(r, c)) Color.GRAY else Color.WHITE
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            val move = SkyscrapersGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}