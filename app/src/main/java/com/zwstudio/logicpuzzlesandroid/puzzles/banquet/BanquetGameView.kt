package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class BanquetGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as BanquetGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val filledPaint = Paint()
    private var pLastDown: Position? = null
    private val textPaint = TextPaint()
//    private val dHedge: Drawable
//    private val dHide: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        filledPaint.color = Color.GRAY
        filledPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
//        dHedge = fromImageToDrawable("images/forest_lighter.png")
//        dHide = fromImageToDrawable("images/hide.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for ((p, n) in game.pos2hint) {
            val p2 = game.hint2blanket(p)!!
            val (r, c) = p2
            if (p == p2) {
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            } else {
                canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), filledPaint)
            }
//                    dHedge.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
//                    dHedge.draw(canvas)
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
                val n = BanquetGame.offset.indexOfFirst { it == p - pLastDown!! }
                if (n != -1) {
                    val move = BanquetGameMove(pLastDown!!, n)
                    if (game.setObject(move)) f()
                }
                pLastDown = null
            }
            MotionEvent.ACTION_UP -> {
                if (p == pLastDown) {
                    val move = BanquetGameMove(pLastDown!!, BanquetGame.PUZ_CANCEL_MOVE)
                    if (game.setObject(move)) f()
                }
                pLastDown = null
            }
        }
        return true
    }
}