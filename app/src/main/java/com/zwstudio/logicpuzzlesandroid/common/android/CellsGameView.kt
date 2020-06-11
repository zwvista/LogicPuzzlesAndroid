package com.zwstudio.logicpuzzlesandroid.common.android

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import java.io.IOException

// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
abstract class CellsGameView : View {
    protected var cellWidth = 0
    protected var cellHeight = 0
    protected abstract fun rowsInView(): Int
    protected abstract fun colsInView(): Int

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = measuredWidth.coerceAtMost(measuredHeight) / rowsInView().coerceAtLeast(colsInView())
        setMeasuredDimension(size * colsInView(), size * rowsInView())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (colsInView() < 1 || rowsInView() < 1) return
        cellWidth = width / colsInView() - 1
        cellHeight = height / rowsInView() - 1
        invalidate()
    }

    // http://stackoverflow.com/questions/11120392/android-center-text-on-canvas
    protected fun drawTextCentered(text: String?, x: Int, y: Int, canvas: Canvas, textPaint: TextPaint) {
        drawTextCentered(text, x, y, cellWidth, cellHeight, canvas, textPaint)
    }

    protected fun drawTextCentered(text: String?, x: Int, y: Int, wd: Int, ht: Int, canvas: Canvas, textPaint: TextPaint) {
        textPaint.textSize = ht.toFloat()
        val txtWd = textPaint.measureText(text).toInt()
        val txtSize = if (ht >= txtWd) ht else ht * ht / txtWd
        textPaint.textSize = txtSize.toFloat()
        val xPos = x + (wd - textPaint.measureText(text)) / 2
        val yPos = y + (ht - textPaint.descent() - textPaint.ascent()) / 2
        canvas.drawText(text!!, xPos, yPos, textPaint)
    }

    protected fun cwc(c: Int) = c * cellWidth + 1
    protected fun chr(r: Int) = r * cellHeight + 1
    protected fun cwc2(c: Int) = c * cellWidth + 1 + cellWidth / 2
    protected fun chr2(r: Int) = r * cellHeight + 1 + cellHeight / 2

    protected fun fromImageToDrawable(imageFile: String?): Drawable? {
        var result: Drawable? = null
        try {
            val `is` = context.applicationContext.assets.open(imageFile!!)
            val bmpLightbulb = BitmapFactory.decodeStream(`is`)
            result = BitmapDrawable(bmpLightbulb)
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }
}