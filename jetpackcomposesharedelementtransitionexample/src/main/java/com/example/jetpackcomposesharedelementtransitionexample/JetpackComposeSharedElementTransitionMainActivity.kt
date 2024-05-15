@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.jetpackcomposesharedelementtransitionexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackcomposesharedelementtransitionexample.ui.theme.DataSaverExampleAppTheme

class JetpackComposeSharedElementTransitionMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            DataSaverExampleAppTheme {
                SharedTransitionLayout {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {

                        composable("list") {
                            val items = listOf(
                                AnimationListItem(R.drawable.ferrari_f1,"Item 1"),
                                AnimationListItem(R.drawable.ferrari_f1_2,"Item 2"),
                                AnimationListItem(R.drawable.ferrari_f1_3,"Item 3"),
                            )
                            ListScreen(items = items, animatedVisibilityScope = this) {
                                navController.navigate("detail/${it.image}/${it.title}")
                            }
                        }

                        composable(
                            route = "detail/{image}/{text}",
                            arguments = listOf(
                                navArgument("image") {
                                    type = NavType.IntType
                                },
                                navArgument("text") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val imageID = it.arguments?.getInt("image") ?: 0
                            val text = it.arguments?.getString("text") ?: ""
                            DetailScreen(item = AnimationListItem(image = imageID, title = text), animatedVisibilityScope = this)
                        }
                    }
                }
            }
        }
    }
}