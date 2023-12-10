package com.jetpackcompose.multiplebackstack

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title:String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)
