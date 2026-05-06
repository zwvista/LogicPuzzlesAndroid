package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class CulturedBranchesGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as CulturedBranchesGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()
    private val dBranchUp: Drawable
    private val dBranchRight: Drawable
    private val dBranchDown: Drawable
    private val dBranchLeft: Drawable
    private val dBranchHorizontal: Drawable
    private val dBranchVertical: Drawable

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        textPaint.isAntiAlias = true
        dBranchUp = fromImageToDrawable("images/branch_up.png")
        dBranchRight = fromImageToDrawable("images/branch_right.png")
        dBranchDown = fromImageToDrawable("images/branch_down.png")
        dBranchLeft = fromImageToDrawable("images/branch_left.png")
        dBranchHorizontal = fromImageToDrawable("images/branch_horizontal.png")
        dBranchVertical = fromImageToDrawable("images/branch_vertical.png")
    }

    protected override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0..<rows)
            for (c in 0..<cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                fun f(dWall: Drawable) {
                    dWall.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                    dWall.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(0, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                    dWall.draw(canvas)
                }
                when (val o = game.getObject(p)) {
                    CulturedBranchesObject.Up -> f(dBranchUp)
                    CulturedBranchesObject.Right -> f(dBranchRight)
                    CulturedBranchesObject.Down -> f(dBranchDown)
                    CulturedBranchesObject.Left -> f(dBranchLeft)
                    CulturedBranchesObject.Horizontal -> f(dBranchHorizontal)
                    CulturedBranchesObject.Vertical -> f(dBranchVertical)
                    CulturedBranchesObject.Hint -> {
                        val text = game.pos2hint[p].toString()
                        val s = game.pos2state(p)
                        textPaint.color = if (s == HintState.Normal) Color.WHITE else if (s == HintState.Complete) Color.GREEN else Color.RED
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                    }
                    else -> {}
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val move = CulturedBranchesGameMove(Position(row, col))
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}