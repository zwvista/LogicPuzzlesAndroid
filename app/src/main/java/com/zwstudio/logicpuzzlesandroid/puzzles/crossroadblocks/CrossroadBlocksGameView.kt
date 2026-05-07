package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import kotlin.math.abs
import kotlin.math.min

class CrossroadBlocksGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as CrossroadBlocksGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val blockPaint = Paint()
    private val trianglePaint = Paint()
    private val textPaint = TextPaint()
    private var pLastDown: Position? = null
    private var pLastMove: Position? = null

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.GREEN
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        blockPaint.strokeWidth = 5f
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
    }

    fun drawTriangle(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, direction: Int, paint: Paint) {
        val path = Path()
        val cx = (left + right) / 2
        val cy = (top + bottom) / 2
        val w = right - left
        val h = bottom - top
        val side = min(w, h)

        when (direction) {
            0 -> { // Up
                path.moveTo(cx, cy - side/2)
                path.lineTo(cx - w/2, cy + side/2)
                path.lineTo(cx + w/2, cy + side/2)
            }
            1 -> { // Right
                path.moveTo(cx + w/2, cy)
                path.lineTo(cx - w/2, cy - side/2)
                path.lineTo(cx - w/2, cy + side/2)
            }
            2 -> { // Down
                path.moveTo(cx, cy + side/2)
                path.lineTo(cx - w/2, cy - side/2)
                path.lineTo(cx + w/2, cy - side/2)
            }
            3 -> { // Left
                path.moveTo(cx - w/2, cy)
                path.lineTo(cx + w/2, cy - side/2)
                path.lineTo(cx + w/2, cy + side/2)
            }
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val hint = game.pos2hint[p] ?: continue
                val (isBlack, n, dir) = hint
                blockPaint.color = if (isBlack) Color.LTGRAY else Color.WHITE
                blockPaint.style = if (isBlack) Paint.Style.STROKE else Paint.Style.FILL_AND_STROKE
                canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), blockPaint)
                if (n == CrossroadBlocksGame.PUZ_UNKNOWN) continue
                val s = game.pos2State(p)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (isBlack) Color.WHITE else Color.BLACK
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r) + cellHeight / 4, cellWidth / 2, cellHeight / 2, canvas, textPaint)
                trianglePaint.color = if (isBlack) Color.WHITE else Color.BLACK
                drawTriangle(canvas, cwc2(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), dir, trianglePaint)
            }
        if (isInEditMode) return
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val dirs = intArrayOf(1, 2)
                for (dir in dirs) {
                    val b = game.getObject(r, c)[dir]
                    if (!b) continue
                    if (dir == 1)
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c + 1).toFloat(), chr2(r).toFloat(), linePaint)
                    else
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr2(r + 1).toFloat(), linePaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (game.isSolved) return true
        val col = (event.x / cellWidth).toInt()
        val row = (event.y / cellHeight).toInt()
        if (col >= cols || row >= rows) return true
        val p = Position(row, col)
        fun f() = soundManager.playSoundTap()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    pLastMove = p
                    pLastDown = pLastMove
                }
                f()
            }
            MotionEvent.ACTION_MOVE -> if (p != pLastMove) {
                val n = CrossroadBlocksGame.offset.indexOfFirst { it == p - pLastMove!! }
                if (n != -1) {
                    val move = CrossroadBlocksGameMove(pLastMove!!, n)
                    if (game.setObject(move)) f()
                }
                pLastMove = p
            }
            MotionEvent.ACTION_UP -> {
                if (p == pLastDown) {
                    val dx = event.x - (col + 0.5) * cellWidth
                    val dy = event.y - (row + 0.5) * cellHeight
                    val dx2 = abs(dx)
                    val dy2 = abs(dy)
                    val move = CrossroadBlocksGameMove(Position(row, col), if (dx2 <= dy2) if (dy > 0) 2 else 0 else if (dx > 0) 1 else 3)
                    game.setObject(move)
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