package com.jetpackcompose.zoomableimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.jetpackcompose.zoomableimage._ui.ImageList
import com.jetpackcompose.zoomableimage.data.Photo
import com.jetpackcompose.zoomableimage.ui.theme.DataSaverExampleAppTheme

class JetpackComposeZoomableImagesMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            DataSaverExampleAppTheme {
                ImageList(photos = listOf(
                    Photo(id = R.drawable.image_1),
                    Photo(id = R.drawable.image_2)
                ), modifier = Modifier.fillMaxSize())
            }
        }
    }
}