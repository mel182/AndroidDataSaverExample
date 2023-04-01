package com.example.contextmenuexample.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CustomRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = Color.Blue

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Blue,
        lightTheme = false
    )

}