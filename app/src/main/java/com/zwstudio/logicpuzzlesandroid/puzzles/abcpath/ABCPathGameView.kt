package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath

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

class ABCPathGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ABCPathGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 1 until rows - 1)
            for (c in 1 until cols - 1) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val ch = game.getObject(r, c)
                if (ch == ' ') continue
                val s = game.getState(r, c)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (game[r, c] == ' ') Color.GRAY else Color.WHITE
                val text = ch.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            val move = ABCPathGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
