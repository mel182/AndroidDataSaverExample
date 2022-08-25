package com.example.datasaverexampleapp.compose.bottom_navigation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.datasaverexampleapp.compose.bottom_navigation.nav_graph.BottomNavGraph
import com.example.datasaverexampleapp.compose.tablayout_example.ui.TabItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        BottomBar(navController = navController)
    }) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        TabItem.Home,
        TabItem.Profile,
        TabItem.Cart,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            screens.forEach { screen ->
                AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
            }
        }
    }
}

@Composable
fun RowScope.AddItem(screen: TabItem, currentDestination: NavDestination?, navController: NavHostController) {

    //TODO: if you want to use the default bottom navigation item
//    BottomNavigationItem(
//        label = {
//            Text(text = screen.title)
//        },
//        icon = {
//            Icon(painter = painterResource(id = screen.icon),
//                contentDescription = null,
//                tint = Color.Unspecified
//            )
//        },
//        selected = currentDestination?.hierarchy?.any {
//            it.route == screen.route
//        } == true, onClick = {
//            navController.navigate(screen.route)
//        }
//    )

   //TODO: if you want to use custom view as bottom navigation item
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight().weight(0.3f, true)
            .clickable(interactionSource = interactionSource,indication = null){
                navController.navigate(screen.route)
            },
    ) {
        Icon(painter = painterResource(id = screen.icon),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(text = screen.title)
    }
}
