package com.example.sharedatabetweencomposeview.using_composition_local_of

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sharedatabetweencomposeview.ui.theme.DataSaverExampleAppTheme
import kotlinx.coroutines.launch

/**
 * This is an example on how to use composition local of to share data between composable views
 * Pros:
 * - Can be use within all composables not view models
 * Cons:
 * - Can only be applied in composable
 * - It can break previews if composition locals cannot be found
 *
 */
class UsingCompositionLocalOfExampleMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.fillMaxSize().background(color = Color.LightGray)) {
                        Text(text = "Shared data using composition local of example",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 10.dp),
                            textAlign = TextAlign.Center
                        )
                        CompositionLocalOfExample()
                    }
                }
            }
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf {
    SnackbarHostState() // You cannot use composable only class and other instances
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompositionLocalOfExample() {

    val snackbarHostState = LocalSnackbarHostState.current
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {

        Scaffold(snackbarHost = {
            SnackbarHost(hostState = LocalSnackbarHostState.current)
        }) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                MyScreen()
            }
        }
    }
}

@Composable
private fun MyScreen() {
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    Button(onClick = {
        scope.launch {
            snackbarHostState.showSnackbar("Hello word!")
        }
    }) {
        Text(text = "Show snack bar")
    }
}