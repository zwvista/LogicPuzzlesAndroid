package com.zwstudio.logicpuzzlesandroid.puzzles.thegreylabyrinth

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

class TheGreyLabyrinthGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as TheGreyLabyrinthGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val dUp: Drawable
    private val dRight: Drawable
    private val dDown: Drawable
    private val dLeft: Drawable
    private val dTreasure: Drawable
    private val dWall: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        dUp = fromImageToDrawable("images/arrow8_bw.png")
        dRight = fromImageToDrawable("images/arrow6_bw.png")
        dDown = fromImageToDrawable("images/arrow2_bw.png")
        dLeft = fromImageToDrawable("images/arrow4_bw.png")
        dTreasure = fromImageToDrawable("images/chest_treasure.png")
        dWall = fromImageToDrawable("images/tower_wall2.png")
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
                when (val o = game.getObject(p)) {
                    TheGreyLabyrinthObject.Forbidden ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, forbiddenPaint)
                    TheGreyLabyrinthObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    TheGreyLabyrinthObject.Wall -> {
                        dWall.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val s = game.pos2state(p)
                        val alpha = if (s == AllowedObjectState.Error) 50 else 0
                        dWall.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                        dWall.draw(canvas)
                    }
                    else -> {
                        val dObject = when (o) {
                            TheGreyLabyrinthObject.Up -> dUp
                            TheGreyLabyrinthObject.Right -> dRight
                            TheGreyLabyrinthObject.Down -> dDown
                            TheGreyLabyrinthObject.Left -> dLeft
                            TheGreyLabyrinthObject.Treasure -> dTreasure
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
            if (col >= cols || row >= rows) return true
            val move = TheGreyLabyrinthGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}