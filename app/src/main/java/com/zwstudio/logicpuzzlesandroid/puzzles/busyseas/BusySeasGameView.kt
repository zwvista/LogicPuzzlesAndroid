package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

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

class BusySeasGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as BusySeasGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val forbiddenPaint = Paint()
    private val dLightbulb: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        dLightbulb = fromImageToDrawable("images/lightbulb.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    is BusySeasLighthouseObject -> {
                        dLightbulb.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val alpaha = if (o.state == AllowedObjectState.Error) 50 else 0
                        dLightbulb.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpaha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                        dLightbulb.draw(canvas)
                    }
                    is BusySeasMarkerObject ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
                    is BusySeasForbiddenObject ->
                        canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
                    else -> {
                        val n = game.pos2hint[p]
                        if (n != null) {
                            val state = game.pos2State(p)
                            textPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
                            val text = n.toString()
                            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                        }
                    }
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = BusySeasGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}