package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class PaintTheNurikabeGameView(context: Context) : CellsGameView(context) {
    private fun activity() = context as PaintTheNurikabeGameActivity
    private fun game() = activity().game
    private val rows get() = if (isInEditMode) 5 else game().rows
    private val cols get() = if (isInEditMode) 5 else game().cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val filledPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        filledPaint.color = Color.rgb(128, 0, 128)
        filledPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o: PaintTheNurikabeObject = game().getObject(p)
                when (o) {
                    PaintTheNurikabeObject.Painted -> canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), filledPaint)
                    PaintTheNurikabeObject.Marker -> canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
                    else -> {}
                }
                val n = game().pos2hint[p]
                if (n != null) {
                    val state = game().pos2State(p)
                    textPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
        for (r in 0 until rows + 1)
            for (c in 0 until cols + 1) {
                if (game().dots[r, c, 1] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
                if (game().dots[r, c, 2] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = PaintTheNurikabeGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}