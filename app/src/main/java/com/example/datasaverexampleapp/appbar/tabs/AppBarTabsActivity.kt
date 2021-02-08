package com.example.datasaverexampleapp.appbar.tabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_app_bar_tabs.*

class AppBarTabsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_tabs)

        setSupportActionBar(tabs_toolbar)
        title = "App bar with tabs"

        val titleList = arrayListOf(
            "Home",
            "Profile",
            "Notification"
        )

        val pagerAdapter = ViewPager2Adapter(titleList)
        tabs_viewpager?.adapter = pagerAdapter

        TabLayoutMediator(tab_layout, tabs_viewpager) { tab, position ->
            tab.text = titleList[position]
            tabs_viewpager.setCurrentItem(tab.position, true)
        }.attach()
    }
}