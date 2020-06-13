package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class WallSentinelsGameView(context: Context) : CellsGameView(context) {

    private fun activity() = context as WallSentinelsGameActivity
    private fun game() = activity().game
    private val rows get() = if (isInEditMode) 5 else game().rows
    private val cols get() = if (isInEditMode) 5 else game().cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val wallPaint = Paint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        wallPaint.color = Color.rgb(128, 0, 128)
        wallPaint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game().getObject(p)
                if (o is WallSentinelsMarkerObject)
                    canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, markerPaint)
                if (o is WallSentinelsWallObject || o is WallSentinelsHintWallObject)
                    canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), wallPaint)
                if (o is WallSentinelsHintLandObject || o is WallSentinelsHintWallObject) {
                    val n = (o as? WallSentinelsHintLandObject)?.tiles ?: (o as WallSentinelsHintWallObject).tiles
                    val s = (o as? WallSentinelsHintLandObject)?.state ?: (o as WallSentinelsHintWallObject).state
                    textPaint.color = when (s) {
                        HintState.Complete -> Color.GREEN
                        HintState.Error -> Color.RED
                        else -> Color.WHITE
                    }
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = WallSentinelsGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }

}
