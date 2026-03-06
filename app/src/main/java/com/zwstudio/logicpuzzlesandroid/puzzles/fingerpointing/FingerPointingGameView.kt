package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

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
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class FingerPointingGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as FingerPointingGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val blockPaint = Paint()
    private val textPaint = TextPaint()
    private val dUp: Drawable
    private val dRight: Drawable
    private val dDown: Drawable
    private val dLeft: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        blockPaint.color = Color.WHITE
        blockPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        dUp = fromImageToDrawable("images/finger_up.png")
        dRight = fromImageToDrawable("images/finger_right.png")
        dDown = fromImageToDrawable("images/finger_down.png")
        dLeft = fromImageToDrawable("images/finger_left.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    FingerPointingObject.Hint -> {
                        val s = game.pos2state(p)
                        textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                        val text = game.pos2hint[p]!!.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                    FingerPointingObject.Block ->
                        canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), blockPaint)
                    else -> {
                        val d = when (o) {
                            FingerPointingObject.Up -> dUp
                            FingerPointingObject.Right -> dRight
                            FingerPointingObject.Down -> dDown
                            FingerPointingObject.Left -> dLeft
                            else -> continue
                        }
                        d.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val alpha = if (game.pos2state(p) == HintState.Error) 50 else 0
                        d.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                        d.draw(canvas)
                    }
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = FingerPointingGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}