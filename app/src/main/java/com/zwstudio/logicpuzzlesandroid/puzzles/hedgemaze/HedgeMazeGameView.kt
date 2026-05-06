package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class HedgeMazeGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as HedgeMazeGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val linePaint = Paint()
    private val dGate: Drawable
    private val dStep: Drawable
    private val dFountain: Drawable
    private val dHedge: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        dGate = fromImageToDrawable("images/gates.png")
        dStep = fromImageToDrawable("images/footstep.png")
        dFountain = fromImageToDrawable("images/fountain.png")
        dHedge = fromImageToDrawable("images/forest_lighter.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    HedgeMazeObject.Empty -> {}
                    HedgeMazeObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    HedgeMazeObject.Forbidden ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, forbiddenPaint)
                    else -> {
                        val dObject = when (o) {
                            HedgeMazeObject.Gate -> dGate
                            HedgeMazeObject.Step -> dStep
                            HedgeMazeObject.Fountain -> dFountain
                            HedgeMazeObject.Hedge -> dHedge
                            else -> continue
                        }
                        dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dObject.draw(canvas)
                    }
                }
            }
        for (r in 0..<rows + 1)
            for (c in 0..<cols + 1) {
                if (game.dots[r, c, 1] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
                if (game.dots[r, c, 2] == GridLineObject.Line)
                    canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
            }
        for ((r, c) in game.invalid2x2Squares())
            canvas.drawArc(cwc(c) - 20.toFloat(), chr(r) - 20.toFloat(), cwc(c) + 20.toFloat(), chr(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = HedgeMazeGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}