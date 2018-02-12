package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.*

/**
 * TODO: document your custom view class.
 */
class WallSentinels2GameView : CellsGameView {
    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val textPaint = TextPaint()
    private val wallPaint = Paint()

    private fun activity() = context as WallSentinels2GameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        wallPaint.color = Color.rgb(128, 0, 128)
        wallPaint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game().getObject(p)
                if (o is WallSentinels2MarkerObject)
                    canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, markerPaint)
                if (o is WallSentinels2WallObject || o is WallSentinels2HintWallObject)
                    canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), wallPaint)
                if (o is WallSentinels2HintLandObject || o is WallSentinels2HintWallObject) {
                    val n = (o as? WallSentinels2HintLandObject)?.tiles
                            ?: (o as WallSentinels2HintWallObject).tiles
                    val state = (o as? WallSentinels2HintLandObject)?.state
                            ?: (o as WallSentinels2HintWallObject).state
                    textPaint.color = if (state == HintState.Complete)
                        Color.GREEN
                    else if (state == HintState.Error)
                        Color.RED
                    else
                        Color.WHITE
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
            val move = WallSentinels2GameMove(Position(row, col), WallSentinels2EmptyObject())
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }

}
