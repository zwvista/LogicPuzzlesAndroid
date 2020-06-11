package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class TataminoGameView : CellsGameView {
    private fun activity() = getContext() as TataminoGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows()

    protected override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint: TextPaint = TextPaint()

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        textPaint.setAntiAlias(true)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode()) continue
            val p = Position(r, c)
            val ch: Char = game().getObject(p)
            if (ch == ' ') continue
            val s: HintState = game().getPosState(p)
            textPaint.setColor(
                if (game().get(p) == ch) Color.GRAY else if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            )
            val text = ch.toString()
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
        }
        if (isInEditMode()) return
        for (r in 0 until rows() + 1) for (c in 0 until cols() + 1) {
            if (game().getDots().get(r, c, 1) == GridLineObject.Line) canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
            if (game().getDots().get(r, c, 2) == GridLineObject.Line) canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move = TataminoGameMove()
                init {
                    p = Position(row, col)
                    obj = ' '
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}