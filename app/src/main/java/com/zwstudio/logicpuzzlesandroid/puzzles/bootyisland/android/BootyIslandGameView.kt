package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.android.ABCPathGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandEmptyObject
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandForbiddenObject
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandMarkerObject
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandObject
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandTreasureObject

class BootyIslandGameView : CellsGameView {

    private fun activity() = context as BootyIslandGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val forbiddenPaint = Paint()
    private lateinit var dTreasure: Drawable

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        dTreasure = fromImageToDrawable("images/tree.png")
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game().getObject(p)
                if (o is BootyIslandTreasureObject) {
                    dTreasure.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    val alpaha = if (o.state == AllowedObjectState.Error) 50 else 0
                    dTreasure.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                    dTreasure.draw(canvas)
                } else if (o is BootyIslandMarkerObject)
                    canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, markerPaint)
                else if (o is BootyIslandForbiddenObject)
                    canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, forbiddenPaint)
                val n = game().pos2hint[p]
                if (n != null) {
                    val state = game().pos2State(p)
                    textPaint.color = if (state == HintState.Complete)
                        Color.GREEN
                    else if (state == HintState.Error)
                        Color.RED
                    else
                        Color.WHITE
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move = BootyIslandGameMove(Position(row, col), BootyIslandEmptyObject())
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }

}
