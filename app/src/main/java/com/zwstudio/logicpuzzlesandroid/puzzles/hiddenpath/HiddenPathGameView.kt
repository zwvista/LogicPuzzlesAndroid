package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import android.R.attr.text
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.KropkiGameMove
import kotlin.math.abs

class HiddenPathGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as HiddenPathGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val textPaint = TextPaint()
    private val dArrowList: List<Drawable>
    private val dStar: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
        dArrowList = getArrowDrawableList()
        dStar = fromImageToDrawable("images/TileContent/star_yellow.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val hint = game.pos2hint[p]!!
                val dImage = if (hint == 8) dStar else dArrowList[hint]
                dImage.setBounds(cwc2(c), chr2(r), cwc(c + 1), chr(r + 1))
                dImage.draw(canvas)
                val (n, state) = game.getObject(p)
                if (n != 0) {
                    textPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = HiddenPathGameMove(Position(row, col))
            if (game.setObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}
