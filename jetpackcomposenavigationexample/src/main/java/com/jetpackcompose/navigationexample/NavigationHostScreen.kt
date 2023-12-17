package com.jetpackcompose.navigationexample

sealed class NavigationHostScreen(val route:String) {
    object MainScreen: NavigationHostScreen("main_screen")
    object DetailScreen: NavigationHostScreen("detail_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
