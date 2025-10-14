package com.zwstudio.logicpuzzlesandroid.puzzles.arrows

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

class ArrowsGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ArrowsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val textPaint = TextPaint()
    private val dArrowList: List<Drawable>

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
        dArrowList = getArrowDrawableList()
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (!((r == 0 || r == rows - 1) && (c == 0 || c == cols - 1)))
                    canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = game.getObject(p)
                if (game.isCorner(p))
                    ;
                else if (game.isBorder(p)) {
                    if (n == ArrowsGame.PUZ_UNKNOWN) continue
                    val dArrow = dArrowList[n]
                    dArrow.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    val alpaha = if (game.getArrowState(p) == AllowedObjectState.Error) 50 else 0
                    dArrow.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpaha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                    dArrow.draw(canvas)
                } else {
                    val text = n.toString()
                    val s = game.getHintState(p)
                    textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                    drawTextCentered(text, cwc(c), chr(r), cellWidth, cellHeight, canvas, textPaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            val move = ArrowsGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
