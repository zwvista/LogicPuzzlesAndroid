package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGame
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameMove
import kotlin.math.abs

class NumberPathGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as NumberPathGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()
    private var pLastDown: Position? = null
    private var pLastMove: Position? = null

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val n = game()[r, c]
                textPaint.color = Color.WHITE
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val dirs = intArrayOf(1, 2)
                for (dir in dirs) {
                    val b = game().getObject(r, c)[dir]
                    if (!b) continue
                    if (dir == 1)
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c + 1).toFloat(), chr2(r).toFloat(), linePaint)
                    else
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr2(r + 1).toFloat(), linePaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (game().isSolved) return true
        val col = (event.x / cellWidth).toInt()
        val row = (event.y / cellHeight).toInt()
        if (col >= cols() || row >= rows()) return true
        val p = Position(row, col)
        fun f() = activity().app.soundManager.playSoundTap()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    pLastMove = p
                    pLastDown = pLastMove
                }
                f()
            }
            MotionEvent.ACTION_MOVE -> if (pLastMove != null && p != pLastMove) {
                val n = NumberPathGame.offset.indexOfFirst { it == p.subtract(pLastMove!!) }
                if (n != -1) {
                    val move = NumberPathGameMove(pLastMove!!, n)
                    if (game().setObject(move)) f()
                }
                pLastMove = p
            }
            MotionEvent.ACTION_UP -> {
                if (p == pLastDown) {
                    val dx: Double = event.x - (col + 0.5) * cellWidth
                    val dy: Double = event.y - (row + 0.5) * cellHeight
                    val dx2 = abs(dx)
                    val dy2 = abs(dy)
                    val move = NumberPathGameMove(Position(row, col), if (-dy2 <= dx && dx <= dy2) if (dy > 0) 2 else 0 else if (-dx2 <= dy && dy <= dx2) if (dx > 0) 1 else 3 else 0)
                    game().setObject(move)
                }
                run {
                    pLastMove = null
                    pLastDown = pLastMove
                }
            }
        }
        return true
    }
}