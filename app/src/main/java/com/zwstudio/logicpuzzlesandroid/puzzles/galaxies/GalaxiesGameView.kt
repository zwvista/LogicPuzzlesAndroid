package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GalaxiesGameView(context: Context) : CellsGameView(context) {
    private val activity get() = context as GalaxiesGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows - 1
    private val cols get() = if (isInEditMode) 5 else game.cols - 1
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val line1Paint = Paint()
    private val line2Paint = Paint()
    private val markerPaint = Paint()
    private val galaxyPaint = Paint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        line1Paint.color = Color.WHITE
        line1Paint.style = Paint.Style.STROKE
        line1Paint.strokeWidth = 20f
        line2Paint.color = Color.YELLOW
        line2Paint.style = Paint.Style.STROKE
        line2Paint.strokeWidth = 20f
        markerPaint.color = Color.YELLOW
        markerPaint.style = Paint.Style.STROKE
        markerPaint.strokeWidth = 5f
        galaxyPaint.style = Paint.Style.FILL_AND_STROKE
        galaxyPaint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        val markerOffset = 20
        for (r in 0 until rows + 1)
            for (c in 0 until cols + 1) {
                val dotObj = game.getObject(r, c)
                when (dotObj[1]) {
                    GridLineObject.Line -> canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(),
                        if (game[r, c][1] == GridLineObject.Line) line1Paint else line2Paint)
                    GridLineObject.Marker -> {
                        canvas.drawLine(cwc2(c) - markerOffset.toFloat(), chr(r) - markerOffset.toFloat(), cwc2(c) + markerOffset.toFloat(), chr(r) + markerOffset.toFloat(), markerPaint)
                        canvas.drawLine(cwc2(c) - markerOffset.toFloat(), chr(r) + markerOffset.toFloat(), cwc2(c) + markerOffset.toFloat(), chr(r) - markerOffset.toFloat(), markerPaint)
                    }
                    else -> {}
                }
                when (dotObj[2]) {
                    GridLineObject.Line -> canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(),
                        if (game[r, c][2] == GridLineObject.Line) line1Paint else line2Paint)
                    GridLineObject.Marker -> {
                        canvas.drawLine(cwc(c) - markerOffset.toFloat(), chr2(r) - markerOffset.toFloat(), cwc(c) + markerOffset.toFloat(), chr2(r) + markerOffset.toFloat(), markerPaint)
                        canvas.drawLine(cwc(c) - markerOffset.toFloat(), chr2(r) + markerOffset.toFloat(), cwc(c) + markerOffset.toFloat(), chr2(r) - markerOffset.toFloat(), markerPaint)
                    }
                    else -> {}
                }
            }
        for (p in game.galaxies) {
            val state = game.pos2State(p)
            galaxyPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
            val r = p.row
            val c = p.col
            val x = cwc2(c / 2) - if (c % 2 == 0) cellWidth / 2 else 0
            val y = chr2(r / 2) - if (r % 2 == 0) cellHeight / 2 else 0
            canvas.drawArc(x - 20.toFloat(), y - 20.toFloat(), x + 20.toFloat(), y + 20.toFloat(), 0f, 360f, true, galaxyPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val offset = 30
            val col = ((event.x + offset) / cellWidth).toInt()
            val row = ((event.y + offset) / cellHeight).toInt()
            val xOffset = event.x.toInt() - col * cellWidth - 1
            val yOffset = event.y.toInt() - row * cellHeight - 1
            if (!(xOffset >= -offset && xOffset <= offset || yOffset >= -offset && yOffset <= offset)) return true
            val move = GalaxiesGameMove(Position(row, col), if (yOffset >= -offset && yOffset <= offset) 1 else 2)
            if (game.switchObject(move))
                activity.app.soundManager.playSoundTap()
        }
        return true
    }
}