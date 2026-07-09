package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class ProofOfQuiltGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as ProofOfQuiltGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val linePaint = Paint()
    private val blockPaint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        linePaint.color = Color.MAGENTA
        linePaint.strokeWidth = 5f
        blockPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val left = (cwc(c) + 4).toFloat()
                val top = (chr(r) + 4).toFloat()
                val right = (cwc(c + 1) - 4).toFloat()
                val bottom = (chr(r + 1) - 4).toFloat()
                fun drawTriangle(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {
                    val path = Path()
                    path.moveTo(x1, y1)
                    path.lineTo(x2, y2)
                    path.lineTo(x3, y3)
                    path.close()
                    canvas.drawPath(path, blockPaint)
                }
                when (game.getObject(p)) {
                    ProofOfQuiltObject.Marker ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, markerPaint)
                    ProofOfQuiltObject.Forbidden ->
                        canvas.drawArc((cwc2(c) - 10).toFloat(), (chr2(r) - 10).toFloat(), (cwc2(c) + 10).toFloat(), (chr2(r) + 10).toFloat(), 0f, 360f, true, forbiddenPaint)
                    ProofOfQuiltObject.TriangleA ->
                        drawTriangle(left, top, right, top, left, bottom)
                    ProofOfQuiltObject.TriangleB ->
                        drawTriangle(left, top, right, top, right, bottom)
                    ProofOfQuiltObject.TriangleC ->
                        drawTriangle(left, top, left, bottom, right, bottom)
                    ProofOfQuiltObject.TriangleD ->
                        drawTriangle(right, top, left, bottom, right, bottom)
                    ProofOfQuiltObject.Filled ->
                        canvas.drawRect(left, top, right, bottom, blockPaint)
                    else -> {}
                }
                val n = game.pos2hint[p] ?: continue
                val text = n.toString()
                val s = game.pos2state(p)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.GRAY
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = ProofOfQuiltGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }

}
