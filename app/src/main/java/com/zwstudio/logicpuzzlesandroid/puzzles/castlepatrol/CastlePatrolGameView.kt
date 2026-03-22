package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

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

class CastlePatrolGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as CastlePatrolGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val lightPaint = Paint()
    private val textPaint = TextPaint()
    private val dWall: Drawable

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        lightPaint.color = Color.YELLOW
        lightPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        dWall = fromImageToDrawable("images/tower_wall2.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                fun drawWall() {
                    dWall.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    dWall.draw(canvas)
                }
                fun drawHint() {
                    val n = game.pos2hint[p]!!
                    val s = game.pos2state(p)
                    textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
                when (game.getObject(p)) {
                    CastlePatrolObject.Wall ->
                        drawWall()
                    CastlePatrolObject.WallHint -> {
                        drawWall()
                        drawHint()
                    }
                    CastlePatrolObject.EmptyHint ->
                        drawHint()
                    CastlePatrolObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, wallPaint)
                    else -> {}
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = CastlePatrolGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}