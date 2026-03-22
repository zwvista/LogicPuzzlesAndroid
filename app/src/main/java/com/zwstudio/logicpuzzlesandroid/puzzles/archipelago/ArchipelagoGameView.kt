package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ArchipelagoGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ArchipelagoGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint = TextPaint()
    private val dWater: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        dWater = fromImageToDrawable("images/sea.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    ArchipelagoObject.Water -> {
                        dWater.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dWater.draw(canvas)
                    }
                    ArchipelagoObject.Hint -> {
                        val s = game.pos2state(p)
                        textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                        val text = game.pos2hint[p]!!.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                    ArchipelagoObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    else -> {}
                }
            }
        for ((r, c) in game.invalid2x2Squares())
            canvas.drawArc(cwc(c) - 20.toFloat(), chr(r) - 20.toFloat(), cwc(c) + 20.toFloat(), chr(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = ArchipelagoGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}