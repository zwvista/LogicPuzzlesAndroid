package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class RobotCrosswordsGameView : CellsGameView {
    private fun activity() = getContext() as RobotCrosswordsGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows()

    protected override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val textPaint: TextPaint = TextPaint()
    private val hintPaint = Paint()

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
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.setAntiAlias(true)
        hintPaint.style = Paint.Style.FILL
        hintPaint.strokeWidth = 5f
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode()) continue
            val n: Int = game().getObject(r, c)
            if (n == -1) canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint) else if (n > 0) {
                val text = n.toString()
                textPaint.setColor(if (game().get(r, c) == n) Color.GRAY else Color.WHITE)
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        }
        if (isInEditMode()) return
        for (i in game().areas.indices) {
            val a: List<Position> = game().areas.get(i)
            val isHorz: Boolean = i < game().horzAreaCount
            for (p in a) {
                val r = p.row
                val c = p.col
                val s: HintState = if (isHorz) game().getHorzState(p) else game().getVertState(p)
                if (s == HintState.Normal) continue
                hintPaint.color = if (s == HintState.Complete) Color.GREEN else Color.RED
                if (isHorz) canvas.drawArc(cwc(c + 1) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc(c + 1) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, hintPaint) else canvas.drawArc(cwc2(c) - 20.toFloat(), chr(r + 1) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr(r + 1) + 20.toFloat(), 0f, 360f, true, hintPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move: RobotCrosswordsGameMove = object : RobotCrosswordsGameMove() {
                init {
                    p = Position(row, col)
                    obj = 0
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}