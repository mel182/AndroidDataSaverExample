package com.jetpackcompose.bottomnavigtion.model

import androidx.compose.runtime.Composable
import com.jetpackcompose.bottomnavigtion.R
import com.jetpackcompose.bottomnavigtion.ui.CartScreen
import com.jetpackcompose.bottomnavigtion.ui.HomeScreen
import com.jetpackcompose.bottomnavigtion.ui.ProfileScreen

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(val route:String,val title:String, val icon: Int, val showBadge:Boolean = false, val screens: ComposableFun) {
    object Home: TabItem("home","Home", icon = R.drawable.ic_walking, screens = { HomeScreen() })
    object Cart: TabItem("cart","Cart", icon = R.drawable.ic_walking, screens = { CartScreen() })
    object Profile: TabItem("profile","Profile", icon = R.drawable.ic_walking, screens = { ProfileScreen() })
}

data class TabItem2(val route:String,val title:String, val icon: Int, var showBadge:Boolean = false)
