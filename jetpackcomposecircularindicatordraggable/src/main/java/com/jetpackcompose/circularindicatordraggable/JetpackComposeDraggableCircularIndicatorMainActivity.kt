package com.jetpackcompose.circularindicatordraggable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetpackcompose.circularindicatordraggable.ui.theme.DataSaverExampleAppTheme

class JetpackComposeDraggableCircularIndicatorMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.darkGray)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CustomCircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(250.dp)
                                .background(color = colorResource(id = R.color.darkGray)),
                            initialValue = 30,
                            valueUnit = "%",
                            primaryColor = colorResource(id = R.color.orange),
                            secondaryColor = colorResource(id = R.color.gray),
                            circularRadius = 230f,
                            onPositionChange = { position ->

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