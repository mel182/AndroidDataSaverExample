package com.example.sharedatabetweencomposeview.using_shared_viewmodel

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
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.sharedatabetweencomposeview.ui.theme.DataSaverExampleAppTheme
import com.example.sharedatabetweencomposeview.using_shared_viewmodel.screens.PersonalDetailsScreen
import com.example.sharedatabetweencomposeview.using_shared_viewmodel.screens.TermsAndConditionsScreen
import com.example.sharedatabetweencomposeview.using_shared_viewmodel.shared_viewmodel.SharedViewModel
import com.example.sharedatabetweencomposeview.using_shared_viewmodel.shared_viewmodel.sharedViewModel

/**
 * This is an example on how to share data between compose view using a shared view model
 * Pros:
 * - Support real data changes
 * -
 *
 * Cons:
 * - Does not support process death by default, but you can fix that by inject a savedStateHandle
 * - Difficult to debugging when all view of coupled to one view model
 */
class UsingSharedViewModelMainActivity : ComponentActivity() {

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
                        Text(text = "Shared view model example", color = Color.Black, fontSize = 20.sp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp), textAlign = TextAlign.Center)
                        ExampleView()
                    }
                }
            }
        }
    }
}

@Composable
private fun ExampleView() {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = "onboarding"
    ) {

        navigation(
            startDestination = "personal_details",
            route = "onboarding"
        ) {

            composable("personal_details") { entry ->
                val viewmodel = entry.sharedViewModel<SharedViewModel>(navController = navHostController)
                val state by viewmodel.sharedState.collectAsStateWithLifecycle()

                PersonalDetailsScreen(
                    sharedState = state,
                    onNavigate = {
                        viewmodel.updateState()
                        navHostController.navigate("terms_and_conditions")
                    }
                )
            }
            composable("terms_and_conditions") { entry ->

                val viewmodel = entry.sharedViewModel<SharedViewModel>(navController = navHostController)
                val state by viewmodel.sharedState.collectAsStateWithLifecycle()

                TermsAndConditionsScreen(
                    sharedState = state,
                    onOnboardingFinished = {
                        navHostController.navigate(route = "home_screen") {
                            popUpTo("onboarding") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("home_screen") {
                Text(text = "Home screen")
            }
        }
    }
}