package com.jetpackcompose.bottomnavigtion

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jetpackcompose.bottomnavigtion.ui.BottomNavigationBar

class JetpackComposeBottomNavigationMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bottom navigation Example"

        setContent {
            Log.i("TAG50","|------------------------ 1|")
            Log.i("TAG50","set content lambda called!")
            Log.i("TAG50","|------------------------ 2 |")
            val navHostController = rememberNavController()
            val current = navHostController.currentBackStackEntryAsState().value?.destination?.route ?: "home"
            UpdateUI(navHostController,current)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpdateUI(navHostController: NavHostController, destination:String) {

    Scaffold(bottomBar = {
        Log.i("TAG50","scaffold bottom bar lambda called!")
        BottomNavigationBar(
            navHostController = navHostController,
            onItemClick = {
                navHostController.navigate(it.route)
            }
        )
    }) {
        Log.i("TAG50","padding values lambda called!")
        Navigation(navHostController = navHostController,destination)
    }
}


@Composable
fun Navigation(navHostController: NavHostController, destination:String) {

    NavHost(navController = navHostController, startDestination = destination){

        composable("home") {
            BottomNavigationHomeScreen()
        }
        composable("chat") {
            BottomNavigationChatScreen()
        }
        composable("settings") {
            BottomNavigationSettingsScreen()
        }
    }
}



@Composable
fun BottomNavigationHomeScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Home screen")
    }
}

@Composable
fun BottomNavigationChatScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Chat screen")
    }
}

@Composable
fun BottomNavigationSettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Settings screen")
    }
}

data class BottomNavItem(var name:String, val route:String, val icon: ImageVector, var badgeCount: Int = 0)