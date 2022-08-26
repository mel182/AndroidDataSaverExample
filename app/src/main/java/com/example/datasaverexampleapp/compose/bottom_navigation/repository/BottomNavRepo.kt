package com.example.datasaverexampleapp.compose.bottom_navigation.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import com.example.datasaverexampleapp.compose.bottom_navigation.BottomNavItem

object BottomNavRepo {

    val tabItems:ArrayList<BottomNavItem> = ArrayList<BottomNavItem>()

    init {
        tabItems.apply {
            add(BottomNavItem(name = "Home", route = "home", icon = Icons.Default.Home))
            add(BottomNavItem(name = "Chat", route = "chat", icon = Icons.Default.Notifications))
            add(BottomNavItem(name = "Settings", route = "settings", icon = Icons.Default.Settings))
        }
    }

}