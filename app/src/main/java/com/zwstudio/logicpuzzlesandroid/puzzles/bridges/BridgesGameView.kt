package com.zwstudio.logicpuzzlesandroid.puzzles.bridges

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class BridgesGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as BridgesGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val islandPaint = Paint()
    private val bridgePaint = Paint()
    private val textPaint = TextPaint()
    private var pLast: Position? = null

    init {
        islandPaint.color = Color.WHITE
        islandPaint.style = Paint.Style.STROKE
        bridgePaint.color = Color.YELLOW
        bridgePaint.style = Paint.Style.STROKE
        bridgePaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        if (isInEditMode) return
        for ((p, info) in game.islandsInfo) {
            val r = p.row
            val c = p.col
            val o = game.getObject(p) as BridgesIslandObject
            canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, islandPaint)
            textPaint.color = if (o.state == HintState.Complete) Color.GREEN else if (o.state == HintState.Error) Color.RED else Color.WHITE
            val text = info.bridges.toString()
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            val dirs = intArrayOf(1, 2)
            for (dir in dirs) {
                val p2 = info.neighbors[dir] ?: continue
                val r2 = p2.row
                val c2 = p2.col
                val b = o.bridges[dir]
                if (dir == 1 && b == 1)
                    canvas.drawLine(cwc(c + 1).toFloat(), chr2(r).toFloat(), cwc(c2).toFloat(), chr2(r2).toFloat(), bridgePaint)
                else if (dir == 1 && b == 2) {
                    canvas.drawLine(cwc(c + 1).toFloat(), chr2(r) - 10.toFloat(), cwc(c2).toFloat(), chr2(r2) - 10.toFloat(), bridgePaint)
                    canvas.drawLine(cwc(c + 1).toFloat(), chr2(r) + 10.toFloat(), cwc(c2).toFloat(), chr2(r2) + 10.toFloat(), bridgePaint)
                } else if (dir == 2 && b == 1)
                    canvas.drawLine(cwc2(c).toFloat(), chr(r + 1).toFloat(), cwc2(c2).toFloat(), chr(r2).toFloat(), bridgePaint)
                else if (dir == 2 && b == 2) {
                    canvas.drawLine(cwc2(c) - 10.toFloat(), chr(r + 1).toFloat(), cwc2(c2) - 10.toFloat(), chr(r2).toFloat(), bridgePaint)
                    canvas.drawLine(cwc2(c) + 10.toFloat(), chr(r + 1).toFloat(), cwc2(c2) + 10.toFloat(), chr(r2).toFloat(), bridgePaint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (game.isSolved) return true
        val col = (event.x / cellWidth).toInt()
        val row = (event.y / cellHeight).toInt()
        if (col >= cols || row >= rows) return true
        val p = Position(row, col)
        val isI = game.isIsland(p)
        fun f() = soundManager.playSoundTap()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (isI) {
                pLast = p
                f()
            }
            MotionEvent.ACTION_MOVE -> if (isI && pLast != null && p != pLast) {
                val move = BridgesGameMove(pLast!!, p)
                game.switchBridges(move)
                pLast = p
                f()
            }
            MotionEvent.ACTION_UP -> pLast = null
        }
        return true
    }
}