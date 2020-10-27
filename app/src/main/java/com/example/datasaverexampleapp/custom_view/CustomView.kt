package com.example.datasaverexampleapp.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View

class CustomView : View {

    // Create the new paint brushes
    // NOTE: For efficiency this should be done in the view's constructor
    private var textPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : this(context,null)
    constructor(context: Context,attrs: AttributeSet?) : this(context,attrs,0)
    constructor(context: Context, attrs:AttributeSet?, defStyleAttr:Int) : super(context,attrs,defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureHeight = measureHeight(heightMeasureSpec)
        val measureWidth = measureWidth(widthMeasureSpec)

        // MUST make this call to setMeasuredDimension
        // or you will cause a runtime exception when
        // the control is laid out.
        setMeasuredDimension(measureHeight,measureWidth)
    }

    private fun measureHeight(measureSpec:Int) : Int
    {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        // Calculate the view height


        // Default size in pixel if no limits are specified
        var result:Int = 500

        if (specMode == MeasureSpec.AT_MOST)
        {
            // Calculate the ideal size of your control within this maximum size.
            // If your control fills the available space return the outer bound
            result = specSize
        } else if (specMode == MeasureSpec.EXACTLY)
        {
            // If your control can fit within these bounds return that value.
            result = specSize
        }

        return result
    }

    private fun measureWidth(measureSpec:Int) : Int
    {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        // Calculate the view height
        return specSize
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        // Draw your visual interface

        // Get the size of the control based on the last call to onMeasure
        val height = measuredHeight
        val width = measuredWidth

        // Find the center
        val px = width/2
        val py = height/2

        textPaint.color = Color.BLACK

        val displayText = "Hello View"

        // Measure the width of the text string
        val textWidth = textPaint.measureText(displayText)

        // Draw the text string in the center of the control
        canvas?.drawText(displayText,px-textWidth/2, py.toFloat(),textPaint)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Return if the event was handled
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        // Return true if the event was handled
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        // Get the type of action this event represents
        val actionPerformed = event?.action
        // Return true if the event was handled
        return true
    }


}