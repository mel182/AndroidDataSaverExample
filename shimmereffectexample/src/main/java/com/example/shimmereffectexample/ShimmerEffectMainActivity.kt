package com.example.shimmereffectexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shimmereffectexample.ui.theme.DataSaverExampleAppTheme
import kotlinx.coroutines.delay

class ShimmerEffectMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                var isLoading by remember {
                    mutableStateOf(true)
                }
                LaunchedEffect(key1 = true) {
                    delay(5000)
                    isLoading = false
                }
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                ) {
                    items(20) {
                        ShimmerListItem(isLoading = isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp),
                            contentAfterLoading = {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 16.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = "This is a long text to show that out shimmer "+
                                            "display is looking perfectly fine"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}