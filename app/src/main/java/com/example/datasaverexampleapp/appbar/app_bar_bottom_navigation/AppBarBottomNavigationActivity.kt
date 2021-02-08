package com.example.datasaverexampleapp.appbar.app_bar_bottom_navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_app_bar_bottom_navigation.*

class AppBarBottomNavigationActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_bottom_navigation)

        val navController = Navigation.findNavController(this,fragment3.id)
        NavigationUI.setupWithNavController(app_bar_bottom_navigation, navController)

        title = app_bar_bottom_navigation.menu.findItem(R.id.contentFragment1).title

        app_bar_bottom_navigation.setOnNavigationItemSelectedListener {

            when(it.itemId)
            {
                R.id.contentFragment1 -> {
                    navController.navigate(it.itemId)
                    title = it.title
                    true
                }
                R.id.contentFragment2 -> {
                    navController.navigate(it.itemId)
                    title = it.title
                    true
                }
                R.id.contentFragment3 -> {
                    navController.navigate(it.itemId)
                    title = it.title
                    true
                }

                else -> false
            }
        }
    }
}