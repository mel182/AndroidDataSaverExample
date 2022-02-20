package com.example.datasaverexampleapp.appbar.app_bar_bottom_navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityAppBarBottomNavigationBinding
import com.example.datasaverexampleapp.type_alias.Layout

class AppBarBottomNavigationActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_bottom_navigation)

        DataBindingUtil.setContentView<ActivityAppBarBottomNavigationBinding>(
            this, Layout.activity_app_bar_bottom_navigation
        ).apply {

            val navController = Navigation.findNavController(this@AppBarBottomNavigationActivity, fragment3.id)
            NavigationUI.setupWithNavController(appBarBottomNavigation, navController)

            title = appBarBottomNavigation.menu.findItem(R.id.contentFragment1).title

            appBarBottomNavigation.setOnItemSelectedListener {
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
}