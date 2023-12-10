package com.jetpackcompose.multiplebackstack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jetpackcompose.multiplebackstack.ui.theme.DataSaverExampleAppTheme

class MultipleBackstackMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                val rootNavController = rememberNavController()
                val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEach { item ->
                                val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route
                                NavigationBarItem(
                                    selected = isSelected,
                                    label = {
                                        Text(text = item.title)
                                    },
                                    onClick = {
                                         rootNavController.navigate(item.title.lowercase()) {
                                             popUpTo(rootNavController.graph.findStartDestination().id) {
                                                 saveState = true
                                             }
                                             launchSingleTop = true
                                             restoreState = true
                                         }
                                    },
                                    icon = {
                                        Icon(imageVector = if (isSelected)
                                                                item.selectedIcon
                                                            else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { padding ->

                    NavHost(navController = rootNavController, startDestination = "home"){
                        composable("home") {
                            HomeNavHost(padding = padding)
                        }
                        composable("chat") {
                            ChatNavHost(padding = padding)
                        }
                        composable("settings") {
                            SettingsNavHost(padding = padding)
                        }
                    }
                }
            }
        }
    }
}