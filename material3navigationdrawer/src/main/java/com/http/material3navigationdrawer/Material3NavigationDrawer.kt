@file:OptIn(ExperimentalMaterial3Api::class)

package com.http.material3navigationdrawer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.http.material3navigationdrawer.ui.theme.DataSaverExampleAppTheme
import kotlinx.coroutines.launch

class Material3NavigationDrawer : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {

                val items = listOf(
                    NavigationItem(
                        title = "All",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    NavigationItem(
                        title = "Urgent",
                        selectedIcon = Icons.Filled.Info,
                        unselectedIcon = Icons.Outlined.Info,
                        badgeCount = 45
                    ),
                    NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings
                    ),
                )

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    //ModalNavigationDrawer - > The default one
                    //PermanentNavigationDrawer -> is a permanent navigation drawer and it is meant for tablet and desktop screens
                    //DismissibleNavigationDrawer -> is combination of modal and permanent navigation drawer
                    ModalNavigationDrawer(
                        drawerContent = {
                            //ModalDrawerSheet
                            //PermanentDrawerSheet -> used when PermanentNavigationDrawer is selected
                            //DismissibleDrawerSheet -> used when DismissibleNavigationDrawer is selected
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, navigationItem ->
                                    NavigationDrawerItem(
                                        label = {
                                           Text(text = navigationItem.title)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                         navigationItem.selectedIcon
                                                } else navigationItem.unselectedIcon,
                                                contentDescription = navigationItem.title
                                            )
                                        },
                                        badge = {
                                            navigationItem.badgeCount?.let {
                                                Text(text = it.toString())
                                            }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState //-> Only for modal and dismissible drawer sheet
                    ) {
                        Scaffold(
                            containerColor = Color.White,
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text(text = "Todo App")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                if (drawerState.isOpen) {
                                                    drawerState.close()
                                                } else {
                                                    drawerState.open()
                                                }
                                            }
                                        }) {
                                            Icon(imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu",
                                                tint = Color.Black
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.smallTopAppBarColors(
                                        containerColor = Color.LightGray,
                                        titleContentColor = Color.Black
                                    )
                                )
                            }
                        ) { paddingValues ->
                            println(paddingValues)
                        }
                    }
                    
                }
            }
        }
    }
}