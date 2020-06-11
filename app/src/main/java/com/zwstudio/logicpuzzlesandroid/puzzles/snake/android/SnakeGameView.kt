package com.zwstudio.logicpuzzlesandroid.puzzles.snake.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class SnakeGameView : CellsGameView {
    private fun activity() = getContext() as SnakeGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows() + 1

    protected override fun colsInView() = cols() + 1

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val grayPaint = Paint()
    private val lightPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint: TextPaint = TextPaint()

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        grayPaint.color = Color.GRAY
        grayPaint.style = Paint.Style.FILL_AND_STROKE
        lightPaint.color = Color.YELLOW
        lightPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.setAntiAlias(true)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode()) continue
            val p = Position(r, c)
            val o: SnakeObject = game().getObject(p)
            when (o) {
                SnakeObject.Snake -> canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(),
                    if (game().pos2snake.contains(p)) grayPaint else wallPaint)
                SnakeObject.Marker -> canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, wallPaint)
                SnakeObject.Forbidden -> canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
            }
        }
        if (isInEditMode()) return
        for (r in 0 until rows()) {
            val s: HintState = game().getRowState(r)
            textPaint.setColor(
                if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            )
            val n: Int = game().row2hint.get(r)
            if (n < 0) continue
            val text = n.toString()
            drawTextCentered(text, cwc(cols()), chr(r), canvas, textPaint)
        }
        for (c in 0 until cols()) {
            val s: HintState = game().getColState(c)
            textPaint.setColor(
                if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            )
            val n: Int = game().col2hint.get(c)
            if (n < 0) continue
            val text = n.toString()
            drawTextCentered(text, cwc(c), chr(rows()), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move = SnakeGameMove()
                init {
                    p = Position(row, col)
                    obj = SnakeObject.Empty
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}