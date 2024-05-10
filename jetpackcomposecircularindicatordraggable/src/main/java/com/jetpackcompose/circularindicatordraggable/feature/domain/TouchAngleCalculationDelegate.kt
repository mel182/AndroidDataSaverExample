package com.jetpackcompose.circularindicatordraggable.feature.domain

import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import com.jetpackcompose.circularindicatordraggable.StartAngle
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.reflect.KProperty

class TouchAngleCalculationDelegate(
    private val startAngle: StartAngle,
    private val center: Offset,
    private val motionEvent: MotionEvent
) : ReadOnlyProperty<Any?, Double> {
    override fun getValue(
        thisRef: Any?, 
        property: KProperty<*>
    ): Double {
        val touchAngle = calculateTouchAngle()
        return when(startAngle) {

            StartAngle.degree_0 -> (touchAngle + 270f).mod(360f).toDouble()
            StartAngle.degree_90 -> touchAngle.mod(360f).toDouble()
            StartAngle.degree_180 -> (touchAngle + 90f).mod(360f).toDouble()
            StartAngle.degree_270 -> (touchAngle + 180f).mod(360f).toDouble()
        }
    }

    private fun calculateTouchAngle(): Float = -atan2(
        x = center.y - motionEvent.y,
        y = center.x - motionEvent.x
    ) * (180f/ PI).toFloat()
}