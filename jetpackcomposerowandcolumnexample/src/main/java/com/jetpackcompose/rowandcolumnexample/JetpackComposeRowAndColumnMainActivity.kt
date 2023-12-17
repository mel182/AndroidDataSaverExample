package com.jetpackcompose.rowandcolumnexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.datasaverexampleapp.compose.row_and_column_example.ui.CustomText

class JetpackComposeRowAndColumnMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Row and column compose activity"
        setContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()) {
                UIComponentInColumn()
                UIComponentInRow()
            }
        }
    }
}

@Composable
private fun UIComponentInColumn() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.Red))
    {
        CustomText("Column item")
        CustomText("Column item")
        CustomText("Column item")
    }
}

@Composable
private fun UIComponentInRow() {
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Green)) {
        CustomText("Row item")
        CustomText("Row item")
        CustomText("Row item")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        UIComponentInColumn()
        UIComponentInRow()
    }
}