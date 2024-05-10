package com.jetpackcompose.circularindicatordraggable

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetpackcompose.circularindicatordraggable.feature.ui.ProgressBarCircular
import com.jetpackcompose.circularindicatordraggable.ui.theme.DataSaverExampleAppTheme

class JetpackComposeDraggableCircularIndicatorMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        ProgressBarCircular(
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.Center),
                            minValue = 0,
                            maxValue = 200,
                            valueUnit = "mA",
                            showOuterIndicatorLines = true,
                            showProgressNumb = true,
                            startAngle = StartAngle.degree_90,
                            onProgressChanged = {

                                Log.i("result","on progress changed: $it")

                            }
                        )
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