package com.example.datasaverexampleapp.compose.tablayout_example.ui

import androidx.compose.runtime.Composable
import com.example.datasaverexampleapp.R

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(val title:String, val icon: Int, val screens:ComposableFun) {
    object Home: TabItem("Home", icon = R.drawable.ic_walking, screens = { HomeScreen() })
    object Cart: TabItem("Cart", icon = R.drawable.ic_walking, screens = { CartScreen() })
    object Profile: TabItem("Profile", icon = R.drawable.ic_walking, screens = { ProfileScreen() })
}
