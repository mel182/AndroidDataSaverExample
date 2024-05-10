package com.jetpackcompose.circularindicatordraggable.feature.domain

import androidx.compose.ui.geometry.Offset
import com.jetpackcompose.circularindicatordraggable.StartAngle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KProperty

class StartPositionLineCalculation(
    private val startAngle: StartAngle,
    private val radius: Float,
    private val appliedAngle: Double
): ReadOnlyProperty<Any?, Pair<Offset, Offset>> {
    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): Pair<Offset, Offset> = when(startAngle) {
        StartAngle.degree_0 -> Pair(
            Offset(
                (radius - 10) * cos((abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius - 10) * sin((abs(appliedAngle)) * PI / 180f).toFloat()
            ),
            Offset(
                (radius + 10) * cos((abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius + 10) * sin((abs(appliedAngle)) * PI / 180f).toFloat()
            )
        )
        StartAngle.degree_90 -> Pair(
            Offset(
                (radius - 10) * cos((-90 + abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius - 10) * sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
            ),
            Offset(
                (radius + 10) * cos((-90 + abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius + 10) * sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
            )
        )
        StartAngle.degree_180 -> Pair(
            Offset(
                (radius - 10) * cos((-180 + abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius - 10) * sin((-180 + abs(appliedAngle)) * PI / 180f).toFloat()
            ),
            Offset(
                (radius + 10) * cos((-180 + abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius + 10) * sin((-180 + abs(appliedAngle)) * PI / 180f).toFloat()
            )
        )

        StartAngle.degree_270 -> Pair(
            Offset(
                (radius - 10) * cos((-270 + abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius - 10) * sin((-270 + abs(appliedAngle)) * PI / 180f).toFloat()
            ),
            Offset(
                (radius + 10) * cos((-270 + abs(appliedAngle)) * PI / 180f)
                    .toFloat(),
                (radius + 10) * sin((-270 + abs(appliedAngle)) * PI / 180f).toFloat()
            )
        )
    }
}