package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove

class FutoshikiGameView : CellsGameView {
    private fun activity() = context as FutoshikiGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode) 5 else game().rows()

    private fun cols() = if (isInEditMode) 5 else game().cols()

    override fun rowsInView() = rows()

    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()
    private val hintPaint = Paint()

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init(attrs: AttributeSet?, defStyle: Int) {
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
            while (r < rows()) {
                var c = 0
                while (c < cols()) {
                    canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                    if (isInEditMode) {
                        c += 2
                        continue
                    }
                    val ch = game().getObject(r, c)
                    if (ch == ' ') {
                        c += 2
                        continue
                    }
                    val text = ch.toString()
                    textPaint.color = if (game()[r, c] == ch) Color.GRAY else Color.WHITE
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    c += 2
                }
                r += 2
            }
        }
        if (isInEditMode) return
        var r = 0
        while (r < rows()) {
            val s = game().getRowState(r)
            if (s == HintState.Normal) {
                r += 2
                continue
            }
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
            val c = cols() - 1
            canvas.drawArc(cwc(c + 1) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc(c + 1) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, hintPaint)
            r += 2
        }
        var c = 0
        while (c < cols()) {
            val s = game().getColState(c)
            if (s == HintState.Normal) {
                c += 2
                continue
            }
            hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
            val r = rows() - 1
            canvas.drawArc(cwc2(c) - 20.toFloat(), chr(r + 1) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr(r + 1) + 20.toFloat(), 0f, 360f, true, hintPaint)
            c += 2
        }
        for ((p, value) in game().pos2hint) {
            val r = p.row
            val c = p.col
            val text = value.toString()
            val s = game().getPosState(p)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move = FutoshikiGameMove()
                init {
                    p = Position(row, col)
                    obj = ' '
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}