package com.zwstudio.logicpuzzlesandroid.puzzles.planets

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

class PlanetsGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as PlanetsGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val dNone: Drawable
    private val dNorth: Drawable
    private val dNorthEast: Drawable
    private val dNorthWest: Drawable
    private val dEast: Drawable
    private val dWest: Drawable
    private val dSouth: Drawable
    private val dSouthEast: Drawable
    private val dSouthWest: Drawable
    private val dSun: Drawable
    private val dNebula: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        dNone = fromImageToDrawable("images/planet_none.png")
        dNorth = fromImageToDrawable("images/planet_north.png")
        dNorthEast = fromImageToDrawable("images/planet_north_east.png")
        dNorthWest = fromImageToDrawable("images/planet_north_west.png")
        dEast = fromImageToDrawable("images/planet_east.png")
        dWest = fromImageToDrawable("images/planet_west.png")
        dSouth = fromImageToDrawable("images/planet_south.png")
        dSouthEast = fromImageToDrawable("images/planet_south_east.png")
        dSouthWest = fromImageToDrawable("images/planet_south_west.png")
        dSun = fromImageToDrawable("images/sun.png")
        dNebula = fromImageToDrawable("images/nebula.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    PlanetsObject.Empty -> {}
                    PlanetsObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    PlanetsObject.Forbidden ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, forbiddenPaint)
                    else -> {
                        val dObject = when (o) {
                            PlanetsObject.None -> dNone
                            PlanetsObject.North -> dNorth
                            PlanetsObject.NorthEast -> dNorthEast
                            PlanetsObject.NorthWest -> dNorthWest
                            PlanetsObject.East -> dEast
                            PlanetsObject.West -> dWest
                            PlanetsObject.South -> dSouth
                            PlanetsObject.SouthEast -> dSouthEast
                            PlanetsObject.SouthWest -> dSouthWest
                            PlanetsObject.Sun -> dSun
                            PlanetsObject.Nebula -> dNebula
                            else -> dNone
                        }
                        dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val s = game.pos2state(p)
                        val alpha = if (s == AllowedObjectState.Error) 50 else 0
                        dObject.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
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
            val move = PlanetsGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}