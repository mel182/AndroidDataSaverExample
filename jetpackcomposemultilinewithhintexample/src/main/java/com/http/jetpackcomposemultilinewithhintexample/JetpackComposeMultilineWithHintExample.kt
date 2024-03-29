package com.http.jetpackcomposemultilinewithhintexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.http.jetpackcomposemultilinewithhintexample.ui.theme.DataSaverExampleAppTheme

class JetpackComposeMultilineWithHintExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    var text by remember {
                        mutableStateOf("")
                    }

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp)
                    ) {
                        MultiLineTextFieldWithHint(
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            hintText = "Write your message\nWrite your message\nWrite your message",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                                .background(color = Color.LightGray)
                                .padding(all = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
