package com.example.datasaverexampleapp.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.datasaverexampleapp.compose.navigation.screens.DetailScreen
import com.example.datasaverexampleapp.compose.navigation.screens.MainScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationHostScreen.MainScreen.route) {
        // In this block you can define composable
        composable(NavigationHostScreen.MainScreen.route) {
            MainScreen(navController = navController)
        }

        composable(route = "${NavigationHostScreen.DetailScreen.route}/{name}", arguments = listOf(
            navArgument("name") {
                type = NavType.StringType // required
                defaultValue = "John" // optional
                nullable = true // Optional
            }
        )) { entry ->
            DetailScreen(name = entry.arguments?.getString("name"))
        }
    }
}