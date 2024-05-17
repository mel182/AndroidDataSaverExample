package com.jetpackcompose.zoomableimage._ui

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jetpackcompose.zoomableimage.data.Photo

@Composable
fun PhotoItem(
    photo: Photo,
    inSelectedMode: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.aspectRatio(1f),
        tonalElevation = 3.dp
    ) {
        Box {
            val transition = updateTransition(targetState = selected, label = "selected")
            val padding by transition.animateDp(label = "padding") { selected ->
                if (selected) 10.dp else 0.dp
            }
            val roundedCornerShape by transition.animateDp(label = "corner") { selected ->
                if (selected) 16.dp else 0.dp
            }
            Image(
                painter = painterResource(id = photo.id),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .padding(padding)
                    .clip(
                        RoundedCornerShape(roundedCornerShape)
                    )
            )
            if (inSelectedMode) {
                if (selected) {
                    val bgColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .border(width = 2.dp, color = bgColor, shape = CircleShape)
                            .clip(CircleShape)
                            .background(color = bgColor)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.RadioButtonUnchecked,
                        tint = Color.White.copy(alpha = 0.7f),
                        contentDescription = null,
                        modifier = Modifier.padding(all = 6.dp)
                    )
                }
            }
        }
    }

}