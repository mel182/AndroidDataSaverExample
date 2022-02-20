@file:Suppress("NAME_SHADOWING","USELESS_ELVIS", "VARIABLE_WITH_REDUNDANT_INITIALIZER")

package com.example.datasaverexampleapp.custom_view.compass_example

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import com.example.datasaverexampleapp.R

class CompassCustomView : View {

    private var bearing:Float? = 1f
    set(value) {
        field = value?:1f
        invalidate()
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
    }

    private var markerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var northString:String = ""
    private var eastString:String = ""
    private var southString:String = ""
    private var westString:String = ""
    private var textHeight:Int = 0

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        isFocusable = true
        val bearingAttrValue = context?.obtainStyledAttributes(attrs, R.styleable.CompassCustomView,defStyleAttr,0)

        bearingAttrValue?.let {

            if (bearingAttrValue.hasValue(R.styleable.CompassCustomView_bearing))
                bearing = bearingAttrValue.getFloat(R.styleable.CompassCustomView_bearing,0f)

            bearingAttrValue.recycle()
        }

        context?.let { context ->

            circlePaint.apply {
                color = ContextCompat.getColor(context,R.color.compass_background)
                strokeWidth = 3f
                style = Paint.Style.FILL_AND_STROKE
            }

            textPaint.color = ContextCompat.getColor(context,R.color.compass_marker_color)
            textHeight = textPaint.measureText("yY").toInt()
            markerPaint.color = ContextCompat.getColor(context,R.color.compass_marker_color)
        }

        northString = resources.getString(R.string.cardinal_north)
        eastString = resources.getString(R.string.cardinal_east)
        southString = resources.getString(R.string.cardinal_south)
        westString = resources.getString(R.string.cardinal_west)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = measure(widthMeasureSpec)
        val measureHeight = measure(widthMeasureSpec)

        val dimension = measureWidth.coerceAtMost(measureHeight)
        setMeasuredDimension(dimension,dimension)
    }


    private fun measure(measureSpec: Int):Int{

        var result = 0

        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        result = if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            200
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            specSize
        }

        return result
    }

    override fun onDraw(canvas: Canvas?) {

        val px = (measuredHeight/2).toFloat()
        val py = (measuredWidth/2).toFloat()

        val radius = px.coerceAtMost(py)

        // Draw the background
        canvas?.apply {
            drawCircle(px,py,radius, circlePaint)

            // Rotate our perspective so that the 'top' is
            // facing the current bearing
            save()
            bearing?.let { rotate(-it,px,py) }

            val textWidth = textPaint.measureText("W")
            var cardinalX = 1f
            var cardinalY = 1f

            textWidth.let {
                cardinalX = px-it/2
                cardinalY = py-radius+it
            }


            for (index in 0 until 24)
            {
                // Draw a marker
                this.drawLine(px, py-radius, px, py-radius+10, markerPaint)
                this.save()
                this.translate(0F, textHeight.toFloat())

                // Draw the cardinal points
                if (index % 6 == 0)
                {
                    var directionString = ""
                    when(index)
                    {
                        0 -> {
                            directionString = northString?:""
                            textHeight.let {
                                val arrowHeight = (2 * it).toFloat()

                                this.drawLine(px,arrowHeight, px - 5, (3 * it).toFloat(), markerPaint)
                                this.drawLine(px,arrowHeight, px + 5, (3 * it).toFloat(), markerPaint)
                            }
                        }

                        6 -> directionString = eastString?:""
                        12 -> directionString = southString?:""
                        18 -> directionString = westString?:""
                    }
                    this.drawText(directionString, cardinalX,cardinalY,textPaint)
                } else if(index % 3 == 0)
                {
                    // Draw the text every alternate 45deg
                    val angle = (index*15).toString()
                    val angleTextWidth = textPaint.measureText(angle)

                    val angleTextX = (px- (angleTextWidth.div(2)))
                    val angleTextY = py-radius+textHeight

                    this.drawText(angle,angleTextX, angleTextY, textPaint)
                }
                this.restore()
                this.rotate(15f,px,py)
            }

            this.restore()
        }
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        super.dispatchPopulateAccessibilityEvent(event)

        return if (isShown)
        {
            event?.text?.add(bearing.toString())
            true
        } else {
            false
        }
    }
}