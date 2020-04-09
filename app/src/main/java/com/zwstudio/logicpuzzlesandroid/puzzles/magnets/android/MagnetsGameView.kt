package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android

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
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsAreaType
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsObject

class MagnetsGameView : CellsGameView {
    private fun activity(): MagnetsGameActivity {
        return context as MagnetsGameActivity
    }

    private fun game(): MagnetsGame? {
        return activity().game
    }

    private fun rows(): Int {
        return if (isInEditMode) 5 else game()!!.rows()
    }

    private fun cols(): Int {
        return if (isInEditMode) 5 else game()!!.cols()
    }

    override fun rowsInView(): Int {
        return rows() + 2
    }

    override fun colsInView(): Int {
        return cols() + 2
    }

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private var dPositive: Drawable? = null
    private var dNegative: Drawable? = null

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        dPositive = fromImageToDrawable("images/positive.png")
        dNegative = fromImageToDrawable("images/negative.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        if (isInEditMode) return
        for (a in game()!!.areas) {
            val r = a!!.p!!.row
            val c = a.p!!.col
            when (a.type) {
                MagnetsAreaType.Single -> {
                    canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                    canvas.drawLine(cwc(c + 1).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), gridPaint)
                }
                MagnetsAreaType.Horizontal -> canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 2).toFloat(), chr(r + 1).toFloat(), gridPaint)
                MagnetsAreaType.Vertical -> canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 2).toFloat(), gridPaint)
            }
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o = game()!!.getObject(r, c)
            when (o) {
                MagnetsObject.Positive -> {
                    dPositive!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    dPositive!!.draw(canvas)
                }
                MagnetsObject.Negative -> {
                    dNegative!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    dNegative!!.draw(canvas)
                }
                MagnetsObject.Marker -> canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
            }
        }
        dPositive!!.setBounds(cwc(cols()), chr(rows()), cwc(cols() + 1), chr(rows() + 1))
        dPositive!!.setColorFilter(Color.argb(75, 0, 0, 0), PorterDuff.Mode.SRC_ATOP)
        dPositive!!.draw(canvas)
        dNegative!!.setBounds(cwc(cols() + 1), chr(rows() + 1), cwc(cols() + 2), chr(rows() + 2))
        dNegative!!.setColorFilter(Color.argb(75, 0, 0, 0), PorterDuff.Mode.SRC_ATOP)
        dNegative!!.draw(canvas)
        for (r in 0 until rows()) {
            for (c in 0..1) {
                val id = r * 2 + c
                val s = game()!!.getRowState(id)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                val n = game()!!.row2hint[id]
                val text = n.toString()
                drawTextCentered(text, cwc(cols() + c), chr(r), canvas, textPaint)
            }
        }
        for (c in 0 until cols()) {
            for (r in 0..1) {
                val id = c * 2 + r
                val s = game()!!.getColState(id)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                val n = game()!!.col2hint[id]
                val text = n.toString()
                drawTextCentered(text, cwc(c), chr(rows() + r), canvas, textPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game()!!.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move: MagnetsGameMove = object : MagnetsGameMove() {
                init {
                    p = Position(row, col)
                    obj = MagnetsObject.Empty
                }
            }
            if (game()!!.switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}