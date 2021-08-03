package com.zwstudio.logicpuzzlesandroid.puzzles.domino

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

class DominoGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as DominoGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows - 1
    private val cols get() = if (isInEditMode) 5 else game.cols - 1
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val line1Paint = Paint()
    private val line2Paint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()

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
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val n = game.pos2hint[p]
                if (n != null) {
                    val state = game.pos2State(p)
                    textPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
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
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val offset = 30
            val col = ((event.x + offset) / cellWidth).toInt()
            val row = ((event.y + offset) / cellHeight).toInt()
            val xOffset = event.x.toInt() - col * cellWidth - 1
            val yOffset = event.y.toInt() - row * cellHeight - 1
            if (!(xOffset >= -offset && xOffset <= offset || yOffset >= -offset && yOffset <= offset)) return true
            val move = DominoGameMove(Position(row, col), if (yOffset >= -offset && yOffset <= offset) 1 else 2)
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}