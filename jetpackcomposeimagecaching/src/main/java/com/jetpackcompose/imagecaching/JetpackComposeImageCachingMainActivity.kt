@file:OptIn(ExperimentalCoilApi::class)

package com.jetpackcompose.imagecaching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import com.jetpackcompose.imagecaching.ui.theme.DataSaverExampleAppTheme

class JetpackComposeImageCachingMainActivity : ComponentActivity() {

    val testImageUrl = "https://cdn.pixabay.com/photo/2016/09/07/10/37/kermit-1651325_1280.jpg"
    val testImageUrl2 = "https://cdn.pixabay.com/photo/2017/01/29/14/19/kermit-2018085_1280.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {

                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SubcomposeAsyncImage(model = testImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1280f / 847f),
                        loading = {
                            // Composable for showing view while loading
                            CircularProgressIndicator(
                                modifier = Modifier.width(20.dp),
                                color = Color.LightGray,
                                trackColor = Color.Blue
                            )
                        }
                    )
                    AsyncImage(model = testImageUrl2,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1280f / 692f)
                    )
                    
                    Button(
                        onClick = {
                            imageLoader.apply {
                                diskCache?.clear()
                                memoryCache?.clear()

                                // If you only want to remove a specific image from the cache
//                                diskCache?.remove(testImageUrl)
//                                memoryCache?.remove(MemoryCache.Key(testImageUrl))
                            }
                        }
                    ) {
                        Text(text = "Clear cache")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DataSaverExampleAppTheme {
        Greeting("Android")
    }
}