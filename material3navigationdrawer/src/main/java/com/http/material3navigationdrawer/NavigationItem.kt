package com.http.material3navigationdrawer

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount:Int? = null
)
