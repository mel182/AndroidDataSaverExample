package com.example.datasaverexampleapp.compose.row_and_column_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.datasaverexampleapp.compose.row_and_column_example.ui.CustomText

class RowAndColumnComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_simple_compose)
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