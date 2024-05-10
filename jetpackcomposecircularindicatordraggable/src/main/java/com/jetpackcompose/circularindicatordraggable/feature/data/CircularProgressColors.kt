package com.jetpackcompose.circularindicatordraggable.feature.data

import androidx.compose.ui.graphics.Color

class CircularProgressColorScheme(
    private val lightTheme: CircularProgressColors = defaultLightTheme,
    private val darkTheme: CircularProgressColors = defaultDarkTheme,
) {
    fun getColorScheme(isSystemInDarkTheme: Boolean): CircularProgressColors =
        if (isSystemInDarkTheme) darkTheme else lightTheme
}

data class CircularProgressColors(
    val progressBarColor: Color,
    val trackColor: Color,
    val numbOuterColor: Color,
    val numbInnerColor: Color,
    val startPositionIndicatorColor: Color,
    val outerIndicatorUnSelectedLineColor: Color,
    val outerIndicatorSelectedLineColor: Color,
)

val defaultLightTheme = CircularProgressColors(
    progressBarColor = ProgressBarBackgroundLightTheme,
    trackColor = ProgressBarProgressLightTheme,
    numbOuterColor = ProgressBarTintLightTheme,
    numbInnerColor = ColorPrimaryLightTheme,
    startPositionIndicatorColor = Color.Green,
    outerIndicatorUnSelectedLineColor = Color.Blue.copy(alpha = 0.3f),
    outerIndicatorSelectedLineColor = Color.Red
)

val defaultDarkTheme = CircularProgressColors(
    progressBarColor = ProgressBarBackgroundDarkTheme,
    trackColor = ProgressBarProgressDarkTheme,
    numbOuterColor = ProgressBarTintDarkTheme,
    numbInnerColor = ColorPrimaryDarkTheme,
    startPositionIndicatorColor = Color.Green,
    outerIndicatorUnSelectedLineColor = Color.Red.copy(alpha = 0.5f),
    outerIndicatorSelectedLineColor = Color.Red
)


