package com.example.datasaverexampleapp.compose.grid_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ComposeGridListExampleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Grid list example"
        setContent {

            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {

                LazyVerticalGrid(columns = GridCells.Fixed(2), content = {

                    items(50) { index ->

                        if (index % 2 == 0) {

                            Box(modifier = Modifier
                                .padding(8.dp)
                                .aspectRatio(1f)
                                .clip(
                                    RoundedCornerShape(5.dp)
                                )
                                .background(Color.Green),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Item ${index}")
                            }
                        } else {
                            Box(modifier = Modifier
                                .padding(8.dp)
                                .aspectRatio(1f)
                                .clip(
                                    RoundedCornerShape(5.dp)
                                )
                                .background(Color.Red),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Item ${index}")
                            }
                        }
                    }
                })
            }
        }
    }
}