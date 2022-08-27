package com.example.datasaverexampleapp.compose.bottom_navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.datasaverexampleapp.compose.bottom_navigation.repository.BottomNavRepo
import com.example.datasaverexampleapp.compose.bottom_navigation.ui.BottomNavigationBar
import com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata.DemoViewModel

class BottomNavigationActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
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
fun UpdateUI(navHostController:NavHostController,destination:String) {

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
fun Navigation(navHostController:NavHostController, destination:String) {

    NavHost(navController = navHostController, startDestination = destination){

        composable("home") {
            HomeScreen()
        }
        composable("chat") {
            ChatScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}



@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Home screen")
    }
}

@Composable
fun ChatScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Chat screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Settings screen")
    }
}

data class BottomNavItem(var name:String, val route:String, val icon:ImageVector, var badgeCount: Int = 0)

