package com.jetpackcompose.zoomableimage._ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import com.jetpackcompose.zoomableimage.data.Photo
import com.jetpackcompose.zoomableimage.R

@Composable
fun FullScreenPhoto(
    photo: Photo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scrim(onClose = onDismiss, modifier = Modifier.fillMaxSize())
        PhotoImage(photo = photo)
    }
}

@Composable
private fun Scrim(
    onClose: () -> Unit, 
    modifier: Modifier = Modifier
) {
    val strClose = stringResource(id = R.string.close)
    Box(modifier = modifier
        .fillMaxSize()
        .pointerInput(onClose) {
            detectTapGestures { onClose() }
        }
        .semantics {
            onClick(strClose) { onClose(); true }
        }
        .focusable()
        .onKeyEvent {
            if (it.key == Key.Escape) {
                onClose()
                true
            } else {
                false
            }
        }
        .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}

@Composable
private fun PhotoImage(
    photo: Photo,
    modifier: Modifier = Modifier
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableFloatStateOf(1f) }

    Image(
        painter = painterResource(id = photo.id),
        contentDescription = "Image full screen",
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tabOffset ->
                        zoom = if (zoom > 1f) 1f else 2f
                        offset = calculateDoubleTabOffset(zoom = zoom, size = size, tabOffset = tabOffset)
                    }
                )
            }.pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, gestureZoom, _ ->
                        offset = offset.calculateNewOffset(
                            centroid = centroid,
                            pan = pan,
                            zoom = zoom,
                            gestureZoom = gestureZoom,
                            size = size
                        )
                        zoom = maxOf(1f, zoom * gestureZoom)
                    }
                )
            }.graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom
                scaleY = zoom
                transformOrigin = TransformOrigin(pivotFractionX = 0f, pivotFractionY = 0f)
            }.aspectRatio(1f)
    )
}

private fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
            (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}

private fun calculateDoubleTabOffset(
    zoom: Float,
    size: IntSize,
    tabOffset: Offset
): Offset {
    val newOffset = Offset(x = tabOffset.x, y = tabOffset.y)
    return Offset(
        x = newOffset.x.coerceIn(0f, (size.width/zoom) * (zoom - 1f)),
        y = newOffset.y.coerceIn(0f, (size.height/zoom) * (zoom - 1f))
    )
}