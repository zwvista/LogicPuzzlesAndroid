package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ADifferentFarmerGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ADifferentFarmerGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val fixedPaint = Paint()
    private val dFv1: Drawable
    private val dFv2: Drawable
    private val dFv3: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        fixedPaint.color = Color.WHITE
        fixedPaint.style = Paint.Style.STROKE
        dFv1 = fromImageToDrawable("images/fv (1).png")
        dFv2 = fromImageToDrawable("images/fv (2).png")
        dFv3 = fromImageToDrawable("images/fv (3).png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game.getObject(p)
                if (o == ADifferentFarmerObject.Empty) continue
                val dObject = when (o) {
                    ADifferentFarmerObject.Fv1 -> dFv1
                    ADifferentFarmerObject.Fv2 -> dFv2
                    ADifferentFarmerObject.Fv3 -> dFv3
                    else -> dFv1
                }
                dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpha = if (game.pos2state(p) == AllowedObjectState.Error) 50 else 0
                dObject.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                dObject.draw(canvas)
                if (game[p] != ADifferentFarmerObject.Empty)
                    canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, fixedPaint)
            }
        for (r in 0..<rows + 1)
            for (c in 0..<cols + 1) {
                if (game.dots[r, c, 1] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
                if (game.dots[r, c, 2] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = ADifferentFarmerGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}