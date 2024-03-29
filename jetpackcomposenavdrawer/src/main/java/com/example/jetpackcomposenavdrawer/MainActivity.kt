package com.example.jetpackcomposenavdrawer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import com.example.jetpackcomposenavdrawer.ui.theme.DataSaverExampleAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    DataSaverExampleAppTheme {
                        val scaffoldState = rememberScaffoldState()
                        val scope = rememberCoroutineScope()
                        Scaffold(scaffoldState = scaffoldState,
                            topBar = {
                                AppBar(
                                    onNavigationClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    }
                                )
                            },
                            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                            drawerContent = {
                                DrawerHeader()
                                DrawerBody(items = listOf(
                                    MenuItem(
                                        id = "home",
                                        title = "Home",
                                        contentDescription = "Go to home screen",
                                        icon = Icons.Default.Home
                                    ),
                                    MenuItem(
                                        id = "settings",
                                        title = "Settings",
                                        contentDescription = "Go to settings screen",
                                        icon = Icons.Default.Settings
                                    ),
                                    MenuItem(
                                        id = "help",
                                        title = "Help",
                                        contentDescription = "Get help",
                                        icon = Icons.Default.Info
                                    )
                                ),
                                    onItemClick = {
                                        Log.i("TAG45","Clicked on item with title: ${it.title}")
                                        scope.launch {
                                            scaffoldState.drawerState.close()
                                        }
                                    })
                            }
                        ) {

                        }
                    }
                }
            }
        )
    }
}