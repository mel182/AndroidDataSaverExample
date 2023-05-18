package com.example.sharedatabetweencomposeview.using_stateful_dependencies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sharedatabetweencomposeview.ui.theme.DataSaverExampleAppTheme
import com.example.sharedatabetweencomposeview.using_shared_viewmodel.shared_viewmodel.sharedViewModel
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.screens.NavScreen1
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.screens.NavScreen2
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.screens.NavScreen3
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.viewmodels.Screen1ViewModel
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.viewmodels.Screen2ViewModel
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.viewmodels.Screen3ViewModel

/**
 * This is an example on how to share data using a stateful dependencies, (e.g. a singleton).
 * Pros:
 * - Value remain the same on all views
 * Cons:
 * - It is consider dangerous since it does not support death process and everything needs to be reset manually.
 */
class UsingStatefulDependenciesExampleMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Shared using stateful example", color = Color.Black, fontSize = 20.sp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp), textAlign = TextAlign.Center)
                        ExampleContentView()
                    }
                }
            }
        }
    }
}

@Composable
private fun ExampleContentView() {

    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = "screen1"
    ) {

        composable("screen1") { entry ->
            val screen1ViewModel = entry.sharedViewModel<Screen1ViewModel>(navController = navHostController)
            val state by screen1ViewModel.count.collectAsStateWithLifecycle()

            NavScreen1(sharedState = state,
                onNavigate = {
                    screen1ViewModel.increment()
                    navHostController.navigate("screen2")
                }
            )
        }

        composable("screen2") { entry ->

            val viewmodel = entry.sharedViewModel<Screen2ViewModel>(navController = navHostController)
            val state by viewmodel.count.collectAsStateWithLifecycle()

            NavScreen2(sharedState = state,
                onNavigate = {
                    viewmodel.increment()
                    navHostController.navigate("screen3")
                }
            )
        }

        composable("screen3") { entry ->

            val viewmodel = entry.sharedViewModel<Screen3ViewModel>(navController = navHostController)
            val state by viewmodel.count.collectAsStateWithLifecycle()

            NavScreen3(sharedState = state,
                onNavigate = {

                }
            )
        }
    }
}