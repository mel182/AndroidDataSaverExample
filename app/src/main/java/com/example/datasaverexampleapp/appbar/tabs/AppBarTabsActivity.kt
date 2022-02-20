package com.example.datasaverexampleapp.appbar.tabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityAppBarTabsBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AppBarTabsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_tabs)

        DataBindingUtil.setContentView<ActivityAppBarTabsBinding>(
            this, Layout.activity_app_bar_tabs
        ).apply {

            setSupportActionBar(tabsToolbar)

            val titleList = arrayListOf(
                "Home",
                "Profile",
                "Notification"
            )

            val pagerAdapter = ViewPager2Adapter(titleList)
            tabsViewpager.adapter = pagerAdapter

            TabLayoutMediator(tabLayout, tabsViewpager) { tab, position ->
                tab.text = titleList[position]
                tabsViewpager.setCurrentItem(tab.position, true)
            }.attach()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
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
}