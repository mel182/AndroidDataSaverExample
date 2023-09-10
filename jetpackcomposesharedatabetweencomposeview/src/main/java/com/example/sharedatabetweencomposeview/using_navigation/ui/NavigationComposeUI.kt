package com.example.navigationcompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationcompose.ui.DetailScreen
import com.example.navigationcompose.ui.MainScreen
import com.example.navigationcompose.ui.Screen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = "${Screen.DetailScreen.route}/{name}" ,
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "Melchior"
                    nullable = true
                }
            )
        ) { entry ->
            DetailScreen(navController = navController, name = entry.arguments?.getString("name"))
        }
    }
}

