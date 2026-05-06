package com.zwstudio.logicpuzzlesandroid.puzzles.gardentunnels

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import kotlin.math.abs

class GardenTunnelsGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as GardenTunnelsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val line1Paint = Paint()
    private val line2Paint = Paint()
    private val textPaint = TextPaint()
    private var pLastDown: Position? = null
    private var pLastMove: Position? = null

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        line1Paint.color = Color.WHITE
        line1Paint.style = Paint.Style.STROKE
        line1Paint.strokeWidth = 20f
        line2Paint.color = Color.YELLOW
        line2Paint.style = Paint.Style.STROKE
        line2Paint.strokeWidth = 20f
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val n = game.pos2hint[p] ?: continue
                val s = game.pos2state(p)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        for (r in 0..<rows + 1)
            for (c in 0..<cols + 1) {
                if (game.dots[r, c, 1] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), line1Paint)
                if (game.dots[r, c, 2] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), line1Paint)
            }
        if (isInEditMode) return
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val dirs = intArrayOf(1, 2)
                for (dir in dirs) {
                    val b = game.getObject(r, c)[dir]
                    if (!b) continue
                    if (dir == 1)
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c + 1).toFloat(), chr2(r).toFloat(), line2Paint)
                    else
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr2(r + 1).toFloat(), line2Paint)
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
                val n = GardenTunnelsGame.offset.indexOfFirst { it == p - pLastMove!! }
                if (n != -1) {
                    val move = GardenTunnelsGameMove(pLastMove!!, n)
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
                    val move = GardenTunnelsGameMove(Position(row, col), if (dx2 <= dy2) if (dy > 0) 2 else 0 else if (dx > 0) 1 else 3)
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