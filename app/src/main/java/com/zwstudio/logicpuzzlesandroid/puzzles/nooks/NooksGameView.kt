package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class NooksGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as NooksGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val forbiddenPaint = Paint()
    private val dHedge: Drawable
    private val dHide: Drawable

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
        dHedge = fromImageToDrawable("images/forest_lighter.png")
        dHide = fromImageToDrawable("images/hide.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    NooksObject.Hedge -> {
                        dHedge.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dHedge.draw(canvas)
                    }
                    NooksObject.Hint -> {
                        dHide.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dHide.draw(canvas)
                        val n = game.pos2hint[p]
                        val text = if (n == NooksGame.PUZ_UNKWOWN) "?" else n.toString()
                        val s = game.pos2state(p)
                        textPaint.color = if (s == HintState.Normal) Color.WHITE else if (s == HintState.Complete) Color.GREEN else Color.RED
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                    NooksObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    else -> {}
                }
            }
        if (isInEditMode) return
        for ((r, c) in game.invalid2x2Squares())
            canvas.drawArc(cwc(c) - 20.toFloat(), chr(r) - 20.toFloat(), cwc(c) + 20.toFloat(), chr(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = NooksGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}