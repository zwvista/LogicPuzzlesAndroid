package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino

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

class TataminoGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as TataminoGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val line1Paint = Paint()
    private val line2Paint = Paint()
    private val textPaint = TextPaint()

    init {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        line1Paint.color = Color.WHITE
        line1Paint.style = Paint.Style.STROKE
        line1Paint.strokeWidth = 20f
        line2Paint.color = Color.YELLOW
        line2Paint.style = Paint.Style.STROKE
        line2Paint.strokeWidth = 20f
        textPaint.isAntiAlias = true
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val ch = game.getObject(p)
                if (ch == ' ') continue
                val s = game.pos2state(p)
                textPaint.color = if (game[p] == ch) Color.GRAY else if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
                val text = ch.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows + 1)
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                fun f(p2: Position) = if (!game.isValid(p2)) '.' else game[p2]
                fun g(p2: Position): Boolean {
                    val (ch, ch2) = f(p) to f(p2)
                    return ch != ch2 && (ch == '.' || ch2 == '.' || ch != ' ' && ch2 != ' ')
                }
                fun h(p2: Position): Boolean {
                    val (ch, ch2) = game.getObject(p) to game.getObject(p2)
                    return ch != ch2 && ch != ' ' && ch2 != ' '
                }
                run {
                    val p2 = Position(r - 1, c)
                    val (b1, b2) = g(p2) to (r in 1..<rows && c < cols && h(p2))
                    if (b1 || b2)
                        canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(),
                            if (b1) line1Paint else line2Paint)
                }
                run {
                    val p2 = Position(r, c - 1)
                    val (b1, b2) = g(p2) to (c in 1..<cols && r < rows && h(p2))
                    if (b1 || b2)
                        canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(),
                            if (b1) line1Paint else line2Paint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = TataminoGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}