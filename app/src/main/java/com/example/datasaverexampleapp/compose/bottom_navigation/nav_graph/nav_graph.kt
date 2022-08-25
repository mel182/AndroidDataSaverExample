package com.example.datasaverexampleapp.compose.bottom_navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.datasaverexampleapp.compose.tablayout_example.ui.CartScreen
import com.example.datasaverexampleapp.compose.tablayout_example.ui.HomeScreen
import com.example.datasaverexampleapp.compose.tablayout_example.ui.ProfileScreen
import com.example.datasaverexampleapp.compose.tablayout_example.ui.TabItem

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = TabItem.Home.route) {
        composable(route = TabItem.Home.route){
            HomeScreen()
        }
        composable(route = TabItem.Cart.route){
            CartScreen()
        }
        composable(route = TabItem.Profile.route){
            ProfileScreen()
        }
    }
}