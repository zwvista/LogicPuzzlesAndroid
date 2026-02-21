package com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.view.GestureDetector
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import kotlin.math.abs
import kotlin.math.min

class YalooniqGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as YalooniqGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val blockPaint = Paint()
    private val trianglePaint = Paint()
    private val textPaint = TextPaint()
    private var pLastDown: Position? = null
    private var pLastMove: Position? = null
    private var isLongPress = false

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
            isLongPress = true
            val col = (e.x / cellWidth).toInt()
            val row = (e.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return
            val p = Position(row, col)
            if (game.setObject(YalooniqGameMove(p, YalooniqGame.PUZ_DIR_SQUARE)))
                soundManager.playSoundTap()
        }
        override fun onDown(e: MotionEvent): Boolean {
            isLongPress = false
            return true
        }
    })

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.GREEN
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        blockPaint.style = Paint.Style.FILL_AND_STROKE
        trianglePaint.color = Color.LTGRAY
        trianglePaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
    }

    fun drawTriangle(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, direction: Int, paint: Paint) {
        val path = Path()
        val cx = (left + right) / 2
        val cy = (top + bottom) / 2
        val w = right - left
        val h = bottom - top
        val side = min(w, h)

        when (direction) {
            0 -> { // Up
                path.moveTo(cx, cy - side/2)
                path.lineTo(cx - w/2, cy + side/2)
                path.lineTo(cx + w/2, cy + side/2)
            }
            1 -> { // Down
                path.moveTo(cx, cy + side/2)
                path.lineTo(cx - w/2, cy - side/2)
                path.lineTo(cx + w/2, cy - side/2)
            }
            2 -> { // Left
                path.moveTo(cx - w/2, cy)
                path.lineTo(cx + w/2, cy - side/2)
                path.lineTo(cx + w/2, cy + side/2)
            }
            3 -> { // Right
                path.moveTo(cx + w/2, cy)
                path.lineTo(cx - w/2, cy - side/2)
                path.lineTo(cx - w/2, cy + side/2)
            }
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val s1 = game.pos2StateAllowed(p)
                blockPaint.color = if (s1 == AllowedObjectState.Error) Color.RED else Color.WHITE
                if (game.squares().contains(p))
                    canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), blockPaint)
                val hint = game.pos2hint[p] ?: continue
                val s = game.pos2StateHint(p)
                textPaint.color = if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else if (!game.isValid(r, c)) Color.GRAY else Color.WHITE
                val text = hint.num.toString()
                drawTextCentered(text, cwc(c), chr(r), cellWidth / 2, cellHeight, canvas, textPaint)
                drawTriangle(canvas, cwc2(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), hint.dir, trianglePaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val dirs = intArrayOf(1, 2)
                for (dir in dirs) {
                    val b = game.getObject(r, c)[dir]
                    if (!b) continue
                    if (dir == 1)
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c + 1).toFloat(), chr2(r).toFloat(), linePaint)
                    else
                        canvas.drawLine(cwc2(c).toFloat(), chr2(r).toFloat(), cwc2(c).toFloat(), chr2(r + 1).toFloat(), linePaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        if (isLongPress) {
            if (event.action == MotionEvent.ACTION_UP) isLongPress = false
            return true
        }
        if (game.isSolved) return true
        val col = (event.x / cellWidth).toInt()
        val row = (event.y / cellHeight).toInt()
        if (col >= cols || row >= rows) return true
        val p = Position(row, col)
        fun f() = soundManager.playSoundTap()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    pLastMove = p
                    pLastDown = pLastMove
                }
                f()
            }
            MotionEvent.ACTION_MOVE -> if (pLastMove != null && p != pLastMove) {
                val n = YalooniqGame.offset.indexOfFirst { it == p - pLastMove!! }
                if (n != -1) {
                    val move = YalooniqGameMove(pLastMove!!, n)
                    if (game.setObject(move)) f()
                }
                pLastMove = p
            }
            MotionEvent.ACTION_UP -> {
                if (p == pLastDown) {
                    val dx = event.x - (col + 0.5) * cellWidth
                    val dy = event.y - (row + 0.5) * cellHeight
                    val dx2 = abs(dx)
                    val dy2 = abs(dy)
                    val move = YalooniqGameMove(Position(row, col), if (dx2 <= dy2) if (dy > 0) 2 else 0 else if (dx > 0) 1 else 3)
                    game.setObject(move)
                }
                run {
                    pLastMove = null
                    pLastDown = pLastMove
                }
            }
        }
        return true
    }
}
