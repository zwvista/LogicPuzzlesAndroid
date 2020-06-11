package com.zwstudio.logicpuzzlesandroid.puzzles.walls.androidimport

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class WallsGameView : CellsGameView {
    private fun activity() = getContext() as WallsGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows()

    protected override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint: TextPaint = TextPaint()
    private var dTree: Drawable? = null

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
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        textPaint.setAntiAlias(true)
        dTree = fromImageToDrawable("images/tree.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode()) return
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: WallsObject = game().getObject(p)
            if (o is WallsHorzObject || o is WallsVertObject) {
                val isHorz = o is WallsHorzObject
                dTree!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                dTree!!.setColorFilter(Color.argb(0, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                if (isHorz) {
                    canvas.save()
                    canvas.rotate(90f, cwc2(c).toFloat(), chr2(r).toFloat())
                }
                dTree!!.draw(canvas)
                if (isHorz) canvas.restore()
            } else if (o is WallsHintObject) {
                val o2: WallsHintObject = o as WallsHintObject
                val text: String = o2.walls.toString()
                val s: HintState = o2.state
                textPaint.setColor(
                    if (s == HintState.Normal) Color.WHITE else if (s == HintState.Complete) Color.GREEN else Color.RED
                )
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move: WallsGameMove = object : WallsGameMove() {
                init {
                    p = Position(row, col)
                    obj = WallsEmptyObject()
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}