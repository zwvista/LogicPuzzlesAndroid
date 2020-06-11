package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class PairakabeGameView : CellsGameView {
    private fun activity() = getContext() as PairakabeGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows()

    protected override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val textPaint: TextPaint = TextPaint()

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.setAntiAlias(true)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode()) continue
            val o: PairakabeObject = game().getObject(r, c)
            if (o is PairakabeHintObject) {
                val o2: PairakabeHintObject = o as PairakabeHintObject
                val n: Int = game().pos2hint.get(Position(r, c))
                if (n >= 0) {
                    textPaint.setColor(
                        if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                    )
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            } else if (o is PairakabeWallObject) {
                val o2: PairakabeWallObject = o as PairakabeWallObject
                canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint)
            } else if (o is PairakabeMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, wallPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move = PairakabeGameMove()
                init {
                    p = Position(row, col)
                    obj = PairakabeEmptyObject()
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}