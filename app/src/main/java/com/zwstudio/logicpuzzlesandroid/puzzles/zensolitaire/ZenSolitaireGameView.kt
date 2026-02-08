package com.zwstudio.logicpuzzlesandroid.puzzles.zensolitaire

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ZenSolitaireGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ZenSolitaireGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val textPaint = TextPaint()
    private val dBackground: Drawable
    private val dStone: Drawable

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
        textPaint.color = Color.BLACK
        dBackground = fromImageToDrawable("images/sand_background.png")
        dStone = fromImageToDrawable("images/pebble1.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                dBackground.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                dBackground.draw(canvas)
                val p = Position(r, c)
                if (game.stones.contains(p)) {
                    dStone.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    dStone.draw(canvas)
                }
                val n = game.getObject(p)
                if (n > 0) {
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
        if (isInEditMode) return
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = ZenSolitaireGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}