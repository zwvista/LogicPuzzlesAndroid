package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

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

class DigitalBattleShipsGameView(context: Context) : CellsGameView(context) {
    private val activity get() = context as DigitalBattleShipsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows + 1
    override val colsInView get() = cols + 1

    private val gridPaint = Paint()
    private val grayPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        grayPaint.color = Color.GRAY
        grayPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val n = game[r, c]
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                val path = Path()
                when (val o = game.getObject(r, c)) {
                    DigitalBattleShipsObject.BattleShipUnit ->
                        canvas.drawArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 0f, 360f, true, grayPaint)
                    DigitalBattleShipsObject.BattleShipMiddle ->
                        canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), grayPaint)
                    DigitalBattleShipsObject.BattleShipLeft -> {
                        path.addRect(cwc2(c).toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), Path.Direction.CW)
                        path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 90f, 180f)
                        canvas.drawPath(path, grayPaint)
                    }
                    DigitalBattleShipsObject.BattleShipTop -> {
                        path.addRect(cwc(c) + 4.toFloat(), chr2(r).toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), Path.Direction.CW)
                        path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 180f, 180f)
                        canvas.drawPath(path, grayPaint)
                    }
                    DigitalBattleShipsObject.BattleShipRight -> {
                        path.addRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc2(c).toFloat(), chr(r + 1) - 4.toFloat(), Path.Direction.CW)
                        path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 270f, 180f)
                        canvas.drawPath(path, grayPaint)
                    }
                    DigitalBattleShipsObject.BattleShipBottom -> {
                        path.addRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr2(r).toFloat(), Path.Direction.CW)
                        path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 0f, 180f)
                        canvas.drawPath(path, grayPaint)
                    }
                    DigitalBattleShipsObject.Marker ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, grayPaint)
                    DigitalBattleShipsObject.Forbidden ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
                    else -> {}
                }
            }
        if (isInEditMode) return
        textPaint.color = Color.WHITE
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val n = game[r, c]
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        for (r in 0 until rows) {
            val s = game.getRowState(r)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val n = game.row2hint[r]
            val text = n.toString()
            drawTextCentered(text, cwc(cols), chr(r), canvas, textPaint)
        }
        for (c in 0 until cols) {
            val s = game.getColState(c)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val n = game.col2hint[c]
            val text = n.toString()
            drawTextCentered(text, cwc(c), chr(rows), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = DigitalBattleShipsGameMove(Position(row, col))
            if (game.switchObject(move))
                activity.app.soundManager.playSoundTap()
        }
        return true
    }
}