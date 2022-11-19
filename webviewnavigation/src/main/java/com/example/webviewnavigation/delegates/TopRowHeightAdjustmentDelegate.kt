package com.example.webviewnavigation.delegates

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.example.webviewnavigation.WebViewViewModel

@Composable
fun topRowHeightDelegate(webViewViewModel: WebViewViewModel) : RowHeight {

    val animationDurationMilli = 500

    val goBackEnabled by webViewViewModel.goBackEnabled.collectAsState()

    /*
    val rowHeight by animateFloatAsState(targetValue = if (goBackEnabled) {
        56f
    } else {
        0f
    },
    animationSpec = tween(durationMillis = animationDurationMilli))
    */

    val iconSize by animateFloatAsState(targetValue = if (goBackEnabled) {
        30f
    } else {
        0f
    },
        animationSpec = tween(durationMillis = animationDurationMilli))

    return RowHeight(height = 56f, iconSize = iconSize)
}