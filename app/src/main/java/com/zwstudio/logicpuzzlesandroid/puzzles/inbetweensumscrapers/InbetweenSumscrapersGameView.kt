package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweensumscrapers

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

class InbetweenSumscrapersGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as InbetweenSumscrapersGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows + 1
    override val colsInView get() = cols + 1

    private val gridPaint = Paint()
    private val textPaint = TextPaint()
    private val dSkyscraper: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
        dSkyscraper = fromImageToDrawable("images/office_building.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val s = game.pos2state(p)
                when (val n = game.getObject(p)) {
                    InbetweenSumscrapersGame.PUZ_SKYSCRAPER -> {
                        dSkyscraper.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val alpha = if (s == AllowedObjectState.Error) 50 else 0
                        dSkyscraper.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                        dSkyscraper.draw(canvas)
                    }
                    InbetweenSumscrapersGame.PUZ_EMPTY -> {}
                    else -> {
                        textPaint.color = if (s == AllowedObjectState.Error) Color.RED else Color.WHITE
                        val text = n.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                }
            }
        if (isInEditMode) return
        for (r in 0 until rows) {
            val s = game.row2state(r)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val n = game.row2hint[r]
            if (n < 0) continue
            val text = n.toString()
            drawTextCentered(text, cwc(cols), chr(r), canvas, textPaint)
        }
        for (c in 0 until cols) {
            val s = game.col2state(c)
            textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val n = game.col2hint[c]
            if (n < 0) continue
            val text = n.toString()
            drawTextCentered(text, cwc(c), chr(rows), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = InbetweenSumscrapersGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}