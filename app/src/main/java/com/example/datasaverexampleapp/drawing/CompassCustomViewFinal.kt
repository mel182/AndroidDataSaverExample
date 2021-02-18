package com.example.datasaverexampleapp.drawing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import com.example.datasaverexampleapp.R
import kotlin.math.cos
import kotlin.math.min

class CompassCustomViewFinal : View {

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

    private val borderGradientColors = IntArray(4)
    private val borderGradientPositions = FloatArray(4)
    private val glassGradientColors = IntArray(5)
    private val glassGradientPositions = FloatArray(5)

    private var skyHorizonColorFrom:Int = 0
    private var skyHorizonColorTo:Int = 0
    private var groundHorizonColorFrom:Int = 0
    private var groundHorizonColorTo:Int = 0

    var pitch:Float = 0f
    var roll:Float = 0f

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    {
        isFocusable = true
        val bearingAttrValue = context?.obtainStyledAttributes(
            attrs,
            R.styleable.CompassCustomView,
            defStyleAttr,
            0
        )

        bearingAttrValue?.let {

            if (bearingAttrValue.hasValue(R.styleable.CompassCustomView_bearing))
                bearing = bearingAttrValue.getFloat(R.styleable.CompassCustomView_bearing, 0f)

            bearingAttrValue.recycle()
        }

        context?.let { context ->

            circlePaint.apply {
                color = ContextCompat.getColor(context, R.color.compass_background)
                strokeWidth = 3f
                style = Paint.Style.STROKE
            }

//            textPaint.color = ContextCompat.getColor(context, R.color.compass_marker_color)

            textPaint.apply {
                color = ContextCompat.getColor(context, R.color.compass_marker_color)
                isFakeBoldText = true
                isSubpixelText = true
                textAlign = Paint.Align.LEFT
                textSize = 30F
            }

            textHeight = textPaint.measureText("yY").toInt()
            markerPaint.apply {
                color = ContextCompat.getColor(context, R.color.compass_marker_color)
                alpha = 200
                strokeWidth = 1F
                style = Paint.Style.STROKE
                setShadowLayer(2F, 1F, 1F, ContextCompat.getColor(context, R.color.shadow_color)) // New implementation
            }

            // New implementation
            borderGradientColors[3] = ContextCompat.getColor(context, R.color.outer_border)
            borderGradientColors[2] = ContextCompat.getColor(context, R.color.inner_border_one)
            borderGradientColors[1] = ContextCompat.getColor(context, R.color.inner_border_two)
            borderGradientColors[0] = ContextCompat.getColor(context, R.color.inner_border)

            borderGradientPositions[3] = 0.0f
            borderGradientPositions[2] = 1-0.03f
            borderGradientPositions[1] = 1-0.06f
            borderGradientPositions[0] = 1.0f

            val glassColor = 245
            glassGradientColors[4] = Color.argb(65, glassColor, glassColor, glassColor)
            glassGradientColors[3] = Color.argb(100, glassColor, glassColor, glassColor)
            glassGradientColors[2] = Color.argb(50, glassColor, glassColor, glassColor)
            glassGradientColors[1] = Color.argb(0, glassColor, glassColor, glassColor)
            glassGradientColors[0] = Color.argb(0, glassColor, glassColor, glassColor)

            glassGradientPositions[4] = 1-0.0f
            glassGradientPositions[3] = 1-0.06f
            glassGradientPositions[2] = 1-0.10f
            glassGradientPositions[1] = 1-0.20f
            glassGradientPositions[0] = 1-1.0f

            skyHorizonColorFrom = ContextCompat.getColor(context, R.color.horizon_sky_from)
            skyHorizonColorTo = ContextCompat.getColor(context, R.color.horizon_sky_to)
            groundHorizonColorFrom = ContextCompat.getColor(context, R.color.horizon_ground_from)
            groundHorizonColorTo = ContextCompat.getColor(context, R.color.horizon_ground_to)

            // New implementation
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
        setMeasuredDimension(dimension, dimension)
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

        val ringWidth = textHeight + 4

        val px = measuredWidth / 2
        val py = measuredHeight / 2
        val center = Point(px, py)

        val radius = min(px, py) - 2
        val boundBox = RectF(
            center.x.toFloat() - radius,
            center.y.toFloat() - radius,
            center.x.toFloat() + radius,
            center.y.toFloat() + radius
        )

        val innerBoundingBox = RectF(
            center.x.toFloat() - radius + ringWidth,
            center.y.toFloat() - radius + ringWidth,
            center.x.toFloat() + radius - ringWidth,
            center.y.toFloat() + radius - ringWidth
        )

        val innerRadius = innerBoundingBox.height()/2

        // Bottom layer at the outside
        val borderGradient = RadialGradient(
            px.toFloat(),
            py.toFloat(),
            radius.toFloat(),
            borderGradientColors,
            borderGradientPositions,
            Shader.TileMode.CLAMP
        )
        val pgb = Paint().apply {
            shader = borderGradient
        }

        val outerRingPath = Path().apply {
            addOval(boundBox, Path.Direction.CW)
        }

        canvas?.drawPath(outerRingPath, pgb)

        // The artificial horizon
        val skyShader = LinearGradient(
            center.x.toFloat(), innerBoundingBox.top,
            center.x.toFloat(), innerBoundingBox.bottom,
            skyHorizonColorFrom, skyHorizonColorTo,
            Shader.TileMode.CLAMP
        )

        val skyPaint = Paint().apply {
            shader = skyShader
        }

        val groundShader = LinearGradient(
            center.x.toFloat(), innerBoundingBox.top,
            center.x.toFloat(), innerBoundingBox.bottom,
            groundHorizonColorFrom, groundHorizonColorTo,
            Shader.TileMode.CLAMP
        )

        val groundPaint = Paint().apply {
            shader = groundShader
        }

        // Pitch and roll values clamp them within +/- 90 degrees and +/- 180 degrees.
        var tiltDegrees = pitch
        while (tiltDegrees > 90 || tiltDegrees < -90){

            if (tiltDegrees > 90 )
                tiltDegrees = -90 + (tiltDegrees - 90)

            if (tiltDegrees < -90)
                tiltDegrees = 90 - (tiltDegrees + 90)
        }

        var rollDegree:Float = roll
        while (rollDegree > 180 || rollDegree < -180)
        {
            if (rollDegree > 180)
                rollDegree = -180 + (rollDegree - 180)

            if (rollDegree < -180)
                rollDegree = 180 - (rollDegree + 180)
        }

        // Create paths that will fill each segment of the circle (ground and sky). The proportion
        // of each segment should be related to the clamped pitch.
        val skyPath = Path().apply {
            addArc(innerBoundingBox, -tiltDegrees, (180 + (2 * tiltDegrees)))
        }

        // Spin the canvas around the center in the opposite direction to the current roll, and draw the sky
        // and ground paths using the Paints you created.
        canvas?.let {
            it.save()
            it.rotate(-rollDegree, px.toFloat(), py.toFloat())
            it.drawOval(innerBoundingBox, groundPaint)
            it.drawPath(skyPath, skyPaint)
            it.drawPath(skyPath, markerPaint)
        }

        // The facing marking. Start by calculating the start and endpoints for the horizontal horizon markings.
        val markWidth = radius / 3
        val startX = center.x - markWidth
        val endX = center.x + markWidth

        // To make the horizon values easier to read, you should ensure that the pitch scale always start at the current value.
        // The following code calculates the position of the UI between the ground and sky on the horizon face.
        val h = innerRadius* cos(Math.toRadians((90 - tiltDegrees).toDouble()))
        val justTiltY = center.y - h

        // Find the number of pixels representing each degree of tilt
        val pxPerDegree = (innerBoundingBox.height()/2)/45f

        // Iterate over 180 degrees, centered on the current tilt value, to give a sliding scale of possible pitch


        var i = 90
        while (i >= -90) {
            i -= 10

            val ypos = justTiltY + i*pxPerDegree

            // Only display the scale within the inner face
            if ((ypos < (innerBoundingBox.top + textHeight)) || (ypos > innerBoundingBox.bottom - textHeight))
                continue

            // Draw a line and the tilt angle for each scale increment
            canvas?.drawLine(
                startX.toFloat(),
                ypos.toFloat(),
                endX.toFloat(),
                ypos.toFloat(),
                markerPaint
            )

            val displayPos = (tiltDegrees - i).toInt()
            val displayString = displayPos.toString()
            val stringSizeWidth = textPaint.measureText(displayString)

            canvas?.drawText(
                displayString,
                (center.x - stringSizeWidth / 2),
                (ypos + 1).toFloat(),
                textPaint
            )
        }

        // Draw a thicker line at the earth/sky interface. Change the stroke thickness of the marker-Paint
        // object before drawing the line (and then set it back to the previous value)
        markerPaint.strokeWidth = 2F
        canvas?.drawLine(
            (center.x - radius / 2).toFloat(),
            justTiltY.toFloat(),
            (center.x + radius / 2).toFloat(),
            justTiltY.toFloat(), markerPaint
        )
        markerPaint.strokeWidth = 1F

        // To make it easier to read the exact roll, you should draw an arrow and display a text string
        // that shows the value.
        //
        // Create a new Path and use the moveTo/lineTo methods to construct an open arrow that points straight up.
        // Draw the path and a text string that shows the current roll
        //
        // Draw arrow
        val rollArrow = Path().apply {
            moveTo((center.x - 3).toFloat(), innerBoundingBox.top + 14)
            lineTo(center.x.toFloat(), innerBoundingBox.top + 10)
            moveTo((center.x + 3).toFloat(), innerBoundingBox.top + 14)
            lineTo(center.x.toFloat(), innerBoundingBox.top + 10)
        }
        canvas?.drawPath(rollArrow, markerPaint)

        // Draw the string
        val rollText = rollDegree.toString()
        val rollTextWidth = textPaint.measureText(rollText)
        canvas?.drawText(
            rollText,
            (center.x - rollTextWidth / 2),
            innerBoundingBox.top + textHeight + 2,
            textPaint
        )

        // Spin the canvas back to upright so that you can draw the rest of the face markings
        canvas?.restore()

        // Draw the roll dial markings by rotating the canvas 10 degrees
        // at a time, drawing a value every 30 degrees and otherwise draw a mark. When you've completed the face,
        // restore the canvas to its upright position

        canvas?.let {
            it.save()
            it.rotate(180F, center.x.toFloat(), center.y.toFloat())
        }

        var index = -180
        while (index < 180) {
            index += 10

            // Show a numeric value every 30 degrees
            if (index % 30 == 0)
            {
                val rollString = (index*-1).toString()
                val rollStringWidth = textPaint.measureText(rollString)
                val rollStringCenter = PointF(
                    center.x - rollStringWidth / 2,
                    innerBoundingBox.top + 1 + textHeight
                )
                canvas?.drawText(rollString, rollStringCenter.x, rollStringCenter.y, textPaint)
            } else {
                // Otherwise draw a marker line
                canvas?.drawLine(
                    center.x.toFloat(),
                    innerBoundingBox.top,
                    center.x.toFloat(),
                    innerBoundingBox.top + 5,
                    markerPaint
                )
            }

            canvas?.rotate(10F, center.x.toFloat(), center.y.toFloat())
        }

        canvas?.restore()

        // Final step in creating the face is drawing the heading markers around the outside edge.

        canvas?.let {
            it.save()
            bearing?.let { bearing ->
                it.rotate((-1 * (bearing)), px.toFloat(), py.toFloat())
            }

            val increment:Double = 22.5

            var markerIndex:Double = 0.0
            while (markerIndex < 360) {
                markerIndex += increment

                val cd = CompassDirection.values()
                val headString = cd.toString()

                val headStringWidth = textPaint.measureText(headString)
                val headingCenter = PointF(
                    center.x - headStringWidth / 2,
                    boundBox.top + 1 + textHeight)

                if (markerIndex % increment == 0.0) {
                    it.drawText(headString, headingCenter.x, headingCenter.y, textPaint)
                } else {
                    it.drawLine(center.x.toFloat(), boundBox.top, center.x.toFloat(), boundBox.top + 3, markerPaint)
                }

                it.rotate(increment.toFloat(), center.x.toFloat(), center.y.toFloat())
            }

            it.restore()
        }

        // Add the 'glass dome' over the top to give the illusion of a watch face. Using the
        // radial gradient array you constructed earlier, create a new Shader and Paint object.
        // Use them to draw a circle over the inner face that makes it look it's covered in glass.
        val glassShader = RadialGradient(px.toFloat(),py.toFloat(),innerRadius, glassGradientColors, glassGradientPositions, Shader.TileMode.CLAMP)
        val glassPaint = Paint().apply {
            shader = glassShader
        }

        canvas?.drawOval(innerBoundingBox, glassPaint)

        // Draw two more circles as clean border for the inner and outer face
        // boundaries. Then restore the canvas to upright, and finish the 'onDraw' method.

        // Draw the outer ring
        canvas?.drawOval(boundBox, circlePaint)

        // Draw the inner ring
        circlePaint.strokeWidth = 2F
        canvas?.drawOval(innerBoundingBox, circlePaint)

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