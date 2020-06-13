package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FutoshikiGameView(context: Context) : CellsGameView(context) {
    private val activity get() = context as FutoshikiGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()
    private val hintPaint = Paint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        textPaint.isAntiAlias = true
        hintPaint.style = Paint.Style.FILL
        hintPaint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        run {
            var r = 0
            while (r < rows) {
                var c = 0
                while (c < cols) {
                    canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                    if (isInEditMode) {
                        c += 2
                        continue
                    }
                    val ch = game.getObject(r, c)
                    if (ch == ' ') {
                        c += 2
                        continue
                    }
                    val text = ch.toString()
                    textPaint.color = if (game[r, c] == ch) Color.GRAY else Color.WHITE
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    c += 2
                }
                r += 2
            }
        }
        if (isInEditMode) return
        var r = 0
        while (r < rows) {
            val s = game.getRowState(r)
            if (s == HintState.Normal) {
                r += 2
                continue
            }
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
            val c = cols - 1
            canvas.drawArc(cwc(c + 1) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc(c + 1) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, hintPaint)
            r += 2
        }
        var c = 0
        while (c < cols) {
            val s = game.getColState(c)
            if (s == HintState.Normal) {
                c += 2
                continue
            }
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
            val r = rows - 1
            canvas.drawArc(cwc2(c) - 20.toFloat(), chr(r + 1) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr(r + 1) + 20.toFloat(), 0f, 360f, true, hintPaint)
            c += 2
        }
        for ((p, value) in game.pos2hint) {
            val r = p.row
            val c = p.col
            val text = value.toString()
            val s = game.getPosState(p)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = FutoshikiGameMove(Position(row, col))
            if (game.switchObject(move))
                activity.app.soundManager.playSoundTap()
        }
        return true
    }
}