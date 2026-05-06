package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ParkingLotGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ParkingLotGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val dLeft: Drawable
    private val dRight: Drawable
    private val dHorizontal: Drawable
    private val dTop: Drawable
    private val dBottom: Drawable
    private val dVertical: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        dLeft = fromImageToDrawable("images/car_left.png")
        dRight = fromImageToDrawable("images/car_right.png")
        dHorizontal = fromImageToDrawable("images/car_horizontal.png")
        dTop = fromImageToDrawable("images/car_top.png")
        dBottom = fromImageToDrawable("images/car_bottom.png")
        dVertical = fromImageToDrawable("images/car_vertical.png")
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                when (val o = game.getObject(p)) {
                    ParkingLotObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    ParkingLotObject.Empty -> {}
                    else -> {
                        val dObject = when (o) {
                            ParkingLotObject.Left -> dLeft
                            ParkingLotObject.Right -> dRight
                            ParkingLotObject.Horizontal -> dHorizontal
                            ParkingLotObject.Top -> dTop
                            ParkingLotObject.Bottom -> dBottom
                            ParkingLotObject.Vertical -> dVertical
                            else -> continue
                        }
                        dObject.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                        val alpha = if (game.getStateAllowed(p) == AllowedObjectState.Error) 50 else 0
                        dObject.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 0, 255, 0), BlendModeCompat.SRC_ATOP)
                        dObject.draw(canvas)
                    }
                }
                val n = game.pos2hint[p] ?: continue
                val s = game.getStateHint(p)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (!game.isValid(r, c)) Color.GRAY else Color.WHITE
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = ParkingLotGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
