package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android

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
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.*

class GardenerGameView : CellsGameView {
    private fun activity() = context as GardenerGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode) 5 else game()!!.rows()

    private fun cols() = if (isInEditMode) 5 else game()!!.cols()

    override fun rowsInView() = rows()

    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint = TextPaint()
    private val hintPaint = Paint()
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
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        hintPaint.style = Paint.Style.FILL
        hintPaint.strokeWidth = 5f
        hintPaint.color = Color.RED
        dTree = fromImageToDrawable("images/tree.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows() + 1) for (c in 0 until cols() + 1) {
            if (game()!!.dots[r, c, 1] == GridLineObject.Line) canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
            if (game()!!.dots[r, c, 2] == GridLineObject.Line) canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = game()!!.getObject(p)
            if (o is GardenerTreeObject) {
                dTree!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpaha = if (o.state == AllowedObjectState.Error) 50 else 0
                dTree!!.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                dTree!!.draw(canvas)
            } else if (o is GardenerMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint) else if (o is GardenerForbiddenObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
            if (game()!!.invalidSpaces(p, true)) canvas.drawArc(cwc(c + 1) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc(c + 1) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, hintPaint)
            if (game()!!.invalidSpaces(p, false)) canvas.drawArc(cwc2(c) - 20.toFloat(), chr(r + 1) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr(r + 1) + 20.toFloat(), 0f, 360f, true, hintPaint)
        }
        for ((p, value) in game()!!.pos2hint) {
            val n = value!!._1()
            val state = game()!!.pos2State(p)
            textPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
            val text = n.toString()
            val r = p.row
            val c = p.col
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game()!!.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move: GardenerGameMove = object : GardenerGameMove() {
                init {
                    p = Position(row, col)
                    obj = GardenerEmptyObject()
                }
            }
            if (game()!!.switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}