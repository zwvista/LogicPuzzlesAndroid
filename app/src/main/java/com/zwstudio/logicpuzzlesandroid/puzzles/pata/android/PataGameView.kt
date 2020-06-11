package com.zwstudio.logicpuzzlesandroid.puzzles.pata.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class PataGameView : CellsGameView {
    private fun activity() = getContext() as PataGameActivity

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
            val o: PataObject = game().getObject(r, c)
            if (o is PataWallObject) {
                val o2: PataWallObject = o as PataWallObject
                canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint)
            } else if (o is PataHintObject) {
                val o2: PataHintObject = o as PataHintObject
                val hint: List<Int> = game().pos2hint.get(Position(r, c))
                textPaint.setColor(
                    if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                )
                val hint2Str: F<Int, String> = F<Int, String> { i: Int? ->
                    val n = hint[i!!]
                    if (n == -1) "?" else n.toString()
                }
                when (hint.size) {
                    1 -> drawTextCentered(hint2Str.f(0), cwc(c), chr(r), canvas, textPaint)
                    2 -> {
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        drawTextCentered(hint2Str.f(1), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                    }
                    3 -> {
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), cellWidth, cellHeight / 2, canvas, textPaint)
                        drawTextCentered(hint2Str.f(1), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        drawTextCentered(hint2Str.f(2), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                    }
                    4 -> {
                        drawTextCentered(hint2Str.f(0), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        drawTextCentered(hint2Str.f(1), cwc2(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        drawTextCentered(hint2Str.f(2), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        drawTextCentered(hint2Str.f(3), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                    }
                }
            } else if (o is PataMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, wallPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move: PataGameMove = object : PataGameMove() {
                init {
                    p = Position(row, col)
                    obj = PataEmptyObject()
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}