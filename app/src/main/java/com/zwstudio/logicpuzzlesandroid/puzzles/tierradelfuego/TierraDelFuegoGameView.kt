package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class TierraDelFuegoGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as TierraDelFuegoGameActivity
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
        for (r in 0..<rows)
            for (c in 0..<cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (game.getObject(p)) {
                    TierraDelFuegoObject.Water -> {
                        dWater.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val s = game.pos2stateAllowed(p)
                        val alpha = if (s == AllowedObjectState.Error) 50 else 0
                        dWater.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                        dWater.draw(canvas)
                    }
                    TierraDelFuegoObject.Hint -> {
                        val id = game.pos2hint[p]!!
                        val s = game.pos2stateHint(p)
                        textPaint.color = if (s == HintState.Complete) Color.GREEN else Color.WHITE
                        val text = id.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                    TierraDelFuegoObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    TierraDelFuegoObject.Forbidden ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, forbiddenPaint)
                    else -> {}
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = TierraDelFuegoGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}