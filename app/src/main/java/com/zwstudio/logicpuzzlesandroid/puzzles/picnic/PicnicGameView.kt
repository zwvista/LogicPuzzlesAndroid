package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

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
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class PicnicGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as PicnicGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val markerPaint = Paint()
    private var pLastDown: Position? = null
    private val textPaint = TextPaint()
//    private val dHedge: Drawable
    private val dBlanket: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.GREEN
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 10f
        markerPaint.color = Color.GREEN
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
//        dHedge = fromImageToDrawable("images/forest_lighter.png")
        dBlanket = fromImageToDrawable("images/mat.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for ((p, n) in game.pos2hint) {
            val p2 = game.hint2blanket(p)
            val (r, c) = p
            if (p2 == null) {
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            } else {
                val (r2, c2) = p2
                dBlanket.setBounds(cwc(c2), chr(r2), cwc(c2 + 1), chr(r2 + 1))
                val alpha = if (game.pos2state(p2) == AllowedObjectState.Error) 50 else 0
                dBlanket.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                dBlanket.draw(canvas)
                canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c2).toFloat(), chr2(r2).toFloat(), linePaint)
                canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
            }
//                    canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (game.isSolved) return true
        val col = (event.x / cellWidth).toInt()
        val row = (event.y / cellHeight).toInt()
        if (col >= cols || row >= rows) return true
        val p = Position(row, col)
        fun f() = soundManager.playSoundTap()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pLastDown = p
                f()
            }
            MotionEvent.ACTION_MOVE -> if (p != pLastDown && pLastDown != null) {
                val n = PicnicGame.offset.indexOfFirst { it == p - pLastDown!! }
                if (n != -1) {
                    val move = PicnicGameMove(pLastDown!!, n)
                    if (game.setObject(move)) f()
                }
                pLastDown = null
            }
            MotionEvent.ACTION_UP -> {
                if (p == pLastDown) {
                    val move = PicnicGameMove(pLastDown!!, PicnicGame.PUZ_TAP_MOVE)
                    if (game.setObject(move)) f()
                }
                pLastDown = null
            }
        }
        return true
    }
}