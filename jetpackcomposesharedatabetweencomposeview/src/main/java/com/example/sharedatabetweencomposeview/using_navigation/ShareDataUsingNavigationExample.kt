package com.example.sharedatabetweencomposeview.using_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.navigationcompose.ui.theme.Navigation
import com.example.sharedatabetweencomposeview.ui.theme.DataSaverExampleAppTheme

/**
 * This is an example of sharing data using navigation in jetpack compose:
 * Pros:
 * - Easy and straight forward
 * - Data survive process death by default, meaning if
 *   the app is in the background and the OS killed
 *   the app that the OS will restore the backstack
 *   when the user open the app again.
 * Cons:
 * - Not useful to share complex data (e.g. parsable data)
 * - Not convenient to share data between multiple screens
 * - Data is stateless, so if you need to use data that
 *   changes everything it is not suitable
 */
class ShareDataUsingNavigationExample : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Navigation()
                }
            }
        }
    }

}