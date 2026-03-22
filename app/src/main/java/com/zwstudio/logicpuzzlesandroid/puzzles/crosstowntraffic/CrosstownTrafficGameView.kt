package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

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

class CrosstownTrafficGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as CrosstownTrafficGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val dUpRight: Drawable
    private val dDownRight: Drawable
    private val dLeftDown: Drawable
    private val dLeftUp: Drawable
    private val dHorizontal: Drawable
    private val dVertical: Drawable
    private val dCross: Drawable
    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        dUpRight = fromImageToDrawable("images/road_upright.png")
        dLeftDown = fromImageToDrawable("images/road_leftdown.png")
        dLeftUp = fromImageToDrawable("images/road_leftup.png")
        dDownRight = fromImageToDrawable("images/road_downright.png")
        dHorizontal = fromImageToDrawable("images/road_horizontal.png")
        dVertical = fromImageToDrawable("images/road_vertical.png")
        dCross = fromImageToDrawable("images/road_cross.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 1 until rows - 1)
            for (c in 1 until cols - 1)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    CrosstownTrafficObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    CrosstownTrafficObject.Hint -> {
                        val (n, s) = game.pos2hint[p]!! to game.pos2state(p)!!
                        textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (!game.isValid(r, c)) Color.GRAY else Color.WHITE
                        val text = n.toString()
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                    else -> {
                        val dObject = when (game.getObject(p)) {
                            CrosstownTrafficObject.UpRight -> dUpRight
                            CrosstownTrafficObject.DownRight -> dDownRight
                            CrosstownTrafficObject.LeftDown -> dLeftDown
                            CrosstownTrafficObject.LeftUp -> dLeftUp
                            CrosstownTrafficObject.Horizontal -> dHorizontal
                            CrosstownTrafficObject.Vertical -> dVertical
                            CrosstownTrafficObject.Cross -> dCross
                            else -> continue
                        }
                        dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        dObject.draw(canvas)
                    }
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            val move = CrosstownTrafficGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}