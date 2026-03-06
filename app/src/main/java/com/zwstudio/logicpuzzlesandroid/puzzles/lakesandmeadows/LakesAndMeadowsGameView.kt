package com.zwstudio.logicpuzzlesandroid.puzzles.lakesandmeadows

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class LakesAndMeadowsGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as LakesAndMeadowsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows - 1
    private val cols get() = if (isInEditMode) 5 else game.cols - 1
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val line1Paint = Paint()
    private val line2Paint = Paint()
    private val markerPaint = Paint()
    private val wallPaint = Paint()
    private val lakePaint1 = Paint()
    private val lakePaint2 = Paint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        line1Paint.color = Color.WHITE
        line1Paint.style = Paint.Style.STROKE
        line1Paint.strokeWidth = 20f
        line2Paint.color = Color.YELLOW
        line2Paint.style = Paint.Style.STROKE
        line2Paint.strokeWidth = 20f
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        wallPaint.color = Color.LTGRAY
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        lakePaint1.style = Paint.Style.STROKE
        lakePaint1.strokeWidth = 5f
        lakePaint2.style = Paint.Style.FILL
        lakePaint2.color = Color.GRAY
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game[p]
                if (o != LakesAndMeadowsObject.Lake) continue
                val s = game.pos2state(p)
                lakePaint1.color = when (s) {
                    HintState.Complete -> Color.GREEN
                    HintState.Error -> Color.RED
                    else -> Color.WHITE
                }
                canvas.drawArc((cwc2(c) - cellWidth / 3).toFloat(), (chr2(r) - cellHeight / 3).toFloat(), (cwc2(c) + cellWidth / 3).toFloat(), (chr2(r) + cellHeight / 3).toFloat(), 0f, 360f, true, lakePaint1)
                canvas.drawArc((cwc2(c) - cellWidth / 3).toFloat(), (chr2(r) - cellHeight / 3).toFloat(), (cwc2(c) + cellWidth / 3).toFloat(), (chr2(r) + cellHeight / 3).toFloat(), 0f, 360f, true, lakePaint2)
            }
        if (isInEditMode) return
        val markerOffset = 20
        for (r in 0 until rows + 1)
            for (c in 0 until cols + 1) {
                when (game.getObject(r, c, 1)) {
                    GridLineObject.Line -> canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(),
                            if (game.dots[r, c, 1] == GridLineObject.Line) line1Paint else line2Paint)
                    GridLineObject.Marker -> {
                        canvas.drawLine((cwc2(c) - markerOffset).toFloat(), (chr(r) - markerOffset).toFloat(), (cwc2(c) + markerOffset).toFloat(), (chr(r) + markerOffset).toFloat(), markerPaint)
                        canvas.drawLine((cwc2(c) - markerOffset).toFloat(), (chr(r) + markerOffset).toFloat(), (cwc2(c) + markerOffset).toFloat(), (chr(r) - markerOffset).toFloat(), markerPaint)
                    }
                    else -> {}
                }
                when (game.getObject(r, c, 2)) {
                    GridLineObject.Line -> canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(),
                            if (game.dots[r, c, 2] == GridLineObject.Line) line1Paint else line2Paint)
                    GridLineObject.Marker -> {
                        canvas.drawLine((cwc(c) - markerOffset).toFloat(), (chr2(r) - markerOffset).toFloat(), (cwc(c) + markerOffset).toFloat(), (chr2(r) + markerOffset).toFloat(), markerPaint)
                        canvas.drawLine((cwc(c) - markerOffset).toFloat(), (chr2(r) + markerOffset).toFloat(), (cwc(c) + markerOffset).toFloat(), (chr2(r) - markerOffset).toFloat(), markerPaint)
                    }
                    else -> {}
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val offset = 30
            val col = ((event.x + offset) / cellWidth).toInt()
            val row = ((event.y + offset) / cellHeight).toInt()
            val xOffset = event.x.toInt() - col * cellWidth - 1
            val yOffset = event.y.toInt() - row * cellHeight - 1
            if (!(xOffset in -offset..offset || yOffset in -offset..offset)) return true
            val move = LakesAndMeadowsGameMove(
                    p = Position(row, col),
                    obj = GridLineObject.Empty,
                    dir = if (yOffset in -offset..offset) 1 else 2
            )
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
