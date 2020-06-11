package com.zwstudio.logicpuzzlesandroid.puzzles.tents.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class TentsGameView : CellsGameView {
    private fun activity() = getContext() as TentsGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows() + 1

    protected override fun colsInView() = cols() + 1

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint: TextPaint = TextPaint()
    private var dTree: Drawable? = null
    private var dTent: Drawable? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.setAntiAlias(true)
        dTree = fromImageToDrawable("images/tree.png")
        dTent = fromImageToDrawable("images/tent.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode()) continue
            val o: TentsObject = game().getObject(r, c)
            if (o is TentsTreeObject) {
                val o2: TentsTreeObject = o as TentsTreeObject
                dTree!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpaha = if (o2.state == AllowedObjectState.Error) 50 else 0
                dTree!!.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                dTree!!.draw(canvas)
            } else if (o is TentsTentObject) {
                val o2: TentsTentObject = o as TentsTentObject
                dTent!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpaha = if (o2.state == AllowedObjectState.Error) 50 else 0
                dTent!!.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                dTent!!.draw(canvas)
            } else if (o is TentsMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint) else if (o is TentsForbiddenObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
        }
        if (isInEditMode()) return
        for (r in 0 until rows()) {
            val s: HintState = game().getRowState(r)
            textPaint.setColor(
                if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            )
            val n: Int = game().row2hint.get(r)
            val text = n.toString()
            drawTextCentered(text, cwc(cols()), chr(r), canvas, textPaint)
        }
        for (c in 0 until cols()) {
            val s: HintState = game().getColState(c)
            textPaint.setColor(
                if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            )
            val n: Int = game().col2hint.get(c)
            val text = n.toString()
            drawTextCentered(text, cwc(c), chr(rows()), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move = TentsGameMove()
                init {
                    p = Position(row, col)
                    obj = TentsEmptyObject()
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}