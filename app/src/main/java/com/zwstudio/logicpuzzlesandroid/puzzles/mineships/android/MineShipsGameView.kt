package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.*

class MineShipsGameView : CellsGameView {
    private fun activity() = context as MineShipsGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val whitePaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint = TextPaint()

    constructor(context: Context?) : super(context) { init(null, 0) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs, 0) }
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init(attrs, defStyle) }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        whitePaint.color = Color.WHITE
        whitePaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.setAntiAlias(true)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o: MineShipsObject = game().getObject(p)
                val path = Path()
                if (o is MineShipsBattleShipUnitObject)
                    canvas.drawArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 0f, 360f, true, whitePaint)
                else if (o is MineShipsBattleShipMiddleObject)
                    canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), whitePaint)
                else if (o is MineShipsBattleShipLeftObject) {
                    path.addRect(cwc2(c).toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), Path.Direction.CW)
                    path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 90f, 180f)
                    canvas.drawPath(path, whitePaint)
                } else if (o is MineShipsBattleShipTopObject) {
                    path.addRect(cwc(c) + 4.toFloat(), chr2(r).toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), Path.Direction.CW)
                    path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 180f, 180f)
                    canvas.drawPath(path, whitePaint)
                } else if (o is MineShipsBattleShipRightObject) {
                    path.addRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc2(c).toFloat(), chr(r + 1) - 4.toFloat(), Path.Direction.CW)
                    path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 270f, 180f)
                    canvas.drawPath(path, whitePaint)
                } else if (o is MineShipsBattleShipBottomObject) {
                    path.addRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr2(r).toFloat(), Path.Direction.CW)
                    path.addArc(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), 0f, 180f)
                    canvas.drawPath(path, whitePaint)
                } else if (o is MineShipsMarkerObject)
                    canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, markerPaint)
                else if (o is MineShipsForbiddenObject)
                    canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, forbiddenPaint)
                val n = game().pos2hint[p]
                if (n != null) {
                    val state = game().pos2State(p)
                    textPaint.color = if (state == HintState.Complete) Color.GREEN else if (state == HintState.Error) Color.RED else Color.WHITE
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move = MineShipsGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }
}