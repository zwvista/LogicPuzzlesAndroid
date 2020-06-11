package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

_
class ParkLakesGameView : CellsGameView {
    private fun activity() = getContext() as ParkLakesGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode()) 5 else game().rows()

    private fun cols() = if (isInEditMode()) 5 else game().cols()

    protected override fun rowsInView() = rows()

    protected override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint: TextPaint = TextPaint()
    private var dTree: Drawable? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.setAntiAlias(true)
        dTree = fromImageToDrawable("images/tree.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode()) return
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: ParkLakesObject = game().getObject(p)
            if (o is ParkLakesTreeObject) {
                val o2: ParkLakesTreeObject = o as ParkLakesTreeObject
                dTree!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpaha = if (o2.state == AllowedObjectState.Error) 50 else 0
                dTree!!.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                dTree!!.draw(canvas)
            } else if (o is ParkLakesHintObject) {
                val o2: ParkLakesHintObject = o as ParkLakesHintObject
                textPaint.setColor(
                    if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                )
                val text = if (o2.tiles == -1) "?" else o2.tiles.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            } else if (o is ParkLakesMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move = ParkLakesGameMove()
                init {
                    p = Position(row, col)
                    obj = ParkLakesEmptyObject()
                }
            }
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}