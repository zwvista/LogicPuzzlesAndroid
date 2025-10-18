package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

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
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ZenLandscaperGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ZenLandscaperGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val dTileArray: Array<Drawable>

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        dTileArray = (0 until 7)
            .map {
                val n = if(it == 6) 1 else it / 2 + 2
                val ch = if (it % 2 == 0) "" else "-f"
                fromImageToDrawable("images/B$n$ch.png")
            }
            .toTypedArray()
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game.getObject(p)
                val fixed = game[p] != ' '
                val s = game.getPosState(p)
                val n = if (o == ' ') 6 else (o - '1') * 2 + (if (fixed) 1 else 0)
                val dTile = dTileArray[n]
                dTile.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpha = if (s == AllowedObjectState.Error) 50 else 0
                dTile.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                dTile.draw(canvas)
            }
        if (isInEditMode) return
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = ZenLandscaperGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}