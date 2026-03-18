package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager

class FussyWaiterGameView(context: Context, val soundManager: SoundManager) : CellsGameView(context) {
    private val activity get() = context as FussyWaiterGameActivity
    private val game get() = activity.game
    private val rows get() = if (isInEditMode) 5 else game.rows
    private val cols get() = if (isInEditMode) 5 else game.cols
    override val rowsInView get() = rows
    override val colsInView get() = cols

    private val gridPaint = Paint()
    private val dFoods: Array<Drawable>
    private val dDrinks: Array<Drawable>

    init {
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        dFoods = arrayOf(
            "images/hamburger.png",
            "images/pizza.png",
            "images/fries.png",
            "images/donut.png",
            "images/fish.png",
            "images/icecream.png",
            "images/pig.png",
        ).map { fromImageToDrawable(it) }.toTypedArray()
        dDrinks = arrayOf(
            "images/drink_blue.png",
            "images/cup.png",
            "images/wine_red_glass.png",
            "images/beer_glass.png",
            "images/cocktail.png",
            "images/wine_white_glass.png",
            "images/lemonade_bottle.png",
        ).map { fromImageToDrawable(it) }.toTypedArray()
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows)
            for (c in 0 until cols)
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
        if (isInEditMode) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = game.getObject(p)
                if (o.food != ' ') {
                    val dFood = dFoods[o.food - 'a']
                    dFood.setBounds(cwc(c), chr(r), cwc2(c), chr2(r))
                    val s = game.pos2stateFood(p)
                    val alpha = if (s == AllowedObjectState.Error) 50 else 0
                    dFood.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                    dFood.draw(canvas)
                }
                if (o.drink != ' ') {
                    val dDrink = dDrinks[o.drink - 'A']
                    dDrink.setBounds(cwc2(c), chr2(r), cwc(c + 1), chr(r + 1))
                    val s = game.pos2stateDrink(p)
                    val alpha = if (s == AllowedObjectState.Error) 50 else 0
                    dDrink.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.argb(alpha, 255, 0, 0), BlendModeCompat.SRC_ATOP)
                    dDrink.draw(canvas)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols || row >= rows) return true
            val x = event.x - col * cellWidth
            val y = event.y - row * cellHeight
            val move = FussyWaiterGameMove(Position(row, col), if (x + y < cellWidth) 'a' else 'A')
            if (game.switchObject(move))
                soundManager.playSoundTap()
        }
        return true
    }
}