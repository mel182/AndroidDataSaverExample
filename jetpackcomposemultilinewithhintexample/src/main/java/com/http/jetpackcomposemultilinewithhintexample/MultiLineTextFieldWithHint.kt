package com.http.jetpackcomposemultilinewithhintexample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun MultiLineTextFieldWithHint(
    value:String,
    onValueChange: (String) -> Unit,
    modifier:Modifier = Modifier,
    hintText:String = "",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines:Int = 4
) {
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        maxLines = maxLines,
        decorationBox = { innerTextField ->

            Box(modifier = modifier) {

                if (value.isEmpty()) {
                    Text(
                        text = hintText,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                }
                innerTextField()
            }
            
        }
    )
}