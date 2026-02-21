package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class MirrorsGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as MirrorsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val blockPaint = Paint()
    private val spotPaint1 = Paint()
    private val spotPaint2 = Paint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.GREEN
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        blockPaint.color = Color.LTGRAY
        blockPaint.style = Paint.Style.FILL_AND_STROKE
        spotPaint1.style = Paint.Style.STROKE
        spotPaint1.strokeWidth = 5f
        spotPaint2.style = Paint.Style.FILL
        spotPaint2.color = Color.GREEN
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                when (game[r, c]) {
                    MirrorsObject.Block ->
                        canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), blockPaint)
                    MirrorsObject.Spot -> {
                        canvas.drawArc((cwc2(c) - cellWidth / 2).toFloat(), (chr2(r) - cellHeight / 2).toFloat(), (cwc2(c) + cellWidth / 2).toFloat(), (chr2(r) + cellHeight / 2).toFloat(), 0f, 360f, true, spotPaint1)
                        canvas.drawArc((cwc2(c) - cellWidth / 2).toFloat(), (chr2(r) - cellHeight / 2).toFloat(), (cwc2(c) + cellWidth / 2).toFloat(), (chr2(r) + cellHeight / 2).toFloat(), 0f, 360f, true, spotPaint2)
                    }
                    else -> {}
                }
            }
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                for (dir in 0 until 4) {
                    val b = game.pos2dirs()[p]!!.contains(dir)
                    if (!b) continue
                    linePaint.color = if (game[p] == MirrorsObject.Empty) Color.GREEN else Color.WHITE
                    when (dir) {
                        0 ->
                            canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr(r).toFloat(), linePaint)
                        1 ->
                            canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc(c + 1).toFloat(), chr2(r).toFloat(), linePaint)
                        2 ->
                            canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr(r + 1).toFloat(), linePaint)
                        3 ->
                            canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc(c).toFloat(), chr2(r).toFloat(), linePaint)
                    }
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = MirrorsGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}