package com.example.datasaverexampleapp.appbar.tabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_app_bar_tabs.*

class AppBarTabsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_tabs)
        setSupportActionBar(tabs_toolbar)

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

        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab?)
            {
                tab?.apply {
                    title = titleList[position]
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        title = titleList[0]
    }
}