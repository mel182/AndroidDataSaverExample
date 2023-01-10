package com.example.jetpackcomposetextautoscale.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified

@Composable
fun AutoResizedText(
    text:String,
    style:TextStyle = MaterialTheme.typography.body1,
    modifier: Modifier = Modifier,
    softWrap:Boolean = false,
    color:Color = style.color
) {
    // Soft wrap makes sure that the text does not go beyond our component bounds.

    var resizedTextStyle by remember {
        mutableStateOf(style)
    }

    var shouldDrawText by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.body1.fontSize

    Text(
        text = text,
        modifier = modifier.drawWithContent {
              if (shouldDrawText) {
                  drawContent()
              }
        },
        color = color,
        softWrap = softWrap,
        style = resizedTextStyle,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {

                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }

                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDrawText = true
            }
        }
    )
}