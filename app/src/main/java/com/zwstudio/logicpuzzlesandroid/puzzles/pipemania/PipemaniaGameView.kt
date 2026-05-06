package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class PipemaniaGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as PipemaniaGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val markerPaint = Paint()
    private val fixedPaint = Paint()
    private val dUpRight: Drawable
    private val dDownRight: Drawable
    private val dLeftDown: Drawable
    private val dLeftUp: Drawable
    private val dHorizontal: Drawable
    private val dVertical: Drawable
    private val dCross: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        fixedPaint.color = Color.WHITE
        fixedPaint.style = Paint.Style.STROKE
        dUpRight = fromImageToDrawable("images/pipe_1.png")
        dDownRight = fromImageToDrawable("images/pipe_2.png")
        dLeftDown = fromImageToDrawable("images/pipe_3.png")
        dLeftUp = fromImageToDrawable("images/pipe_4.png")
        dHorizontal = fromImageToDrawable("images/pipe_horizontal.png")
        dVertical = fromImageToDrawable("images/pipe_vertical.png")
        dCross = fromImageToDrawable("images/pipe_cross.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dObject = when (game.getObject(p)) {
                    PipemaniaObject.UpRight -> dUpRight
                    PipemaniaObject.DownRight -> dDownRight
                    PipemaniaObject.LeftDown -> dLeftDown
                    PipemaniaObject.LeftUp -> dLeftUp
                    PipemaniaObject.Horizontal -> dHorizontal
                    PipemaniaObject.Vertical -> dVertical
                    PipemaniaObject.Cross -> dCross
                    else -> continue
                }
                dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dObject.draw(canvas)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = PipemaniaGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}