package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class CarpentersWallGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as CarpentersWallGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val textPaint = TextPaint()
    private val fixedPaint = Paint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        fixedPaint.color = Color.WHITE
        fixedPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                when (val o = game.getObject(r, c)) {
                    is CarpentersWallCornerObject -> {
                        val n = o.tiles
                        textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
                        val text = if (n == 0) "?" else n.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                        canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, fixedPaint)
                    }
                    is CarpentersWallLeftObject -> {
                        textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
                        drawTextCentered("<", cwc(c), chr(r), canvas, textPaint)
                    }
                    is CarpentersWallRightObject -> {
                        textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
                        drawTextCentered(">", cwc(c), chr(r), canvas, textPaint)
                    }
                    is CarpentersWallUpObject -> {
                        textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
                        drawTextCentered("^", cwc(c), chr(r), canvas, textPaint)
                    }
                    is CarpentersWallDownObject -> {
                        textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
                        drawTextCentered("v", cwc(c), chr(r), canvas, textPaint)
                    }
                    is CarpentersWallWallObject ->
                        canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint)
                    is CarpentersWallMarkerObject ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, wallPaint)
                    else -> {}
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = CarpentersWallGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}