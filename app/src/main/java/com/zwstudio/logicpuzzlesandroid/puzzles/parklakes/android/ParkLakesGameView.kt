package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android

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
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.*

class ParkLakesGameView : CellsGameView {
    private fun activity() = context as ParkLakesGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private lateinit var dTree: Drawable

    constructor(context: Context?) : super(context) { init(null, 0) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs, 0) }
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init(attrs, defStyle) }

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
        for (r in 0 until rows())
            for (c in 0 until cols())
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o: ParkLakesObject = game().getObject(p)
                if (o is ParkLakesTreeObject) {
                    dTree.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    val alpaha = if (o.state == AllowedObjectState.Error) 50 else 0
                    dTree.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                    dTree.draw(canvas)
                } else if (o is ParkLakesHintObject) {
                    textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
                    val text = if (o.tiles == -1) "?" else o.tiles.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                } else if (o is ParkLakesMarkerObject)
                    canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move = ParkLakesGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}