package com.example.navigationcompose.ui

sealed class Screen(val route:String) {
    object MainScreen: Screen(route = "main_screen")
    object DetailScreen: Screen(route = "detail_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
