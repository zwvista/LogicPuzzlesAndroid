package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.android

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
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.*

class TierraDelFuegoGameView : CellsGameView {
    private fun activity() = getContext() as TierraDelFuegoGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode()) 5 else game().rows()
    private fun cols() = if (isInEditMode()) 5 else game().cols()
    protected override fun rowsInView() = rows()
    protected override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
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
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.setAntiAlias(true)
        dTree = fromImageToDrawable("images/tree.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode()) return
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: TierraDelFuegoObject = game().getObject(p)
            if (o is TierraDelFuegoTreeObject) {
                val o2: TierraDelFuegoTreeObject = o as TierraDelFuegoTreeObject
                dTree!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpaha = if (o2.state == AllowedObjectState.Error) 50 else 0
                dTree!!.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                dTree!!.draw(canvas)
            } else if (o is TierraDelFuegoHintObject) {
                val o2: TierraDelFuegoHintObject = o as TierraDelFuegoHintObject
                textPaint.setColor(if (o2.state == HintState.Complete) Color.GREEN else Color.WHITE)
                val text: String = o2.id.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            } else if (o is TierraDelFuegoMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint) else if (o is TierraDelFuegoForbiddenObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            val col = (event.getX() / cellWidth) as Int
            val row = (event.getY() / cellHeight) as Int
            if (col >= cols() || row >= rows()) return true
            val move = TierraDelFuegoGameMove(Position(row, col))
            if (game().switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}