package com.zwstudio.logicpuzzlesandroid.puzzles.masyu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import kotlin.math.abs

class MasyuGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as MasyuGameActivity
    private fun game() = activity().game
    private val rows get() = if (isInEditMode) 5 else game().rows
    private val cols get() = if (isInEditMode) 5 else game().cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val pearlBlackPaint = Paint()
    private val pearlWhitePaint = Paint()
    private val textPaint = TextPaint()
    private var pLastDown: Position? = null
    private var pLastMove: Position? = null

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.GREEN
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        pearlBlackPaint.color = Color.WHITE
        pearlBlackPaint.style = Paint.Style.STROKE
        pearlBlackPaint.strokeWidth = 5f
        pearlWhitePaint.color = Color.WHITE
        pearlWhitePaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val ch = game()[r, c]
                if (ch != ' ')
                    canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true,
                    if (ch == 'B') pearlBlackPaint else pearlWhitePaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val dirs = intArrayOf(1, 2)
                for (dir in dirs) {
                    val b: Boolean = game().getObject(r, c)[dir]
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
        if (col >= cols || row >= rows) return true
        val p = Position(row, col)
        fun f() = activity().app.soundManager.playSoundTap()
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    pLastMove = p
                    pLastDown = pLastMove
                }
                f()
            }
            MotionEvent.ACTION_MOVE -> if (p != pLastMove) {
                val n = MasyuGame.offset.indexOfFirst { it == p - pLastMove!! }
                if (n != -1) {
                    val move = MasyuGameMove(pLastMove!!, n)
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
                    val move = MasyuGameMove(Position(row, col), if (-dy2 <= dx && dx <= dy2) if (dy > 0) 2 else 0 else if (-dx2 <= dy && dy <= dx2) if (dx > 0) 1 else 3 else 0)
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