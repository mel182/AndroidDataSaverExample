package com.jetpackcompose.circularindicatordraggable.feature.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

class CircularProgressTextStyle(
    private val styleLightTheme: TextStyle = defaultTextStyleLightTheme,
    private val styleDarkTheme: TextStyle = defaultTextStyleDarkTheme
) {
    fun getTextStyle(isSystemInDarkTheme: Boolean): TextStyle =
        if (isSystemInDarkTheme) styleDarkTheme else styleLightTheme

}

val defaultTextStyleLightTheme = TextStyle(color = Color.Black, fontSize = 38.sp)
val defaultTextStyleDarkTheme = TextStyle(color = Color.White, fontSize = 38.sp)