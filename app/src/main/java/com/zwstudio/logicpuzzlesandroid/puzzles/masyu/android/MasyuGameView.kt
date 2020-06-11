package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove
import fj.F
import fj.data.Stream
import fj.function.Effect0

class MasyuGameView : CellsGameView {
    private fun activity() = context as MasyuGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val pearlBlackPaint = Paint()
    private val pearlWhitePaint = Paint()
    private val textPaint: TextPaint = TextPaint()
    private var pLastDown: Position? = null
    private var pLastMove: Position? = null

    constructor(context: Context?) : super(context) { init(null, 0) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs, 0) }
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init(attrs, defStyle) }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
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
        textPaint.setAntiAlias(true)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode) continue
            val ch: Char = game().get(r, c)
            if (ch != ' ') canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true,
                if (ch == 'B') pearlBlackPaint else pearlWhitePaint)
        }
        if (isInEditMode) return
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val dirs = intArrayOf(1, 2)
            for (dir in dirs) {
                val b: Boolean = game().getObject(r, c).get(dir)
                if (!b) continue
                if (dir == 1) canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c + 1).toFloat(), chr2(r).toFloat(), linePaint) else canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr2(r + 1).toFloat(), linePaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (game().isSolved()) return true
        val col = (event.getX() / cellWidth).toInt()
        val row = (event.getY() / cellHeight).toInt()
        if (col >= cols() || row >= rows()) return true
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
                val n = Stream.range(0, MasyuGame.offset.size.toLong())
                    .filter(F<Int, Boolean> { i: Int? -> MasyuGame.offset.get(i!!) == p.subtract(pLastMove) })
                    .orHead(F0<Int> { -1 })
                if (n != -1) {
                    val move = MasyuGameMove(pLastMove!!, n)
                    if (game().setObject(move)) f()
                }
                pLastMove = p
            }
            MotionEvent.ACTION_UP -> {
                if (p == pLastDown) {
                    val dx: Double = event.getX() - (col + 0.5) * cellWidth
                    val dy: Double = event.getY() - (row + 0.5) * cellHeight
                    val dx2 = Math.abs(dx)
                    val dy2 = Math.abs(dy)
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