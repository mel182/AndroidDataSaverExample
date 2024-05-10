package com.jetpackcompose.circularindicatordraggable.feature.domain

import androidx.compose.ui.geometry.Offset
import com.jetpackcompose.circularindicatordraggable.StartAngle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KProperty

class ProgressNumbOffsetCalculation(
    private val startAngle: StartAngle,
    private val radius: Float,
    private val appliedAngle: Double
): ReadOnlyProperty<Any?, Offset> {

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): Offset = when(startAngle) {
        StartAngle.degree_0 -> Offset(
            radius * cos((abs(appliedAngle)) * PI / 180f).toFloat(),
            radius * sin((abs(appliedAngle)) * PI / 180f).toFloat()
        )
        StartAngle.degree_90 -> Offset(
            radius * cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
            radius * sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
        )
        StartAngle.degree_180 -> Offset(
            radius * cos((-180 + abs(appliedAngle)) * PI / 180f).toFloat(),
            radius * sin((-180 + abs(appliedAngle)) * PI / 180f).toFloat()
        )
        StartAngle.degree_270 -> Offset(
            radius * cos((-270 + abs(appliedAngle)) * PI / 180f).toFloat(),
            radius * sin((-270 + abs(appliedAngle)) * PI / 180f).toFloat()
        )
    }
}